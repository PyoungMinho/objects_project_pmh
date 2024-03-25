package com.objects.marketbridge.domains.coupon.service.port;

import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;

import java.util.List;

public interface MemberCouponRepository {
    MemberCoupon findById(Long id);

    MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId);

    MemberCoupon findByMemberIdAndCouponIdAndProductId(Long memberId, Long couponId, Long productId);

    MemberCoupon save(MemberCoupon memberCoupon);

    List<MemberCoupon> saveAll(List<MemberCoupon> memberCoupons);

    List<MemberCoupon> findAll();

    void deleteAllInBatch();


}
