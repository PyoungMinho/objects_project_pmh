package com.objects.marketbridge.domains.cart.infra;

import com.objects.marketbridge.domains.cart.domain.Cart;
import com.objects.marketbridge.domains.cart.service.port.CartCommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CartCommendRepositoryImpl implements CartCommendRepository {

    private final CartJpaRepository cartJpaRepository;

    @Override
    public Cart save(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    @Override
    public void saveAll(List<Cart> carts) {
        cartJpaRepository.saveAll(carts);
    }

    @Override
    public void deleteAllInBatch() {
        cartJpaRepository.deleteAllInBatch();
    }

    @Override
    public void deleteAllByIdInBatch(List<Long> ids) {
        cartJpaRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void saveAndFlush(Cart cart) {
        cartJpaRepository.saveAndFlush(cart);
    }
}
