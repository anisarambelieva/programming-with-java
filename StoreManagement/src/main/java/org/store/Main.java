package org.store;

import org.store.enums.GoodsCategory;
import org.store.exceptions.NotEnoughGoodsAvailableException;
import org.store.models.*;
import org.store.services.CashDeskService;
import org.store.services.ReceiptService;
import org.store.services.StoreService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Cashier cashier1 = new Cashier("Ben", new BigDecimal(2000));

        EnumMap<GoodsCategory, Double> marginByCategory = new EnumMap<>(GoodsCategory.class);
        marginByCategory.put(GoodsCategory.FOOD, 10.0);
        marginByCategory.put(GoodsCategory.NONFOOD, 15.0);
        Store store = new Store(15, 10.0, marginByCategory);

        LocalDate expDate = LocalDate.of(2024, 5, 15);
        Goods goods1 = new Goods("1", "frozen peas", new BigDecimal(5), new BigDecimal(10), expDate, GoodsCategory.FOOD);
        Goods goods2 = new Goods("1", "notebook", new BigDecimal(15), new BigDecimal(20), expDate, GoodsCategory.NONFOOD);

        StoreService storeService = new StoreService(store);
        storeService.deliverGoods(goods1);

        storeService.deliverGoods(goods1);
        storeService.deliverGoods(goods1);
        storeService.deliverGoods(goods2);
        storeService.deliverGoods(goods2);
        storeService.deliverGoods(goods2);

        Customer customer = new Customer(new BigDecimal(1000));
        customer.addToCart(goods1, 1);
        customer.addToCart(goods2, 2);

        CashDesk cashDesk = new CashDesk(cashier1, store);
        CashDeskService cashDeskService = new CashDeskService(cashDesk, storeService);

        try {
            Receipt receipt = cashDeskService.checkout(customer);
            ReceiptService receiptService = new ReceiptService(receipt);
            int count = receiptService.countReceiptsIssued();

            receipt.getGoods();
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}