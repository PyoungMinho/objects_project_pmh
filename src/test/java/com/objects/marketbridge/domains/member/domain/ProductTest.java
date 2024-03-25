package com.objects.marketbridge.domains.member.domain;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("주어진 수량만큼 재고가 증가해야 한다.")
    public void increase() {
        // given
        Product product = Product.builder()
                .stock(10L)
                .build();
        Long quantity = 5L;

        // when
        product.increase(quantity);

        // then
        assertThat(product.getStock()).isEqualTo(15L);
    }

    @DisplayName("사용하지 않은 쿠폰리스트를 반환할 수 있다.")
    @Test
    void getAvailableCoupons(){

        //given
        MemberCoupon unUsedMemberCoupon1 = MemberCoupon.builder().isUsed(false).build();
        MemberCoupon unUsedMemberCoupon2 = MemberCoupon.builder().isUsed(false).build();
        MemberCoupon usedMemberCoupon = MemberCoupon.builder().isUsed(true).build();

        Coupon coupon1 = Coupon.builder().name("쿠폰1").build();
        Coupon coupon2 = Coupon.builder().name("쿠폰2").build();
        Coupon coupon3 = Coupon.builder().name("쿠폰3").build();

        coupon1.addMemberCoupon(unUsedMemberCoupon1);
        coupon2.addMemberCoupon(unUsedMemberCoupon2);
        coupon3.addMemberCoupon(usedMemberCoupon);

        Product product = Product.builder().productNo("productNo1").build();
        product.addCoupons(coupon1);
        product.addCoupons(coupon2);
        product.addCoupons(coupon3);

        //when
        List<Coupon> availableCoupons = product.getAvailableCoupons();

        //then
        Assertions.assertThat(availableCoupons).hasSize(2);
        Assertions.assertThat(availableCoupons)
                .extracting(c -> c.getName())
                .contains("쿠폰1", "쿠폰2");
    }
}