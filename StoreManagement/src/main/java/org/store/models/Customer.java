package org.store.models;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        LocalDate now = LocalDate.now();
        LocalDate expDate = goods.getExpirationDate();

        if (expDate.isAfter(now)) {
            double currentQuantity = this.cart.getOrDefault(goods, 0.0);
            this.cart.put(goods, currentQuantity+quantity);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cart=" + cart +
                ", money=" + money +
                '}';
    }
}
