package com.github.mgurov.talks.kotlinfortesting.domain;

public class PurchaseOrder {
    private final String productCode;
    private final int quantity;
    private final String buyer;

    public PurchaseOrder(String productCode, int quantity, String buyer) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.buyer = buyer;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBuyer() {
        return buyer;
    }
}
