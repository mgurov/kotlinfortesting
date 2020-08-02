package com.github.mgurov.talks.kotlinfortesting.domain;

import java.util.List;

public class BusinessLogic {

    public static List<PurchaseOrder> selectByBuyer(String buyer, List<PurchaseOrder> purchaseOrders) {
        return purchaseOrders;
    }

    public static int countTotalQuantity(List<PurchaseOrder> purchaseOrders) {
        return 0;
    }

}
