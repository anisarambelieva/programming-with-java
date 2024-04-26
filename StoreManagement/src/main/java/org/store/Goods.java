package org.store;

import java.math.BigDecimal;
import java.util.Date;

public class Goods {
    private String id;
    private String name;
    private BigDecimal deliveryPrice;
    private BigDecimal price;
    private Category category;
    private Date expirationDate;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
