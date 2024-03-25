package com.objects.marketbridge.domains.product.infra.product;

import com.objects.marketbridge.domains.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Optional<Product> findByProductNo(String productNo);
    List<Product> findByName(String name);

}
