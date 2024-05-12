package org.store.exceptions;

public class NotEnoughGoodsAvailableException extends RuntimeException{
    public NotEnoughGoodsAvailableException(String goodsName, double quantity) {
        super(String.format("Checkout not possible! %s needs %.2f in inventory!", goodsName, quantity));
    }
}
