package org.store.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.store.enums.GoodsCategory;
import org.store.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StoreServiceTest {
    private Store store;
    private StoreService storeService;

    @BeforeEach
    void BeforeEach() {
        EnumMap<GoodsCategory, Double> margins = new EnumMap<>(GoodsCategory.class);
        margins.put(GoodsCategory.FOOD, 10.00);
        margins.put(GoodsCategory.NONFOOD, 15.00);

        this.store = new Store(10, 5.00, margins);
        this.storeService = new StoreService(this.store);
    }

    @Test
    void AddSoldGoods_SuccessfullyAddsGoods() {
        // Arrange
        Goods goods1 = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Goods goods2 = new Goods("2", "carrots", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> sold = new HashMap<>();
        double expectedQuantity1 = 3.40;
        double expectedQuantity2 = 1.50;
        sold.put(goods1, expectedQuantity1);
        sold.put(goods2, expectedQuantity2);

        // Act
        this.storeService.addSoldGoods(sold);

        // Assert
        Map<Goods, Double> actualSoldGoods = this.store.getSoldGoods();
        assertTrue(actualSoldGoods.containsKey(goods1));
        assertTrue(actualSoldGoods.containsKey(goods2));
        double actualQuantity1 = actualSoldGoods.get(goods1);
        double actualQuantity2 = actualSoldGoods.get(goods2);

        assertEquals(expectedQuantity1, actualQuantity1);
        assertEquals(expectedQuantity2, actualQuantity2);
    }

    @Test
    void DeliverGoods_SuccessfullyAddsToDeliveredAndInventory() {
        // Arrange
        Goods goods = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        double margin = this.store.getMarginPercentByCategory().get(goods.getGoodsCategory());
        BigDecimal increase = goods.getPrice().multiply(new BigDecimal(margin/100));
        BigDecimal expectedPrice = goods.getPrice().add(increase).setScale(2, BigDecimal.ROUND_UP);;

        // Act
        this.storeService.deliverGoods(goods);

        // Assert
        assertTrue(this.store.getDeliveredGoods().containsKey(goods));
        assertTrue(this.store.getInventory().containsKey(goods));

        BigDecimal actualPrice = goods.getPrice();
        assertEquals(expectedPrice, actualPrice);
    }

    @Test
    void RemoveFromInventory_SuccessfullyRemovesGoods() {
        // Arrange
        Goods goods = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> inventory = new HashMap<>();
        inventory.put(goods, 5.00);
        this.store.setInventory(inventory);

        Map<Goods, Double> sold = new HashMap<>();
        sold.put(goods, 2.00);

        double expectedQuantity = 3.00;

        // Act
        this.storeService.removeFromInventory(sold);

        // Assert
        assertTrue(this.store.getInventory().containsKey(goods));

        double actualQuantity = this.store.getInventory().get(goods);
        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    void RemoveFromInventory_SuccessfullyRemovesAllGoods() {
        // Arrange
        Goods goods = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> inventory = new HashMap<>();
        inventory.put(goods, 5.00);
        this.store.setInventory(inventory);

        Map<Goods, Double> sold = new HashMap<>();
        sold.put(goods, 5.00);

        // Act
        this.storeService.removeFromInventory(sold);

        // Assert
        assertFalse(this.store.getInventory().containsKey(goods));
    }

    @Test
    void CalculateGoodsDeliveryExpenses_ReturnsCorrectResult() {
        // Arrange
        Goods goods1 = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Goods goods2 = new Goods("2", "carrots", new BigDecimal(10), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> delivered = new HashMap<>();

        double goodsQuantity1 = 1.00;
        double goodsQuantity2 = 2.00;
        delivered.put(goods1, goodsQuantity1);
        delivered.put(goods2, goodsQuantity2);
        this.store.setDeliveredGoods(delivered);

        BigDecimal goodsDeliveredPrice1 = goods1.getDeliveryPrice().multiply(new BigDecimal(goodsQuantity1));
        BigDecimal goodsDeliveredPrice2 = goods1.getDeliveryPrice().multiply(new BigDecimal(goodsQuantity2));
        BigDecimal expectedResult = goodsDeliveredPrice1.add(goodsDeliveredPrice2);

        // Act
        BigDecimal actualResult = this.storeService.calculateGoodsDeliveryExpenses();

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void CalculateCashierSalaryExpenses_WhenNoCashiers_ReturnsZero() {
        // Arrange
        BigDecimal expectedResult = BigDecimal.ZERO;

        // Act
        BigDecimal actualResult = this.storeService.calculateCashierSalaryExpenses();

        // Assert
        assertEquals(expectedResult, actualResult);
    }
    @Test
    void CalculateCashierSalaryExpenses_ReturnsCorrectResult() {
        // Arrange
        Cashier cashier1 = new Cashier("Tom", new BigDecimal(1000));
        Cashier cashier2 = new Cashier("Ben", new BigDecimal(1500));
        HashSet<Cashier> cashiers = new HashSet<>();
        cashiers.add(cashier1);
        cashiers.add(cashier2);
        this.store.setCashiers(cashiers);

        BigDecimal expectedResult = new BigDecimal(2500);

        // Act
        BigDecimal actualResult = this.storeService.calculateCashierSalaryExpenses();

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void CalculateGoodsSoldRevenue_WhenNoGoodsSold_ReturnsZero() {
        // Arrange
        BigDecimal expectedResult = BigDecimal.ZERO;

        // Act
        BigDecimal actualResult = this.storeService.calculateGoodsSoldRevenue();

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void CalculateGoodsSoldRevenue_ReturnsCorrectResult() {
        // Arrange
        Goods goods1 = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Goods goods2 = new Goods("2", "carrots", new BigDecimal(10), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> sold = new HashMap<>();

        double goodsQuantity1 = 1.00;
        double goodsQuantity2 = 2.00;
        sold.put(goods1, goodsQuantity1);
        sold.put(goods2, goodsQuantity2);

        this.store.setSoldGoods(sold);

        BigDecimal expectedResult = new BigDecimal(30);

        // Act
        BigDecimal actualResult = this.storeService.calculateGoodsSoldRevenue();

        // Assert
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void CalculateStoreProfit_ReturnsCorrectResult() {
        // Arrange
        Cashier cashier = new Cashier("Tom", new BigDecimal(1000));
        HashSet<Cashier> cashiers = new HashSet<>();
        cashiers.add(cashier);
        this.store.setCashiers(cashiers);

        Goods goods = new Goods("1", "kindle", new BigDecimal(5), new BigDecimal(300), LocalDate.now(), GoodsCategory.NONFOOD);
        Map<Goods, Double> sold = new HashMap<>();

        double goodsQuantity = 10.00;
        sold.put(goods, goodsQuantity);
        this.store.setSoldGoods(sold);

        BigDecimal expectedResult = new BigDecimal(2000);

        // Act
        BigDecimal actualResult = this.storeService.calculateStoreProfit();

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}