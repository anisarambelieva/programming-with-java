package org.store.models;

import java.time.LocalDate;
import java.util.Map;

public class Receipt {
    private String serialNumber;
    private Cashier cashier;
    private LocalDate issueDate;
    private Map<Goods, Double> goods;

    public Receipt(String serialNumber, Cashier cashier, LocalDate issueDate, Map<Goods, Double> goods) {
        this.serialNumber = serialNumber;
        this.cashier = cashier;
        this.issueDate = issueDate;
        this.goods = goods;
    }

    public void setGoods(Map<Goods, Double> goods) {
        this.goods = goods;
    }

    public Map<Goods, Double> getGoods() {
        return goods;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "Receipt {" +
                "\n  serialNumber = '" + serialNumber + '\'' +
                "\n  cashier = " + cashier +
                "\n  issueDate = " + issueDate +
                "\n  goods = " + goods +
                "\n}";
    }
}

