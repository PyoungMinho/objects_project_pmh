package com.objects.marketbridge.domains.order.mock;

import com.objects.marketbridge.domains.order.domain.OrderDetail;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BaseFakeOrderDetailRepository {

    @Getter
    private static final BaseFakeOrderDetailRepository instance = new BaseFakeOrderDetailRepository();

    private Long autoGeneratedId = 0L;
    private List<OrderDetail> data = new ArrayList<>();

    protected Long increaseId() {
        return ++autoGeneratedId;
    }

    public void clear() {
        autoGeneratedId = 0L;
        data.clear();
    }
}