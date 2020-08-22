package com.github.mgurov.talks.kotlinfortesting.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class BusinessLogicKotlinTest {
    @Test
    fun shouldSelectByBuyer() {
        //given
        val expected1 = PurchaseOrder("product 1", 1, "me")
        val expected2 = PurchaseOrder("product 2", 2, "me")
        val notExpected = PurchaseOrder("product 2", 2, "someone else")
        //when
        val actual = BusinessLogic.selectByBuyer("me", listOf(expected1, expected2, notExpected))
        //then
        Assertions.assertThat(actual)
                .containsExactlyInAnyOrder(expected1, expected2)
    }
}