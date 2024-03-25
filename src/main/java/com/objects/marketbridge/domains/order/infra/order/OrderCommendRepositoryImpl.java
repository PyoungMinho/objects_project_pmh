package com.objects.marketbridge.domains.order.infra.order;

import com.objects.marketbridge.domains.order.domain.Order;
import com.objects.marketbridge.domains.order.service.port.OrderCommendRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderCommendRepositoryImpl implements OrderCommendRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderCommendRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager em) {
        this.orderJpaRepository = orderJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public void deleteAllInBatch() {
        orderJpaRepository.deleteAllInBatch();
    }

    @Override
    public void saveAll(List<Order> orders) {
        orderJpaRepository.saveAll(orders);
    }

    @Override
    public void deleteByOrderNo(String orderNo) {
        orderJpaRepository.deleteByOrderNo(orderNo);
    }

}
