package com.objects.marketbridge.domains.order.mock;

import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.coupon.service.port.MemberCouponRepository;

import java.util.List;

public class FakeMemberCouponRepository implements MemberCouponRepository {
    @Override
    public MemberCoupon findById(Long id) {
        return null;
    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponId(Long MemberId, Long CouponId) {
        return null;
    }

    @Override
    public MemberCoupon save(MemberCoupon memberCoupon) {
        return null;
    }

    @Override
    public List<MemberCoupon> saveAll(List<MemberCoupon> memberCoupons) {
        return null;
    }

    @Override
    public List<MemberCoupon> findAll() {
        return null;
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponIdAndProductId(Long memberId, Long couponId, Long productId) {
        return null;
    }
}
