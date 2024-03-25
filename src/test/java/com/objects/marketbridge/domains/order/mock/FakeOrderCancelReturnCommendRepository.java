package com.objects.marketbridge.domains.order.mock;

import com.objects.marketbridge.domains.order.domain.OrderCancelReturn;
import com.objects.marketbridge.domains.order.service.port.OrderCancelReturnCommendRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

public class FakeOrderCancelReturnCommendRepository extends BaseFakeOrderCancelReturnRepository implements OrderCancelReturnCommendRepository {

    @Override
    public OrderCancelReturn save(OrderCancelReturn orderCancelReturn) {
        if (orderCancelReturn.getId() == null || orderCancelReturn.getId() == 0) {
            ReflectionTestUtils.setField(orderCancelReturn, "id", increaseId(), Long.class);
        } else {
            getInstance().getData().removeIf(item -> Objects.equals(item.getId(), orderCancelReturn.getId()));
        }
        getInstance().getData().add(orderCancelReturn);
        return orderCancelReturn;
    }
}
