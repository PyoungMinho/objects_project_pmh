package com.objects.marketbridge.domains.cart.infra;

import com.objects.marketbridge.domains.cart.domain.Cart;
import com.objects.marketbridge.domains.cart.service.port.CartQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.objects.marketbridge.domains.cart.domain.QCart.cart;
import static com.objects.marketbridge.domains.member.domain.QMember.member;
import static com.objects.marketbridge.domains.product.domain.QProduct.product;


@Repository
@Slf4j
@Transactional(readOnly = true)
public class CartQueryRepositoryImpl implements CartQueryRepository {

    private final CartJpaRepository cartJpaRepository;
    private final JPAQueryFactory queryFactory;

    public CartQueryRepositoryImpl(CartJpaRepository cartJpaRepository, EntityManager em) {
        this.cartJpaRepository = cartJpaRepository;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Cart> findAll() {
        return cartJpaRepository.findAll();
    }

    @Override
    public Optional<Cart> findByProductId(Long productId) {
        return cartJpaRepository.findByProductId(productId);
    }

    @Override
    public Cart findById(Long id) {
        return cartJpaRepository.findById(id).orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public Slice<Cart> findSlicedCart(Pageable pageable, Long memberId) {
        int pageSize = pageable.getPageSize();
        List<Cart> contents = queryFactory
                .selectFrom(cart)
                .join(cart.member, member).fetchJoin()
                .join(cart.product, product).fetchJoin()
                .where(eqMemberId(memberId))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .orderBy(cart.createdAt.desc())
                .fetch();

        boolean hasNext = false;
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            hasNext = true;
        }

        // Slice 객체 반환
        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private BooleanExpression eqMemberId(Long memberId) {
        return cart.member.id.eq(memberId);
    }

    @Override
    public Long countByMemberId(Long memberId) {
        return cartJpaRepository.countByMemberId(memberId);
    }
}
