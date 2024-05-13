package org.store.services;

import org.store.exceptions.NotEnoughGoodsAvailableException;
import org.store.exceptions.NotEnoughMoneyException;
import org.store.models.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

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

    private BigDecimal calculatePriceWithDiscount(BigDecimal price, double discountPercent) {
        BigDecimal discount = price.multiply(new BigDecimal(discountPercent/100));
        BigDecimal result = price.subtract(discount);

        return result.setScale(2, BigDecimal.ROUND_UP);
    }

    private void adjustPricesBasedOnExpiryDate(Map<Goods, Double> cart) {
        Store store = this.cashDesk.getStore();
        int countDays = store.getCountDaysForExpiryDateDiscount();
        double discount = store.getExpiryDateDiscount();

        LocalDate now = LocalDate.now();

        for (Map.Entry<Goods, Double> entry : cart.entrySet()) {
            Goods goods = entry.getKey();
            LocalDate expDate =  goods.getExpirationDate();
            long daysBetween = DAYS.between(expDate, now);

            BigDecimal currentPrice = goods.getPrice();

            if (daysBetween <= countDays) {
                BigDecimal updatedPrice = calculatePriceWithDiscount(currentPrice, discount);
                goods.setPrice(updatedPrice);
            }
        }
    }

    private void saveReceiptToFile(Receipt receipt) {
        String directoryPath = "src/main/resources/receipts";
        String serialNumber = receipt.getSerialNumber();
        String fileName = String.format("%s.txt", serialNumber);
        String content = receipt + "\n";

        try {
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File created: " + file.getAbsolutePath());
            }

            // Open the file in append mode using FileWriter
            FileWriter fileWriter = new FileWriter(file, true);

            // Or use BufferedWriter for efficient writing
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Append the content to the file
            bufferedWriter.write(content);

            // Close the resources
            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Content appended to " + file.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public Receipt checkout(Customer customer) {
        Map<Goods, Double> customerCart = customer.getCart();

        try {
            checkGoodsInventory(customerCart);
        } catch (NotEnoughGoodsAvailableException e) {
            throw e;
        }

        this.storeService.removeFromInventory(customerCart);

        adjustPricesBasedOnExpiryDate(customerCart);
        this.storeService.addSoldGoods(customerCart);

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

        System.out.println(receipt);
        saveReceiptToFile(receipt);
        return receipt;
    }
}
