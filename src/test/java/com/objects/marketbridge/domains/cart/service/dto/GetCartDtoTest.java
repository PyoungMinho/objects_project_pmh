package com.objects.marketbridge.domains.cart.service.dto;


import com.objects.marketbridge.domains.cart.domain.Cart;
import com.objects.marketbridge.domains.cart.service.dto.GetCartDto;
import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.product.domain.Option;
import com.objects.marketbridge.domains.product.domain.ProdOption;
import com.objects.marketbridge.domains.product.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@Slf4j
class GetCartDtoTest {


    @DisplayName("CouponDto 의 of 메서드 테스트 ")
    @Test
    void of_CouponDto() {

        // given
        Coupon coupon = Coupon.builder()
                .name("[상품1]1000원 할인")
                .price(1000L)
                .endDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                .minimumPrice(15000L)
                .build();

        // when
        GetCartDto.CouponDto couponDto = GetCartDto.CouponDto.of(coupon);

        //then
        assertThat(couponDto)
            .hasFieldOrPropertyWithValue("name", "[상품1]1000원 할인")
            .hasFieldOrPropertyWithValue("price", 1000L)
            .hasFieldOrPropertyWithValue("endDate", LocalDateTime.of(2024, 1, 1, 0, 0, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .hasFieldOrPropertyWithValue("minimumPrice", 15000L);
    }

    @DisplayName("GetCartDto 의 of 메서드 테스트 : 쿠폰이 존재하는 경우")
    @Test
    void of_GetCartDto_1() {

        // given
        Member member = Member.builder().name("홍길동").email("test@email.com").build();

        Product product1 = createProduct("productNo1", "썸네일1", "상품1", 2000L, 9999L, 0L, true, false);

        Option option1 = createOption("옵션1");
        Option option2 = createOption("옵션2");

        ProdOption prodOption1_1 = createProdOption(option1);
        ProdOption prodOption1_2 = createProdOption(option2);

        product1.addProdOptions(prodOption1_1);
        product1.addProdOptions(prodOption1_2);

        MemberCoupon memberCoupon1_1 = MemberCoupon.builder().isUsed(false).member(member).build();
        MemberCoupon memberCoupon1_2 = MemberCoupon.builder().isUsed(false).member(member).build();

        Coupon coupon1_1 = createCoupon("[상품1] 1000원", 1000L, 10000L, LocalDateTime.of(2024,1,1,0,0,0), 9999L, LocalDateTime.of(2023,1,1,0,0,0));
        Coupon coupon1_2 = createCoupon("[상품1] 5000원", 5000L, 20000L, LocalDateTime.of(2024,1,1,0,0,0), 9999L, LocalDateTime.of(2023,1,1,0,0,0));

        coupon1_1.addMemberCoupon(memberCoupon1_1);
        coupon1_2.addMemberCoupon(memberCoupon1_2);

        product1.addCoupons(coupon1_1);
        product1.addCoupons(coupon1_2);

        Cart cart1 = createCart(false, member, 1L, product1);


        // when
        GetCartDto cartDto1 = GetCartDto.of(cart1);

        //then
        assertThat(cartDto1)
                .hasFieldOrPropertyWithValue("productNo", "productNo1")
                .hasFieldOrPropertyWithValue("thumbImageUrl", "썸네일1")
                .hasFieldOrPropertyWithValue("productName", "상품1")
                .hasFieldOrPropertyWithValue("productPrice", 2000L)
                .hasFieldOrPropertyWithValue("quantity", 1L)
                .hasFieldOrPropertyWithValue("discountRate", 0L)
                .hasFieldOrPropertyWithValue("isOwn", true)
                .hasFieldOrPropertyWithValue("isSubs", false)
                .hasFieldOrPropertyWithValue("stock", 9999L)
                .hasFieldOrProperty("optionNames")
                .hasFieldOrProperty("availableCoupons");

        assertThat(cartDto1.getOptionNames())
                .hasSize(2)
                .containsExactlyElementsOf(List.of("옵션1", "옵션2"));

        assertThat(cartDto1.getAvailableCoupons())
                .hasSize(2)
                .extracting("name", "price", "endDate", "minimumPrice")
                .containsExactlyInAnyOrder(
                        tuple("[상품1] 1000원", 1000L, "2024-01-01 00:00:00", 10000L),
                        tuple("[상품1] 5000원", 5000L, "2024-01-01 00:00:00", 20000L)
                );
    }

    @DisplayName("GetCartDto 의 of 메서드 테스트 : 쿠폰이 없는 경우")
    @Test
    void of_GetCartDto_2() {

        // given
        Member member = Member.builder().name("홍길동").email("test@email.com").build();

        Product product2 = createProduct("productNo2", "썸네일1", "상품2", 2000L, 9999L, 0L, true, false);

        Option option1 = createOption("옵션1");
        Option option2 = createOption("옵션2");

        ProdOption prodOption2_1 = createProdOption(option1);
        ProdOption prodOption2_2 = createProdOption(option2);


        product2.addProdOptions(prodOption2_1);
        product2.addProdOptions(prodOption2_2);

        Cart cart2 = createCart(false, member, 1L, product2);

        // when
        GetCartDto cartDto2 = GetCartDto.of(cart2);

        //then
        assertThat(cartDto2.getAvailableCoupons()).isNull();
    }

    private static Option createOption(String name) {
        return Option.builder()
                .name(name)
                .build();
    }

    private Product createProduct(String productNo, String thumbImg, String name, Long price, Long stock, Long discountRate, Boolean isOwn, Boolean isSubs) {
        return Product.builder()
                .productNo(productNo)
                .thumbImg(thumbImg)
                .name(name)
                .price(price)
                .stock(stock)
                .discountRate(discountRate)
                .isOwn(isOwn)
                .isSubs(isSubs)
                .build();
    }

    private ProdOption createProdOption(Option option) {
        return ProdOption.builder()
                .option(option)
                .build();
    }

    private Coupon createCoupon(String name, Long price, Long minimumPrice, LocalDateTime endDate, Long count, LocalDateTime startDate) {
        return Coupon.builder()
                .name(name)
                .price(price)
                .minimumPrice(minimumPrice)
                .endDate(endDate)
                .count(count)
                .startDate(startDate)
                .build();

    }

    private Cart createCart(Boolean isSubs, Member member, Long quantity, Product product) {
        return Cart.builder()
                .isSubs(isSubs)
                .member(member)
                .quantity(quantity)
                .product(product)
                .build();
    }
}