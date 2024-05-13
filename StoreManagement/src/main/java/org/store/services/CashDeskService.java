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

    private boolean checkGoodsInventory(Map<Goods, Double> customerCart) {
        Store store = this.cashDesk.getStore();
        Map<Goods, Double> inventory = store.getInventory();

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

        return true;
    }

    private boolean canCustomerPay(Customer customer, BigDecimal price) {
        BigDecimal customerMoney = customer.getMoney();

        if (customerMoney.compareTo(price) < 0) {
            throw new NotEnoughMoneyException("Customer does not have enough money to pay!");
        }

        return true;
    }

    public Receipt checkout(Customer customer) {
        Map<Goods, Double> customerCart = customer.getCart();

        try {
            checkGoodsInventory(customerCart);
        } catch (NotEnoughGoodsAvailableException e) {
            throw e;
        }

        this.storeService.addSoldGoods(customerCart);
        this.storeService.removeFromInventory(customerCart);

        String serialNumber = UUID.randomUUID().toString();
        LocalDate issueDate = LocalDate.now();
        Receipt receipt = new Receipt(serialNumber, this.cashDesk.getCashier(), issueDate, customerCart);
        ReceiptService receiptService = new ReceiptService(receipt);
        BigDecimal priceToPay = receiptService.calculateTotalPrice();

        try {
            canCustomerPay(customer, priceToPay);
        } catch (NotEnoughMoneyException e) {
            throw e;
        }

        return receipt;
    }
}
