package org.store.models;
import org.store.enums.GoodsCategory;

import java.util.*;

public class Store {
    private HashSet<Cashier> cashiers;
    private Map<Goods, Double> deliveredGoods;
    private Map<Goods, Double> inventory;
    private Map<Goods, Double> soldGoods;
    private HashSet<Receipt> issuedReceipts;
    private int countDaysForExpiryDateDiscount;
    private double expiryDateDiscount;
    private EnumMap<GoodsCategory, Double> marginPercentByCategory;

    public Store(int countDaysForExpiryDateDiscount, double expiryDateDiscount, EnumMap<GoodsCategory, Double> marginPercentByCategory) {
        this.cashiers = new HashSet<>();
        this.deliveredGoods = new HashMap<>();
        this.inventory = new HashMap<>();
        this.soldGoods = new HashMap<>();
        this.issuedReceipts = new HashSet<>();
        this.countDaysForExpiryDateDiscount = countDaysForExpiryDateDiscount;
        this.expiryDateDiscount = expiryDateDiscount;
        this.marginPercentByCategory = marginPercentByCategory;
    }

    public Map<Goods, Double> getDeliveredGoods() {
        return deliveredGoods;
    }

    public HashSet<Cashier> getCashiers() {
        return cashiers;
    }

    public Map<Goods, Double> getSoldGoods() {
        return soldGoods;
    }

    public Map<Goods, Double> getInventory() {
        return inventory;
    }

    public int getCountDaysForExpiryDateDiscount() {
        return countDaysForExpiryDateDiscount;
    }

    public double getExpiryDateDiscount() {
        return expiryDateDiscount;
    }

    public EnumMap<GoodsCategory, Double> getMarginPercentByCategory() {
        return marginPercentByCategory;
    }

    public void setSoldGoods(Map<Goods, Double> soldGoods) {
        this.soldGoods = soldGoods;
    }

    public void setCashiers(HashSet<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public void setInventory(Map<Goods, Double> inventory) {
        this.inventory = inventory;
    }

    public void setDeliveredGoods(Map<Goods, Double> deliveredGoods) {
        this.deliveredGoods = deliveredGoods;
    }

    public void addToInventory(Goods goods) {
        double currentQuantity = this.inventory.getOrDefault(goods, 0.0);
        this.inventory.put(goods, currentQuantity + 1);
    }

    public void addToDeliveredGoods(Goods goods) {
        double currentQuantity = this.deliveredGoods.getOrDefault(goods, 0.0);
        this.deliveredGoods.put(goods, currentQuantity + 1);
    }

    @Override
    public String toString() {
        return "Store{" +
                "cashiers=" + cashiers +
                ", deliveredGoods=" + deliveredGoods +
                ", inventory=" + inventory +
                ", soldGoods=" + soldGoods +
                ", issuedReceipts=" + issuedReceipts +
                ", countDaysForExpiryDateDiscount=" + countDaysForExpiryDateDiscount +
                ", expiryDateDiscount=" + expiryDateDiscount +
                ", marginPercentByCategory=" + marginPercentByCategory +
                '}';
    }
}
