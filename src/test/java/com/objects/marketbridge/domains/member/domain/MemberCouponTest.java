package com.objects.marketbridge.domains.member.domain;

import com.objects.marketbridge.common.utils.DateTimeHolder;
import com.objects.marketbridge.domains.coupon.domain.MemberCoupon;
import com.objects.marketbridge.domains.order.mock.TestDateTimeHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class MemberCouponTest {

    @Test
    @DisplayName("사용여부와 사용시간이 초기화 되어야 한다.")
    public void changeUsageInfo1() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 16, 6, 34);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .now(localDateTime)
                .build();
        MemberCoupon usedCoupon = createMemberCoupon(localDateTime, true);

        // when
        usedCoupon.changeUsageInfo(dateTimeHolder);
    
        // then
        Assertions.assertThat(usedCoupon).extracting("usedDate", "isUsed")
                .containsExactly(localDateTime, false);
    }

    @Test
    @DisplayName("dateTimeHolder가 null이라면 사용 날짜는 초기화 되어야 한다.")
    public void changeUsageInfo2() {
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 16, 6, 34);
        MemberCoupon usedCoupon = createMemberCoupon(localDateTime, true);

        // when
        usedCoupon.changeUsageInfo((DateTimeHolder) null);

        // then
        Assertions.assertThat(usedCoupon).extracting("usedDate", "isUsed")
                .containsExactly(null, false);
    }

    private static MemberCoupon createMemberCoupon(LocalDateTime localDateTime, boolean isUsed) {
        return MemberCoupon.builder()
                .usedDate(localDateTime)
                .isUsed(isUsed)
                .build();
    }

}