package com.objects.marketbridge.domains.product.infra.product;

import com.objects.marketbridge.domains.product.domain.Product;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productJpaRepository.findById(id).orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public Product findByProductNo(String productNo) {
        return productJpaRepository.findByProductNo(productNo).orElseThrow(() -> new JpaObjectRetrievalFailureException(new EntityNotFoundException()));
    }

    @Override
    public List<Product> findAllById(List<Long> ids) {
        return productJpaRepository.findAllById(ids);
    }

    @Override
    public List<Product> findByName(String name) {
        return productJpaRepository.findByName(name);
    }

    @Override
    public void deleteAllInBatch() {
        productJpaRepository.deleteAllInBatch();
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Product saveAndFlush(Product product) {
        return productJpaRepository.saveAndFlush(product);
    }

    @Override
    public void saveAll(List<Product> products) {
        productJpaRepository.saveAll(products);
    }

    @Override
    public void delete(Product product) {
        productJpaRepository.delete(product);
    }

    @Override
    public Page<Product> findAllByCategoryId(Pageable pageable,Long categoryId) {

        String query = "WITH RECURSIVE CTE AS ( " +
                "    SELECT " +
                "category_id, " +
                "parent_id " +
                "FROM category " +
                "WHERE category_id = ?1 " +
                "UNION ALL " +
                "SELECT " +
                "c.category_id, " +
                "c.parent_id " +
                "FROM category c " +
                "INNER JOIN CTE b ON c.parent_id = b.category_id " +
                ") " +
                "select * " +
                "from product p " +
                "where p.category_id in (select category_id from CTE)";

        List<Product> products = entityManager.createNativeQuery(query, Product.class)
                .setParameter(1,categoryId).getResultList();

        return PageableExecutionUtils.getPage(products,pageable, products::size);
    }

}
