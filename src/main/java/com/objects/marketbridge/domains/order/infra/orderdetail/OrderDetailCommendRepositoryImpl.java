package com.objects.marketbridge.domains.order.infra.orderdetail;

import com.objects.marketbridge.domains.order.domain.OrderDetail;
import com.objects.marketbridge.domains.order.service.port.OrderDetailCommendRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDetailCommendRepositoryImpl implements OrderDetailCommendRepository {

    private final OrderDetailJpaRepository orderDetailJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderDetailCommendRepositoryImpl(OrderDetailJpaRepository orderDetailJpaRepository, EntityManager em) {
        this.orderDetailJpaRepository = orderDetailJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public int changeAllType(Long orderId, String type) {
        return orderDetailJpaRepository.changeAllType(orderId, type);
    }

    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetail) {
        return orderDetailJpaRepository.saveAll(orderDetail);
    }

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailJpaRepository.save(orderDetail);
    }

    @Override
    public OrderDetail saveAndReturnEntity(OrderDetail orderDetail) {
        return orderDetailJpaRepository.save(orderDetail);
    }

    @Override
    public void deleteAllInBatch() {
        orderDetailJpaRepository.deleteAllInBatch();
    }

}
