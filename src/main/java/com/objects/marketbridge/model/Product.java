package com.objects.marketbridge.model;

import com.objects.marketbridge.domain.order.entity.OrderDetail;
import com.objects.marketbridge.global.error.CustomLogicException;
import com.objects.marketbridge.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.objects.marketbridge.global.error.ErrorCode.*;
import static com.objects.marketbridge.global.error.ErrorCode.OUT_OF_STOCK;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

//    @OneToMany(mappedBy = "product")
//    private List<ProdOption> prodOptions = new ArrayList<>();

//    TODO 넣을지 말지
    private String productNo;

    private Boolean isOwn; // 로켓 true , 오픈 마켓 false

    private String name;

    private Long price;

    private Boolean isSubs;

    private Long stock;

    private String thumbImg;

    private Long discountRate;


    @Builder
    private Product(Category category, String productNo, Boolean isOwn, String name, Long price, Boolean isSubs, Long stock, String thumbImg, Long discountRate) {
        this.category = category;
        this.productNo = productNo;
        this.isOwn = isOwn; // 로켓 true , 오픈 마켓 false
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.stock = stock;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setProduct(this);
    }

    public void increase(Long quantity) {
        stock += quantity;
    }

    public void decrease(Long quantity) {
        verifyStockAvailable(quantity);
        stock -= quantity;
    }

    private void verifyStockAvailable(Long quantity) {
        if (stock - quantity < 0) {
            throw new CustomLogicException(OUT_OF_STOCK.getMessage());
        }
    }

    public void changeStock(Long stock) {
        this.stock = stock;
    }
}