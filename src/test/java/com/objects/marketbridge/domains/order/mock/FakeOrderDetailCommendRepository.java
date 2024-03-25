package com.objects.marketbridge.domains.order.mock;

import com.objects.marketbridge.domains.order.domain.OrderDetail;
import com.objects.marketbridge.domains.order.service.port.OrderDetailCommendRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;

import static com.objects.marketbridge.domains.order.domain.StatusCodeType.ORDER_PARTIAL_CANCEL;

public class FakeOrderDetailCommendRepository extends BaseFakeOrderDetailRepository implements OrderDetailCommendRepository {

    @Override
    public void save(OrderDetail orderDetail) {
        if (orderDetail.getId() == null || orderDetail.getId() == 0) {
            ReflectionTestUtils.setField(orderDetail, "id", getInstance().increaseId(), Long.class);
            getInstance().getData().add(orderDetail);
        } else {
            getInstance().getData().removeIf(item -> Objects.equals(item.getId(), orderDetail.getId()));
            getInstance().getData().add(orderDetail);
        }
    }

    @Override
    public OrderDetail saveAndReturnEntity(OrderDetail orderDetail) {
        save(orderDetail);
        return OrderDetail.create(orderDetail, ORDER_PARTIAL_CANCEL.getCode());
    }

    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetail) {
        return null;
    }

    @Override
    public int changeAllType(Long orderId, String type) {
        return 0;
    }

    @Override
    public void deleteAllInBatch() {

    }
}
