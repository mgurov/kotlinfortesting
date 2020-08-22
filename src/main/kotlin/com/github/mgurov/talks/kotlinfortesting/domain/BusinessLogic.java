package com.github.mgurov.talks.kotlinfortesting.domain;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessLogic {

    public static List<PurchaseOrder> selectByBuyer(String buyer, List<PurchaseOrder> purchaseOrders) {
        return purchaseOrders.stream()
                .filter(po -> buyer.equals(po.getBuyer()))
                .collect(Collectors.toList());
    }

    public static int countTotalQuantity(List<PurchaseOrder> purchaseOrders) {
        return 0;
    }

}
