package com.objects.marketbridge.domains.order.infra.ordercancelreturn;

import com.objects.marketbridge.domains.order.domain.OrderCancelReturn;
import com.objects.marketbridge.domains.order.service.port.OrderCancelReturnCommendRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCancelReturnCommendRepositoryImpl implements OrderCancelReturnCommendRepository {

    private final OrderCancelReturnJpaRepository orderCancelReturnJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderCancelReturnCommendRepositoryImpl(OrderCancelReturnJpaRepository orderCancelReturnJpaRepository, EntityManager em) {
        this.orderCancelReturnJpaRepository = orderCancelReturnJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public OrderCancelReturn save(OrderCancelReturn orderCancelReturn) {
        return orderCancelReturnJpaRepository.save(orderCancelReturn);
    }
}
