package org.store.models;

public class CashDesk {
    private Cashier cashier;
    private Store store;

    public CashDesk(Cashier cashier, Store store) {
        this.cashier = cashier;
        this.store = store;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public Store getStore() {
        return store;
    }

    @Override
    public String toString() {
        return "CashDesk{" +
                "cashier=" + cashier +
                ", store=" + store +
                '}';
    }
}
