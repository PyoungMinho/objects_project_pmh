package com.objects.marketbridge.domains.order.mock;

import com.objects.marketbridge.common.utils.DateTimeHolder;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;
import com.objects.marketbridge.domains.order.controller.OrderCancelController;
import com.objects.marketbridge.domains.order.controller.OrderCancelReturnController;
import com.objects.marketbridge.domains.order.controller.OrderReturnController;
import com.objects.marketbridge.domains.order.service.OrderCancelService;
import com.objects.marketbridge.domains.order.service.OrderReturnService;
import com.objects.marketbridge.domains.order.service.port.*;
import com.objects.marketbridge.domains.payment.service.port.PaymentClient;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.coupon.service.port.MemberCouponRepository;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import lombok.Builder;

public class TestContainer {

    public final OrderCancelReturnController orderCancelReturnController;
    public final OrderCancelController orderCancelController;
    public final OrderReturnController orderReturnController;

    public final OrderCancelService orderCancelService;
    public final OrderReturnService orderReturnService;

    public final OrderQueryRepository orderQueryRepository;
    public final OrderCommendRepository orderCommendRepository;
    public final OrderDetailQueryRepository orderDetailQueryRepository;
    public final OrderDetailCommendRepository orderDetailCommendRepository;
    public final OrderCancelReturnQueryRepository orderCancelReturnQueryRepository;
    public final OrderCancelReturnCommendRepository orderCancelReturnCommendRepository;
    public final OrderDtoRepository orderDtoRepository;
    public final OrderDetailDtoRepository orderDetailDtoRepository;
    public final ProductRepository productRepository;
    public final CouponRepository couponRepository;
    public final MemberCouponRepository memberCouponRepository;
    public final MemberRepository memberRepository;
    public final PaymentClient paymentClient;

    @Builder
    public TestContainer(DateTimeHolder dateTimeHolder) {
        // Repository
        this.orderQueryRepository = new FakeOrderQueryRepository();
        this.orderCommendRepository = new FakeOrderCommendRepository();
        this.orderDetailQueryRepository = new FakeOrderDetailQueryRepository();
        this.orderDetailCommendRepository = new FakeOrderDetailCommendRepository();
        this.orderCancelReturnQueryRepository = new FakeOrderCancelReturnQueryRepository();
        this.orderCancelReturnCommendRepository = new FakeOrderCancelReturnCommendRepository();
        this.orderDtoRepository = new FakeOrderDtoRepository();
        this.orderDetailDtoRepository = new FakeOrderDetailDtoRepository();
        this.productRepository = new FakeProductRepository();
        this.couponRepository = new FakeCouponRepository();
        this.memberCouponRepository = new FakeMemberCouponRepository();
        this.memberRepository = new FakeMemberRepository();
        this.paymentClient = new FakePaymentClient(dateTimeHolder);

        // Service
        this.orderCancelService = OrderCancelService.builder()
                .orderDetailQueryRepository(this.orderDetailQueryRepository)
                .orderDetailCommendRepository(this.orderDetailCommendRepository)
                .orderCancelReturnQueryRepository(this.orderCancelReturnQueryRepository)
                .orderCancelReturnCommendRepository(this.orderCancelReturnCommendRepository)
                .paymentClient(this.paymentClient)
                .dateTimeHolder(dateTimeHolder)
                .build();
        this.orderReturnService = OrderReturnService.builder()
                .orderDetailQueryRepository(this.orderDetailQueryRepository)
                .orderDetailCommendRepository(this.orderDetailCommendRepository)
                .orderCancelReturnQueryRepository(this.orderCancelReturnQueryRepository)
                .orderCancelReturnCommendRepository(this.orderCancelReturnCommendRepository)
                .paymentClient(this.paymentClient)
                .dateTimeHolder(dateTimeHolder)
                .build();

        // Controller
        this.orderCancelReturnController = OrderCancelReturnController.builder()
                .orderDetailDtoRepository(this.orderDetailDtoRepository)
                .build();
        this.orderCancelController = OrderCancelController.builder()
                .orderCancelService(this.orderCancelService)
                .memberRepository(this.memberRepository)
                .dateTimeHolder(dateTimeHolder)
                .build();
        this.orderReturnController = OrderReturnController.builder()
                .orderReturnService(this.orderReturnService)
                .memberRepository(this.memberRepository)
                .dateTimeHolder(dateTimeHolder)
                .build();

    }
}
