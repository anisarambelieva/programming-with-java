package org.store;

import java.math.BigDecimal;
import java.util.*;

public class Checkout {
    private Cashier cashier;
    private Collection<Goods> cart;

    public Checkout(Cashier cashier) {
        this.cashier = cashier;
        this.cart = new ArrayList<Goods>();
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public boolean addToCart(Goods goods) {
        Date now = new Date();

        if (goods.getExpirationDate().before(now)) {
            throw new IllegalArgumentException("Cannot add expired goods to cart!");
        }

        return this.cart.add(goods);
    }


    public Receipt sellGoods(BigDecimal customerMoney) {
        String serialNumber = UUID.randomUUID().toString();
        Date issueDate = new Date();
        Receipt receipt = new Receipt(serialNumber, this.cashier, issueDate, this.cart);

        BigDecimal priceToPay = receipt.calculateTotalPrice();
        if (customerMoney.compareTo(priceToPay) < 0) {
            throw new IllegalArgumentException("Customer does not have enough money to pay!");
        }

        return receipt;
    }
}
