package com.objects.marketbridge.payment.service.dto;

import com.objects.marketbridge.common.dto.KaKaoCancelResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RefundDto {

    private Long totalRefundAmount;
    private String refundMethod;
    private LocalDateTime refundProcessedAt; // 환불 일자

    @Builder
    public RefundDto(Long totalRefundAmount, String refundMethod, LocalDateTime refundProcessedAt) {
        this.totalRefundAmount = totalRefundAmount;
        this.refundMethod = refundMethod;
        this.refundProcessedAt = refundProcessedAt;
    }

    public static RefundDto of(KaKaoCancelResponse kaKaoCancelResponse) {
        return RefundDto.builder()
                .refundMethod(kaKaoCancelResponse.getPayment_method_type())
                .refundMethod(kaKaoCancelResponse.getCanceled_at())
                .totalRefundAmount((long) kaKaoCancelResponse.getCanceled_amount().getTotal())
                .build();
    }
}
