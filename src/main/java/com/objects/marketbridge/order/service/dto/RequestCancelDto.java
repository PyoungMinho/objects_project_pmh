package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.MembershipType;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

import static com.objects.marketbridge.order.domain.MemberShipPrice.BASIC;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;

public class RequestCancelDto {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private List<ProductInfoResponseDto> productInfoResponseDtos;
        private CancelRefundInfoResponseDto cancelRefundInfoResponseDto;

        @Builder
        private Response(List<ProductInfoResponseDto> productInfoResponseDtos, CancelRefundInfoResponseDto cancelRefundInfoResponseDto) {
            this.productInfoResponseDtos = productInfoResponseDtos;
            this.cancelRefundInfoResponseDto = cancelRefundInfoResponseDto;
        }

        public static Response of(List<OrderDetail> orderDetails, String memberShip) {
            return Response.builder()
                    .productInfoResponseDtos(
                            orderDetails.stream()
                            // TODO Product로 인해 N+1 문제 발생 예상 (of)
                            .map(RequestCancelDto.ProductInfoResponseDto::of)
                            .toList()
                    )
                    .cancelRefundInfoResponseDto(
                            // TODO coupon 으로 인해 N+1문제 발생할 것으로 예상 (of) -> fetchJoin으로 쿠폰까지 조인후 해결
                            CancelRefundInfoResponseDto.of(orderDetails, memberShip)
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfoResponseDto {
        private Long quantity;
        private String name;
        private Long price;
        private String image; // TODO 주문 취소 요청 이미지 반환

        @Builder
        private ProductInfoResponseDto(Long quantity, String name, Long price, String image) {
            this.quantity = quantity;
            this.name = name;
            this.price = price;
            this.image = image;
        }

        public static ProductInfoResponseDto of(OrderDetail orderDetail) {
            return ProductInfoResponseDto.builder()
                    .quantity(orderDetail.getQuantity())
                    .name(orderDetail.getProduct().getName())
                    .price(orderDetail.getProduct().getPrice())
                    .image(orderDetail.getProduct().getThumbImg())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CancelRefundInfoResponseDto {
        private Long deliveryFee;
        private Long refundFee;
        private Long discountPrice;
        private Long totalPrice;

        @Builder
        private CancelRefundInfoResponseDto(Long deliveryFee, Long refundFee, Long discountPrice, Long totalPrice) {
            this.deliveryFee = deliveryFee;
            this.refundFee = refundFee;
            this.discountPrice = discountPrice;
            this.totalPrice = totalPrice;
        }

        public static CancelRefundInfoResponseDto of(List<OrderDetail> orderDetails, String memberShip) {
            if (isBasicMember(memberShip)) {
                return createDto(orderDetails, BASIC.getDeliveryFee(), BASIC.getRefundFee());
            }
            return createDto(orderDetails, WOW.getDeliveryFee(), WOW.getRefundFee());
        }

        private static boolean isBasicMember(String memberShip) {
            return Objects.equals(memberShip, MembershipType.BASIC.getText());
        }

        private static CancelRefundInfoResponseDto createDto(List<OrderDetail> orderDetails, Long deliveryFee, Long refundFee) {
            return CancelRefundInfoResponseDto.builder()
                    .discountPrice( // TODO coupon 으로 인해 N+1문제 발생할 것으로 예상 -> fetchJoin으로 쿠폰까지 조인후 해결
                            orderDetails.stream()
                                    .mapToLong(orderDetail -> orderDetail.getCoupon().getPrice())
                                    .sum()
                    )
                    .totalPrice(
                            orderDetails.stream()
                                    .mapToLong(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                                    .sum()
                    )
                    .deliveryFee(deliveryFee)
                    .refundFee(refundFee)
                    .build();
        }
    }


}
