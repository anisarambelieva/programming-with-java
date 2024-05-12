package org.store.services;
import org.store.enums.GoodsCategory;
import org.store.models.*;

import java.math.BigDecimal;
import java.util.Map;

public class StoreService {
    private Store store;

    public StoreService(Store store) {
        this.store = store;
    }

    public void addSoldGoods(Map<Goods, Double> sold) {
        Map<Goods, Double> tmpSoldGoods = this.store.getSoldGoods();

        for (Map.Entry<Goods, Double> entry : sold.entrySet()) {
            Goods goods = entry.getKey();
            Double quantity = entry.getValue();

            Double currentQuantity = tmpSoldGoods.getOrDefault(goods, 0.0);
            Double updatedQuantity = currentQuantity + quantity;

            tmpSoldGoods.put(goods, updatedQuantity);
        }

        this.store.setSoldGoods(tmpSoldGoods);
    }

    private BigDecimal calculatePriceWithMargin(Goods goods) {
        BigDecimal currentPrice = goods.getPrice();
        GoodsCategory category = goods.getGoodsCategory();

        double margin = this.store.getMarginPercentByCategory().get(category);

        BigDecimal increase = currentPrice.multiply(new BigDecimal(margin / 100));
        BigDecimal updatedPrice = currentPrice.add(increase);

        return updatedPrice.setScale(2, BigDecimal.ROUND_UP);
    }
    public void deliverGoods(Goods goods) {
        BigDecimal updatedPrice = calculatePriceWithMargin(goods);
        goods.setPrice(updatedPrice);

        this.store.addToDeliveredGoods(goods);
        this.store.addToInventory(goods);
    }

    public void removeFromInventory(Map<Goods, Double> sold) {
        Map<Goods, Double> tmpInventory = this.store.getInventory();

        for (Map.Entry<Goods, Double> entry : sold.entrySet()) {
            Goods goods = entry.getKey();
            Double quantity = entry.getValue();

            Double currentQuantity = tmpInventory.getOrDefault(goods, 0.0);
            Double updatedQuantity = currentQuantity - quantity;

            if (updatedQuantity == 0) {
                tmpInventory.remove(goods);
            } else {
                tmpInventory.put(goods, updatedQuantity);
            }
        }

        this.store.setInventory(tmpInventory);
    }

    public BigDecimal calculateGoodsDeliveryExpenses() {
        BigDecimal result = BigDecimal.ZERO;
        for (Map.Entry<Goods, Double> entry : this.store.getDeliveredGoods().entrySet()) {
            result = result.add(entry.getKey().getDeliveryPrice());
        }

        return result;
    }

    public BigDecimal calculateCashierSalaryExpenses() {
        BigDecimal salaries = BigDecimal.ZERO;

        for (Cashier cashier : this.store.getCashiers()) {
            salaries = salaries.add(cashier.getMonthlySalary());
        }

        return salaries;
    }

    public BigDecimal calculateGoodsSoldRevenue() {
        BigDecimal result = BigDecimal.ZERO;
        for (Map.Entry<Goods, Double> entry : this.store.getSoldGoods().entrySet()) {
            BigDecimal quantity = new BigDecimal(entry.getValue());
            result = result.add(entry.getKey().getPrice().multiply(quantity));
        }

        return result;
    }

    public BigDecimal calculateStoreProfit() {
        BigDecimal salaries = this.calculateCashierSalaryExpenses();
        BigDecimal deliveries = this.calculateGoodsDeliveryExpenses();
        BigDecimal revenue = this.calculateGoodsSoldRevenue();

        BigDecimal result = revenue.subtract(salaries.add(deliveries));

        return result;
    }
}
