package com.objects.marketbridge.domains.product.service;

import com.objects.marketbridge.domains.category.domain.Category;
import com.objects.marketbridge.domains.category.service.CategoryService;
import com.objects.marketbridge.domains.category.service.port.CategoryRepository;
import com.objects.marketbridge.domains.image.domain.Image;
import com.objects.marketbridge.domains.order.mock.FakeProductRepository;
import com.objects.marketbridge.domains.product.domain.*;
import com.objects.marketbridge.domains.product.dto.CreateProductDto;
import com.objects.marketbridge.domains.product.dto.ProductDetailDto;
import com.objects.marketbridge.domains.product.mock.FakeCategoryRepository;
import com.objects.marketbridge.domains.product.mock.FakeProdOptionRepository;
import com.objects.marketbridge.domains.product.mock.FakeProdTagRepository;
import com.objects.marketbridge.domains.product.mock.FakeProductImageRepository;
import com.objects.marketbridge.domains.product.service.ProductService;
import com.objects.marketbridge.domains.product.service.port.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

class ProductServiceTest {


    FakeProdOptionRepository fakeProdOptionRepository = new FakeProdOptionRepository();
    FakeProductImageRepository fakeProductImageRepository = new FakeProductImageRepository();
    FakeProdTagRepository fakeProdTagRepository = new FakeProdTagRepository();
    FakeCategoryRepository fakeCategoryRepository = new FakeCategoryRepository();
    FakeProductRepository fakeProductRepository = new FakeProductRepository();

    ProductService productService = ProductService.builder()
            .productRepository(fakeProductRepository)
            .categoryRepository(fakeCategoryRepository)
            .productImageRepository(fakeProductImageRepository)
            .prodOptionRepository(fakeProdOptionRepository)
            .prodTagRepository(fakeProdTagRepository)
            .productCustomRepository(fakeProductRepository)
            .prodOptionCustomRepository(fakeProdOptionRepository)
            .productImageCustomRepository(fakeProductImageRepository)
            .build();


    @Test
    void 상품_엔티티를_만들_수_있다(){
        Category category = Category.builder()
                .id(1L)
                .parentId(null)
                .name("의류")
                .level(1L)
                .build();
        fakeCategoryRepository.save(category);

        CreateProductDto createProductDto = CreateProductDto.builder()
                .discountRate(14L)
                .isOwn(true)
                .isSubs(true)
                .productNo("dfdfaf")
                .price(3400L)
                .stock(200L)
                .thumbImg("Fdfdf")
                .name("반팔티")
                .categoryId(category.getId())
                .build();

        Product product = productService.createProduct(createProductDto);
        Assertions.assertThat(product.getCategory().getLevel()).isEqualTo(1L);
    }
}