package com.objects.marketbridge.payment.service;

import com.objects.marketbridge.common.dto.KakaoPayApproveRequest;
import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.common.infra.KakaoPayService;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.controller.dto.CompleteOrderHttp;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.port.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.config.KakaoPayConfig.ONE_TIME_CID;
import static com.objects.marketbridge.order.domain.StatusCodeType.PAYMENT_COMPLETED;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final KakaoPayService kakaoPayService;


    @Transactional
    public CompleteOrderHttp.Response create(KakaoPayApproveResponse response) {

        // 1. Payment 엔티티 생성
        Payment payment = createPayment(response);
        paymentRepository.save(payment);

        // 2. Order - Payment 연관관계 매핑
        Order order = orderQueryRepository.findByOrderNoWithOrderDetailsAndProduct(response.getPartnerOrderId());
        order.linkPayment(payment);

        // 3. orderDetail 의 statusCode 업데이트
        payment.changeStatusCode(PAYMENT_COMPLETED.getCode());

        // TODO : payment 대신 order?
        return CompleteOrderHttp.Response.of(payment);
    }

    private Payment createPayment(KakaoPayApproveResponse response) {

        String orderNo = response.getPartnerOrderId();
        String paymentMethod = response.getPaymentMethodType();
        String tid = response.getTid();
        CardInfo cardInfo = response.getCardInfo();
        Amount amount = response.getAmount();
        LocalDateTime approvedAt = response.getApprovedAt();

        return Payment.create(orderNo, paymentMethod, tid, cardInfo, amount, approvedAt);
    }

    public KakaoPayApproveResponse approve(String orderNo, String pgToken) {
        Order order = orderQueryRepository.findByOrderNoWithMember(orderNo);
        return kakaoPayService.approve(createKakaoRequest(order, pgToken));
    }
    private KakaoPayApproveRequest createKakaoRequest(Order order, String pgToken) {
        return KakaoPayApproveRequest.builder()
                .pgToken(pgToken)
                .partnerUserId(order.getMember().getId().toString())
                .partnerOrderId(order.getOrderNo())
                .tid(order.getTid())
                .totalAmount(order.getTotalPrice())
                .cid(ONE_TIME_CID)
                .build();
    }
}
