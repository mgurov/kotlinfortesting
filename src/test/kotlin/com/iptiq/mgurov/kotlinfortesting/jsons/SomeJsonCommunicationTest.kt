package com.iptiq.mgurov.kotlinfortesting.jsons

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.iptiq.mgurov.kotlinfortesting.payment.Payment
import com.iptiq.mgurov.kotlinfortesting.payment.PaymentDirection
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class SomeJsonCommunicationTest {




    @Test
    fun `should deserialize`() {
        val givenPayment = Payment(
            userId = "userId",
            policyId = null,
            amount = "10.12".toBigDecimal(),
            currency = Currency.getInstance("EUR"),
            direction = PaymentDirection.INCOMING
        )


        val asJson = mapper.writeValueAsString(givenPayment)

        assertThat(asJson).isEqualTo(
            """
                {"userId":"userId","policyId":null,"amount":10.12,"currency":"EUR","direction":"${givenPayment.direction}"}
            """.trimIndent()
        )
    }












    @Test
    fun `should deserialize - comparing trees`() {
        val givenPayment = Payment(
            userId = "userId",
            policyId = null,
            amount = "10.12".toBigDecimal(),
            currency = Currency.getInstance("EUR"),
            direction = PaymentDirection.INCOMING
        )

        val whenJsonProduced = mapper.writeValueAsString(givenPayment)

        //then
        val expectedJson = """
        {
            "userId":"userId",
            "policyId":null,
            "amount":10.12,
            "currency":"EUR",
            "direction":"${givenPayment.direction}"
        }
        """.trimIndent()

        assertThat(mapper.readTree(whenJsonProduced)).isEqualTo(mapper.readTree(expectedJson))
    }









}

val mapper = jacksonObjectMapper()
