package com.objects.marketbridge.domain.order.service.port;

import com.objects.marketbridge.domain.model.Product;
import com.objects.marketbridge.domain.order.entity.OrderDetail;

import java.util.List;

public interface OrderDetailRepository  {

    int changeAllType(Long orderId, String type);

    List<OrderDetail> saveAll(List<OrderDetail> orderDetail);

    void addReason(Long orderId, String reason);

    void deleteAllInBatch();

    void save(OrderDetail orderDetail);

    OrderDetail findById(Long id);

    List<OrderDetail> findByProductId(Long id);

    List<OrderDetail> findAll();

    List<OrderDetail> findByOrderNo(String orderNo);

    List<OrderDetail> findByProdOrder_IdAndProductIn(Long orderId, List<Product> products);

//    ProdOrderDetail findByStockIdAndOrderId(Long stockId, Long orderId);
}
