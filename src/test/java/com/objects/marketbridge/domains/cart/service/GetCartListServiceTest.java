package com.objects.marketbridge.domains.cart.service;


import com.objects.marketbridge.domains.cart.domain.Cart;
import com.objects.marketbridge.domains.cart.service.GetCartListService;
import com.objects.marketbridge.domains.cart.service.dto.GetCartDto;
import com.objects.marketbridge.domains.cart.service.port.CartCommendRepository;
import com.objects.marketbridge.domains.cart.service.port.CartDtoRepository;
import com.objects.marketbridge.domains.cart.service.port.CartQueryRepository;
import com.objects.marketbridge.common.responseobj.SliceResponse;
import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.coupon.service.port.MemberCouponRepository;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;
import com.objects.marketbridge.domains.product.domain.Option;
import com.objects.marketbridge.domains.product.domain.ProdOption;
import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import com.objects.marketbridge.domains.product.service.port.OptionRepository;
import com.objects.marketbridge.domains.product.service.port.ProdOptionRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@Transactional
class GetCartListServiceTest {

    @Autowired CartQueryRepository cartQueryRepository;
    @Autowired CartDtoRepository cartDtoRepository;
    @Autowired CartCommendRepository cartCommendRepository;
    @Autowired ProductRepository productRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired GetCartListService getCartListService;
    @Autowired OptionRepository optionRepository;
    @Autowired ProdOptionRepository prodOptionRepository;
    @Autowired CouponRepository couponRepository;
    @Autowired MemberCouponRepository memberCouponRepository;
    @Autowired EntityManager em;

