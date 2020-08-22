package com.github.mgurov.talks.kotlinfortesting.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessLogicTest {

    @Test
    public void shouldSelectByBuyer() {
        //given
        PurchaseOrder expected1 = new PurchaseOrder("product 1", 1, BigDecimal.ONE, "me");
        PurchaseOrder expected2 = new PurchaseOrder("product 2", 2, BigDecimal.ZERO,"me");
        PurchaseOrder notExpected = new PurchaseOrder("product 2", 2, BigDecimal.TEN,"someone else");
        //when
        List<PurchaseOrder> actual = BusinessLogic.selectByBuyer("me", Arrays.asList(expected1, expected2, notExpected));
        //then
        assertThat(actual)
                .containsExactlyInAnyOrder(expected1, expected2);
    }

}