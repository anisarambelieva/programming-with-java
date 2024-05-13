package org.store.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    private Map<Goods, Double> cart;
    private BigDecimal money;

    public Customer(BigDecimal money) {
        this.cart = new HashMap<>();
        this.money = money;
    }

    public Map<Goods, Double> getCart() {
        return cart;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void addToCart(Goods goods, double quantity) {
        double currentQuantity = this.cart.getOrDefault(goods, 0.0);
        this.cart.put(goods, currentQuantity+quantity);
    }
}