    @BeforeEach
    void init() {
        Member member = Member.builder().email("test@email.com").build();
        memberRepository.save(member);

        MemberCoupon memberCoupon1_1 = MemberCoupon.builder().member(member).isUsed(false).build();
        MemberCoupon memberCoupon1_2 = MemberCoupon.builder().member(member).isUsed(false).build();

        MemberCoupon memberCoupon2_1 = MemberCoupon.builder().member(member).isUsed(false).build();
        MemberCoupon memberCoupon2_2 = MemberCoupon.builder().member(member).isUsed(false).build();

        MemberCoupon memberCoupon3_1 = MemberCoupon.builder().member(member).isUsed(false).build();
        MemberCoupon memberCoupon3_2 = MemberCoupon.builder().member(member).isUsed(false).build();

        MemberCoupon memberCoupon4_1 = MemberCoupon.builder().member(member).isUsed(false).build();
        MemberCoupon memberCoupon4_2 = MemberCoupon.builder().member(member).isUsed(false).build();

        MemberCoupon memberCoupon5_1 = MemberCoupon.builder().member(member).isUsed(false).build();
        MemberCoupon memberCoupon5_2 = MemberCoupon.builder().member(member).isUsed(true).build(); // 사용한 쿠폰 -> 5_2 쿠폰

        Coupon coupon1_1 = Coupon.builder().name("[상품1]1000원 할인").price(1000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();
        Coupon coupon1_2 = Coupon.builder().name("[상품1]5000원 할인").price(5000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();

        Coupon coupon2_1 = Coupon.builder().name("[상품2]1000원 할인").price(1000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();
        Coupon coupon2_2 = Coupon.builder().name("[상품2]5000원 할인").price(5000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();

        Coupon coupon3_1 = Coupon.builder().name("[상품3]1000원 할인").price(1000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();
        Coupon coupon3_2 = Coupon.builder().name("[상품3]5000원 할인").price(5000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();

        Coupon coupon4_1 = Coupon.builder().name("[상품4]1000원 할인").price(1000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();
        Coupon coupon4_2 = Coupon.builder().name("[상품4]5000원 할인").price(5000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();

        Coupon coupon5_1 = Coupon.builder().name("[상품5]1000원 할인").price(1000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();
        Coupon coupon5_2 = Coupon.builder().name("[상품5]5000원 할인").price(5000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();

        // 쿠폰6_1, 6_2 들은 회원이 가지고 있지 않음.
        Coupon coupon6_1 = Coupon.builder().name("[상품6]1000원 할인").price(1000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();
        Coupon coupon6_2 = Coupon.builder().name("[상품6]5000원 할인").price(5000L).endDate(LocalDateTime.of(2024,1,1,0,0,0)).build();

        coupon1_1.addMemberCoupon(memberCoupon1_1);
        coupon1_2.addMemberCoupon(memberCoupon1_2);

        coupon2_1.addMemberCoupon(memberCoupon2_1);
        coupon2_2.addMemberCoupon(memberCoupon2_2);

        coupon3_1.addMemberCoupon(memberCoupon3_1);
        coupon3_2.addMemberCoupon(memberCoupon3_2);

        coupon4_1.addMemberCoupon(memberCoupon4_1);
        coupon4_2.addMemberCoupon(memberCoupon4_2);

        coupon5_1.addMemberCoupon(memberCoupon5_1);
        coupon5_2.addMemberCoupon(memberCoupon5_2);

        Option option1 = Option.builder().name("옵션1").build();
        Option option2 = Option.builder().name("옵션2").build();



        ProdOption prodOption1_1 = ProdOption.builder().build();
        ProdOption prodOption1_2 = ProdOption.builder().build();
        ProdOption prodOption2_1 = ProdOption.builder().build();
        ProdOption prodOption2_2 = ProdOption.builder().build();
        ProdOption prodOption3_1 = ProdOption.builder().build();
        ProdOption prodOption3_2 = ProdOption.builder().build();
        ProdOption prodOption4_1 = ProdOption.builder().build();
        ProdOption prodOption4_2 = ProdOption.builder().build();
        ProdOption prodOption5_1 = ProdOption.builder().build();
        ProdOption prodOption5_2 = ProdOption.builder().build();
        ProdOption prodOption6_1 = ProdOption.builder().build();
        ProdOption prodOption6_2 = ProdOption.builder().build();

        option1.addProdOptions(prodOption1_1);
        option1.addProdOptions(prodOption2_1);
        option1.addProdOptions(prodOption3_1);
        option1.addProdOptions(prodOption4_1);
        option1.addProdOptions(prodOption5_1);
        option1.addProdOptions(prodOption6_1);
        option2.addProdOptions(prodOption1_2);
        option2.addProdOptions(prodOption2_2);
        option2.addProdOptions(prodOption3_2);
        option2.addProdOptions(prodOption4_2);
        option2.addProdOptions(prodOption5_2);
        option2.addProdOptions(prodOption6_2);

        optionRepository.saveAll(List.of(option1, option2));

        Product product1 = Product.builder().stock(5L).productNo("productNo1").build();
        Product product2 = Product.builder().stock(5L).productNo("productNo2").build();
        Product product3 = Product.builder().stock(5L).productNo("productNo3").build();
        Product product4 = Product.builder().stock(5L).productNo("productNo4").build();
        Product product5 = Product.builder().stock(5L).productNo("productNo5").build();
        Product product6 = Product.builder().stock(5L).productNo("productNo6").build();

        product1.addProdOptions(prodOption1_1);
        product1.addProdOptions(prodOption1_2);
        product1.addCoupons(coupon1_1);
        product1.addCoupons(coupon1_2);

        product2.addProdOptions(prodOption2_1);
        product2.addProdOptions(prodOption2_2);
        product2.addCoupons(coupon2_1);
        product2.addCoupons(coupon2_2);

        product3.addProdOptions(prodOption3_1);
        product3.addProdOptions(prodOption3_2);
        product3.addCoupons(coupon3_1);
        product3.addCoupons(coupon3_2);

        product4.addProdOptions(prodOption4_1);
        product4.addProdOptions(prodOption4_2);
        product4.addCoupons(coupon4_1);
        product4.addCoupons(coupon4_2);

        product5.addProdOptions(prodOption5_1);
        product5.addProdOptions(prodOption5_2);
        product5.addCoupons(coupon5_1);
        product5.addCoupons(coupon5_2);

        product6.addProdOptions(prodOption6_1);
        product6.addProdOptions(prodOption6_2);
        product6.addCoupons(coupon6_1);
        product6.addCoupons(coupon6_2);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);

        Cart cart1 = Cart.create(member, product1, false, 1L);
        Cart cart2 = Cart.create(member, product2, false, 1L);
        Cart cart3 = Cart.create(member, product3, false, 1L);
        Cart cart4 = Cart.create(member, product4, false, 1L);
        Cart cart5 = Cart.create(member, product5, false, 1L);
        Cart cart6 = Cart.create(member, product6, false, 1L);


        cartCommendRepository.saveAndFlush(cart1);
        cartCommendRepository.saveAndFlush(cart2);
        cartCommendRepository.saveAndFlush(cart3);
        cartCommendRepository.saveAndFlush(cart4);
        cartCommendRepository.saveAndFlush(cart5);
        cartCommendRepository.saveAndFlush(cart6);

    }

    @AfterEach
    void clear() {
        cartCommendRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        prodOptionRepository.deleteAllInBatch();
        optionRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
    }


    @DisplayName("SliceResponse 로 변환된다")
    @Test
    void get_SliceResponse(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        int pageNumber = 0;
        int pageSize = 2;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //when
        SliceResponse<GetCartDto> sliceResponse = getCartListService.get(pageRequest, member.getId());

        //then
        assertThat(sliceResponse).isInstanceOf(SliceResponse.class);
        assertThat(sliceResponse.getSize()).isEqualTo(pageSize);
        assertThat(sliceResponse.getSort().getDirection()).isEqualTo(Sort.Direction.DESC.toString());
        assertThat(sliceResponse.getSort().getOrderProperty()).isEqualTo("createdAt");
        assertThat(sliceResponse.getCurrentPage()).isEqualTo(0);
        assertThat(sliceResponse.isFirst()).isTrue();
        assertThat(sliceResponse.isLast()).isFalse();
        assertThat(sliceResponse.getContent()).hasSize(pageSize);
    }

    @DisplayName("장바구니를 조회할 수 있다_쿠폰제외")
    @Test
    void get_withoutCoupon(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        int pageNumber = 0;
        int pageSize = 2;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //when
        SliceResponse<GetCartDto> sliceResponse = getCartListService.get(pageRequest, member.getId());

        //then
        // response.getCartItems().getContent()의 각 요소가 GetCartDto 클래스의 인스턴스인지 확인
        assertThat(sliceResponse.getContent())
                .allSatisfy(cartItem -> assertThat(cartItem).isInstanceOf(GetCartDto.class));

        // 각각의 CartInfo 객체가 2개의 옵션을 가지고 있으며 각 옵션 이름은 '옵션1', '옵션2' 여야 한다.
        assertThat(sliceResponse.getContent())
                .extracting(GetCartDto::getOptionNames)
                .allMatch(optionNames -> optionNames.size() == 2)
                .allSatisfy(optionNames -> {
                    assertThat(optionNames).containsExactly("옵션1", "옵션2");
                });
    }

    //TODO 해결해야할 TEST By 정민우님
    @DisplayName("회원이 쿠폰을 가지고 있는경우 쿠폰 정보도 같이 조회된다")
    @Test
    void get_withCoupon1(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        int pageNumber = 1;
        int pageSize = 2;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //when
        SliceResponse<GetCartDto> sliceResponse = getCartListService.get(pageRequest, member.getId());

        //then
        assertThat(sliceResponse.getContent())
                .extracting(content -> content.getAvailableCoupons())
                .allMatch(coupons -> coupons.size() == 2)
                .allSatisfy(coupons ->{
                    assertThat(coupons)
                            .extracting(c -> c.getPrice())
                            .containsExactly(1000L, 5000L);

                    assertThat(coupons)
                            .extracting(c -> c.getName())
                            .allMatch(name -> name.matches("\\[상품\\d+]1000원 할인") || name.matches("\\[상품\\d+]5000원 할인"));
                        }
                );
    }

    @DisplayName("회원이 소유하고 있는 쿠폰만 조회가 되어야한다")
    @Test
    void get_withCoupon2(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        int pageNumber = 0;
        int pageSize = 1;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        //when
        SliceResponse<GetCartDto> sliceResponse = getCartListService.get(pageRequest, member.getId());

        //then
        assertThat(sliceResponse.getContent().get(0).getProductNo()).isEqualTo("productNo6");
        assertThat(sliceResponse.getContent().get(0).getAvailableCoupons()).isNull();
        assertThatThrownBy(() ->
                sliceResponse.getContent().get(0).getAvailableCoupons().get(0))
                .isInstanceOf(NullPointerException.class);

    }
    
@DisplayName("사용하지 않은 쿠폰만 조회가 되어야한다")
@Test
void get_withCoupon3(){
    //given
    Member member = memberRepository.findByEmail("test@email.com");
    int pageNumber = 0;
    int pageSize = 2;
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
    Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

    //when
    SliceResponse<GetCartDto> sliceResponse = getCartListService.get(pageRequest, member.getId());

    //then
    assertThat(sliceResponse.getContent().get(0).getProductNo()).isEqualTo("productNo6");
    assertThat(sliceResponse.getContent().get(1).getProductNo()).isEqualTo("productNo5");
    assertThat(sliceResponse.getContent().get(1).getAvailableCoupons()).hasSize(1);
    assertThat(sliceResponse.getContent().get(1).getAvailableCoupons())
            .extracting(c -> c.getName())
            .contains("[상품5]1000원 할인");

}
    @DisplayName("회원이 장바구니를 조회할 수 있다")
    @Test
    void countAll(){
        //given
        Long memberId = memberRepository.findByEmail("test@email.com").getId();

        //when
        Long countResult = getCartListService.countAll(memberId);

        //then
        assertThat(countResult).isEqualTo(6);
    }
}