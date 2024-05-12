package org.store.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.store.enums.GoodsCategory;
import org.store.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptServiceTest {
    private Receipt receipt;
    private ReceiptService receiptService;

    @BeforeEach
    void BeforeEach() {
        Cashier cashier = new Cashier("John", new BigDecimal(1000));
        LocalDate issueDate = LocalDate.now();
        Map<Goods, Double> goods = new HashMap<>();

        this.receipt = new Receipt("0000", cashier, issueDate, goods);
        this.receiptService = new ReceiptService(this.receipt);
    }
    @Test
    void CalculateTotalPrice_EmptyGoodsList_ReturnsZero() {
        // Arrange
        BigDecimal expectedTotalPrice = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_UP);

        // Act
        BigDecimal actualTotalPrice = this.receiptService.calculateTotalPrice();

        // Assert
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }

    @Test
    void CalculateTotalPrice_MultipleGoodsInList_ReturnsCorrectPrice() {
        // Arrange
        Map<Goods, Double> goods = new HashMap<>();
        Goods goods1 = new Goods("1", "peas", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        Goods goods2 = new Goods("2", "carrots", new BigDecimal(5), new BigDecimal(10), LocalDate.now(), GoodsCategory.FOOD);
        goods.put(goods1, 3.40);
        goods.put(goods2, 1.50);

        this.receipt.setGoods(goods);
        BigDecimal expectedTotalPrice = new BigDecimal(49).setScale(2, BigDecimal.ROUND_UP);

        // Act
        BigDecimal actualTotalPrice = this.receiptService.calculateTotalPrice();

        // Assert
        assertEquals(expectedTotalPrice, actualTotalPrice);
    }
}