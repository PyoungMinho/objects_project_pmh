package com.objects.marketbridge.domains.product.infra;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.member.domain.Member;
import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.member.service.port.MemberRepository;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;
import com.objects.marketbridge.domains.coupon.service.port.MemberCouponRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberCouponRepositoryImplTest {

    @Autowired
    MemberCouponRepository memberCouponRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CouponRepository couponRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder().email("test@email.com").build();
        memberRepository.save(member);
        Coupon coupon = Coupon.builder().name("5000원짜리 쿠폰").build();
        MemberCoupon memberCoupon = MemberCoupon.builder().member(member).build();
        coupon.addMemberCoupon(memberCoupon);
        couponRepository.save(coupon);
    }

    @DisplayName("멤버아이디와 쿠폰아이디로 멤버쿠폰을 가져올수 있다.")
    @Test
    void findByMemberIdAndCouponId(){
        //given
        Member member = memberRepository.findByEmail("test@email.com");
        Coupon coupon = couponRepository.findAll().get(0);

        //when
        MemberCoupon memberCoupon = memberCouponRepository.findByMemberIdAndCouponId(member.getId(), coupon.getId());

        //then
        Assertions.assertThat(memberCoupon.getMember().getEmail()).isEqualTo("test@email.com");
        Assertions.assertThat(memberCoupon.getCoupon().getName()).isEqualTo("5000원짜리 쿠폰");
    }
}