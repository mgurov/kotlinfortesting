package com.github.mgurov.talks.kotlinfortesting.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessLogic {

    public static List<PurchaseOrder> selectByBuyer(String buyer, List<PurchaseOrder> purchaseOrders) {
        return purchaseOrders.stream()
                .filter(po -> buyer.equals(po.getBuyer()))
                .collect(Collectors.toList());
    }

    /**
     * @return the list of purchases made by buyer the total cost of which exceed 20
     */
    public  static List<PurchaseOrder> selectExpensiveBuyerPurchases(String buyer, List<PurchaseOrder> purchaseOrders) {
        return purchaseOrders.stream()
                .filter(po -> buyer.equals(po.getBuyer()))
                .filter(po -> po.getPrice().multiply(new BigDecimal(po.getQuantity())).compareTo(BigDecimal.valueOf(20L)) > 0)
                .collect(Collectors.toList());
    }

}
