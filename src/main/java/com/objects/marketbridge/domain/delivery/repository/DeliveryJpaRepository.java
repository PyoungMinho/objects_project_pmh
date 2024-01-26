package com.objects.marketbridge.domain.delivery.repository;

import com.objects.marketbridge.domain.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, Long> {
}