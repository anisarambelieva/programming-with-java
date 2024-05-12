package org.store.services;

import org.store.exceptions.NotEnoughGoodsAvailableException;
import org.store.exceptions.NotEnoughMoneyException;
import org.store.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class CashDeskService {
    private CashDesk cashDesk;
    private StoreService storeService;

    public CashDeskService(CashDesk cashDesk, StoreService storeService) {
        this.cashDesk = cashDesk;
        this.storeService = storeService;
    }

//    private double checkGoodsInventory(Map<Goods, Double> inventory, Map<Goods, Double> customerCart) { }

    public Receipt checkout(Customer customer) {
        Store store = this.cashDesk.getStore();
        Map<Goods, Double> inventory = store.getInventory();
        Map<Goods, Double> customerCart = customer.getCart();

        // TODO: Extract in private method
        for (Map.Entry<Goods, Double> entry : customerCart.entrySet()) {
            Goods goods = entry.getKey();
            double quantity = entry.getValue();

            double inventoryQuantity = inventory.getOrDefault(goods, 0.0);

            double difference = quantity - inventoryQuantity;
            if (difference >= 0) {
                double neededQuantity = (difference != 0) ? difference : quantity;
                throw new NotEnoughGoodsAvailableException(goods.getName(), neededQuantity);
            }
        }

        this.storeService.addSoldGoods(customerCart);
        this.storeService.removeFromInventory(customerCart);

        String serialNumber = UUID.randomUUID().toString();
        LocalDate issueDate = LocalDate.now();

        Receipt receipt = new Receipt(serialNumber, this.cashDesk.getCashier(), issueDate, customerCart);
        ReceiptService receiptService = new ReceiptService(receipt);

        BigDecimal priceToPay = receiptService.calculateTotalPrice();
        BigDecimal customerMoney = customer.getMoney();

        if (customerMoney.compareTo(priceToPay) < 0) {
            throw new NotEnoughMoneyException("Customer does not have enough money to pay!");
        }

        return receipt;
    }
}
