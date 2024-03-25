package com.objects.marketbridge.domains.coupon.infra;

import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.coupon.service.port.MemberCouponRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCouponRepositoryImpl implements MemberCouponRepository {

    private final MemberCouponJpaRepository memberCouponJpaRepository;

    @Override
    public MemberCoupon findById(Long id) {
        return memberCouponJpaRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId) {
        return memberCouponJpaRepository.findByMemberIdAndCouponId(memberId, couponId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponIdAndProductId(Long memberId, Long couponId, Long productId) {
        return memberCouponJpaRepository.findByMemberIdAndCouponIdAndProductId(memberId, couponId, productId).orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException("멤버쿠폰이 존재하지 않습니다")));
    }

    @Override
    public void deleteAllInBatch() {
        memberCouponJpaRepository.deleteAllInBatch();
    }

    @Override
    public MemberCoupon save(MemberCoupon memberCoupon) {
        return memberCouponJpaRepository.save(memberCoupon);
    }

    @Override
    public List<MemberCoupon> saveAll(List<MemberCoupon> memberCoupons) {
        return memberCouponJpaRepository.saveAll(memberCoupons);
    }

    @Override
    public List<MemberCoupon> findAll() {
        return memberCouponJpaRepository.findAll();
    }
}
