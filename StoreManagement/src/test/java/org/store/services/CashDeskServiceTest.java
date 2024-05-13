package org.store.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.store.enums.GoodsCategory;
import org.store.exceptions.NotEnoughGoodsAvailableException;
import org.store.exceptions.NotEnoughMoneyException;
import org.store.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CashDeskServiceTest {
    private CashDesk cashDesk;
    private CashDeskService cashDeskService;
    private Store store;
    private StoreService storeService;

    @BeforeEach
    void BeforeEach() {
        Cashier cashier = new Cashier("John", new BigDecimal(1000));

        EnumMap<GoodsCategory, Double> margins = new EnumMap<>(GoodsCategory.class);
        margins.put(GoodsCategory.FOOD, 10.00);
        margins.put(GoodsCategory.NONFOOD, 15.00);
        this.store = new Store(10, 5.00, margins);
        this.storeService = new StoreService(store);

        this.cashDesk = new CashDesk(cashier, this.store);
        this.cashDeskService = new CashDeskService(this.cashDesk, this.storeService);
    }

    @Test
    void Checkout_ReturnsReceiptSuccessfully() {
        // Arrange
        Goods goods = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> inventory = new HashMap<>();
        inventory.put(goods, 5.00);
        this.store.setInventory(inventory);

        Customer customer = new Customer(new BigDecimal(200));
        customer.addToCart(goods, 2.00);

        // Act
        Receipt receipt = this.cashDeskService.checkout(customer);

        // Assert
        assertEquals(customer.getCart(), receipt.getGoods());
    }

    @Test
    void Checkout_WhenMoneyNotEnough_ThrowsException() {
        // Arrange
        Goods goods = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> inventory = new HashMap<>();
        inventory.put(goods, 5.00);
        this.store.setInventory(inventory);

        Customer customer = new Customer(new BigDecimal(10));
        customer.addToCart(goods, 3.00);

        // Act and Assert
        assertThrows(NotEnoughMoneyException.class, () -> this.cashDeskService.checkout(customer));
    }

    @Test
    void Checkout_WhenGoodsNotAvailable_ThrowsException() {
        // Arrange
        Goods goods = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Map<Goods, Double> inventory = new HashMap<>();
        inventory.put(goods, 2.00);
        this.store.setInventory(inventory);

        Customer customer = new Customer(new BigDecimal(10));
        customer.addToCart(goods, 3.00);

        // Act and Assert
        assertThrows(NotEnoughGoodsAvailableException.class, () -> this.cashDeskService.checkout(customer));
    }
}