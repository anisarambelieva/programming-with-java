package org.store;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class Receipt {
    private String serialNumber;
    private Cashier cashier;
    private Date issueDate;
    private Collection<Goods> goodsList;
    private BigDecimal price;
    public static int totalReceiptsCount;

    public Receipt(String serialNumber, Cashier cashier, Date issueDate, Collection<Goods> goodsList) {
        this.serialNumber = serialNumber;
        this.cashier = cashier;
        this.issueDate = issueDate;
        this.goodsList = goodsList;

        Receipt.totalReceiptsCount++;
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal result = BigDecimal.ZERO;

        for (Goods g : this.goodsList) {
            result.add(g.getPrice());
        }

        return result;
    }
// списък със
// стоки, които се включват в касовата бележка включително цената и количеството им и общата
// стойност, която трябва да се заплати от клиента.
}
