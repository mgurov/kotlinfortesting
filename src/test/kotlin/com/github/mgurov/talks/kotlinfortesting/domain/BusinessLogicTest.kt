package com.github.mgurov.talks.kotlinfortesting.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BusinessLogicKotlinTest {
    @Test
    fun shouldSelectByBuyer() {
        //given
        val expected1 = aPurchaseOrder(buyer= "me")
        val expected2 = aPurchaseOrder(buyer= "me")
        val notExpected = aPurchaseOrder(buyer= "someone else")
        //when
        val actual = BusinessLogic.selectByBuyer("me", listOf(expected1, expected2, notExpected))
        //then
        Assertions.assertThat(actual)
                .containsExactlyInAnyOrder(expected1, expected2)
    }

    fun aPurchaseOrder(
            productCode: String = "a product",
            quantity: Int = 1,
            price: BigDecimal = BigDecimal.TEN,
            buyer: String = "anonymous"
    ) = PurchaseOrder(
                productCode, quantity, price, buyer
        )

}