package org.store.models;

import org.store.enums.GoodsCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Goods {
    private String id;
    private String name;
    private BigDecimal deliveryPrice;
    private BigDecimal price;
    private LocalDate expirationDate;
    private GoodsCategory goodsCategory;

    public Goods(String id, String name, BigDecimal deliveryPrice, BigDecimal price,
                 LocalDate expirationDate, GoodsCategory goodsCategory) {
        this.id = id;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.price = price;
        this.expirationDate = expirationDate;
        this.goodsCategory = goodsCategory;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public GoodsCategory getGoodsCategory() {
        return goodsCategory;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
