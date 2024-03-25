package com.objects.marketbridge.domains.order.service.port;

import com.objects.marketbridge.domains.order.domain.OrderDetail;

import java.util.List;

public interface OrderDetailCommendRepository {

    void save(OrderDetail orderDetail);

    OrderDetail saveAndReturnEntity(OrderDetail orderDetail);

    List<OrderDetail> saveAll(List<OrderDetail> orderDetail);

    int changeAllType(Long orderId, String type);

    void deleteAllInBatch();

}
