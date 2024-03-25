package com.objects.marketbridge.domains.coupon.domain;

import com.objects.marketbridge.common.utils.DateTimeHolder;
import com.objects.marketbridge.domains.member.domain.BaseEntity;
import com.objects.marketbridge.domains.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCoupon> memberCoupons = new ArrayList<>();

    private Long count;

    private Long minimumPrice;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder
    public Coupon(Product product, String name, Long price, Long count, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
        this.product = product;
        this.name = name;
        this.price = price;
        this.count = count;
        this.minimumPrice = minimumPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Coupon create(Product product, String name, Long price, Long count, Long minimumPrice, LocalDateTime startDate, LocalDateTime endDate) {
        return Coupon.builder()
                .product(product)
                .name(name)
                .price(price)
                .count(count)
                .minimumPrice(minimumPrice)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public void addMemberCoupon(MemberCoupon memberCoupon) {
        memberCoupons.add(memberCoupon);
        memberCoupon.setCoupon(this);
    }

    public void addProduct(Product product) {
        this.product = product;
    }

    public void changeMemberCouponInfo(DateTimeHolder dateTimeHolder, Long memberId) {
        memberCoupons.stream()
                .filter(m -> m.getMember().getId().equals(memberId))
                        .forEach(m -> m.changeUsageInfo(dateTimeHolder));
    }
}
