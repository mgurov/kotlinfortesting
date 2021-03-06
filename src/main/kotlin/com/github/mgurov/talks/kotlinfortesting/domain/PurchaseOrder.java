package com.github.mgurov.talks.kotlinfortesting.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class PurchaseOrder {
    private final String productCode;
    private final int quantity;
    private final BigDecimal price;
    private final String buyer;

    public PurchaseOrder(String productCode, int quantity, BigDecimal price, String buyer) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.buyer = buyer;
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getBuyer() {
        return buyer;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "productCode='" + productCode + '\'' +
                ", quantity=" + quantity +
                ", buyer='" + buyer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrder that = (PurchaseOrder) o;
        return quantity == that.quantity &&
                Objects.equals(productCode, that.productCode) &&
                Objects.equals(buyer, that.buyer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, quantity, buyer);
    }
}
