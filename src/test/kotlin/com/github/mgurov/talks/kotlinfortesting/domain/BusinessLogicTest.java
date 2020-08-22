package com.github.mgurov.talks.kotlinfortesting.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessLogicTest {

    @Test
    public void shouldSelectByBuyer() {
        //given
        PurchaseOrder expected1 = new PurchaseOrder("product 1", 1, "me");
        PurchaseOrder expected2 = new PurchaseOrder("product 2", 2, "me");
        PurchaseOrder notExpected = new PurchaseOrder("product 2", 2, "someone else");
        //when
        List<PurchaseOrder> actual = BusinessLogic.selectByBuyer("me", Arrays.asList(expected1, expected2, notExpected));
        //then
        assertThat(actual)
                .containsExactlyInAnyOrder(expected1, expected2);
    }

}