package com.objects.marketbridge.domains.order.mock;

import com.objects.marketbridge.domains.coupon.domain.Coupon;
import com.objects.marketbridge.domains.coupon.service.port.CouponRepository;

import java.util.List;

public class FakeCouponRepository implements CouponRepository {
    @Override
    public Coupon findById(Long id) {
        return null;
    }

    @Override
    public Coupon save(Coupon coupon) {
        return null;
    }

    @Override
    public void saveAll(List<Coupon> coupons) {

    }

    @Override
    public Coupon saveAndFlush(Coupon coupon) {
        return null;
    }

    @Override
    public List<Coupon> findAll() {
        return null;
    }

    @Override
    public void deleteAllInBatch() {

    }
}
