package org.store.models;

public class Cart {
    private Goods goods;
    private double quantity;

    @Override
    public String toString() {
        return "Cart{" +
                "goods=" + goods +
                ", quantity=" + quantity +
                '}';
    }
}
