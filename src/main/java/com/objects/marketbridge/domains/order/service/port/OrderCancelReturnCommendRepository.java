package com.objects.marketbridge.domains.order.service.port;

import com.objects.marketbridge.domains.order.domain.OrderCancelReturn;

public interface OrderCancelReturnCommendRepository {
    OrderCancelReturn save(OrderCancelReturn orderCancelReturn);
}
