package org.store;

import java.math.BigDecimal;
import java.util.Collection;

public class Store {
    private Collection<Cashier> cashiers;
    private Collection<Goods> deliveredGoods;
    private Collection<Goods> soldGoods;
    private Collection<Receipt> issuedReceipts;

// За всеки магазин трябва да може
// да се изчисли колко са разходите за: заплати на касиери и за доставка на стоки и колко са
// приходите от продадени стоки. Освен това, трябва да се изчислява колко е печалбата на
// магазина.

    public BigDecimal calculateCashierSalaryExpenses() {
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateGoodsDeliveryExpenses() {
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateGoodsSoldRevenue() {
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateStoreProfit() {
        return BigDecimal.ZERO;
    }
}
