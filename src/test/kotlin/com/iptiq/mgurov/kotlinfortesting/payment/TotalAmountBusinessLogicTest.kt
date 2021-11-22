package com.iptiq.mgurov.kotlinfortesting.payment

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class TotalAmountBusinessLogicTest {

    val businessLogic = PaymentsBusinessLogic()

    @Test
    fun `should sum payments up`() {
        val actual = businessLogic.totalAmount(
            listOf(
                Payment(
                    userId = "whoever",
                    policyId = null,
                    amount = BigDecimal.ONE,
                    currency = Currency.getInstance("EUR"),
                    direction = PaymentDirection.INCOMING,
                ),
                Payment(
                    userId = "whoever",
                    policyId = null,
                    amount = BigDecimal("12.34"),
                    currency = Currency.getInstance("EUR"),
                    direction = PaymentDirection.INCOMING,
                ),
            )
        )

        assertThat(actual).isEqualTo(BigDecimal("13.34"))
    }











    @Test
    fun `should sum payments up - builder version`() {
        val actual = businessLogic.totalAmount(
            listOf(
                PaymentTestObjectBuilder.newPayment()
                    .withAmount(BigDecimal.ONE)
                    .build(),
                PaymentTestObjectBuilder.newPayment()
                    .withAmount(BigDecimal("12.34"))
                    .build()
            ),
        )

        assertThat(actual).isEqualTo(BigDecimal("13.34"))
    }















    @Test
    fun `should sum payments up - java-style functions`() {
        val actual = businessLogic.totalAmount(
            listOf(
                paymentWithAmount(BigDecimal.ONE),
                paymentWithAmount(BigDecimal("12.34")),
            ),
        )

        assertThat(actual).isEqualTo(BigDecimal("13.34"))
    }

    private fun paymentWithAmount(amount: BigDecimal): Payment = PaymentTestObjectBuilder.newPayment().withAmount(amount).build()










    @Test
    fun `should take direction into account`() {
        val actual = businessLogic.totalAmount(
            listOf(
                Payment(
                    userId = "whoever",
                    policyId = null,
                    amount = BigDecimal.ONE,
                    currency = Currency.getInstance("EUR"),
                    direction = PaymentDirection.INCOMING,
                ),
                Payment(
                    userId = "whoever",
                    policyId = null,
                    amount = BigDecimal("12.34"),
                    currency = Currency.getInstance("EUR"),
                    direction = PaymentDirection.OUTGOING,
                ),
            )
        )

        assertThat(actual).isEqualTo(BigDecimal("-11.34"))
    }















    @Test
    fun `should take direction into account - functions with defaults`() {

        val actual = businessLogic.totalAmount(
            aPayment(amount = "1.00", direction = PaymentDirection.INCOMING),
            aPayment(amount = "12.34", direction = PaymentDirection.OUTGOING),
        )

        assertThat(actual).isEqualTo(BigDecimal("-11.34"))
    }

    private fun aPayment(
        amount: String = "1.00",
        currency: String = "EUR",
        direction: PaymentDirection = PaymentDirection.INCOMING,
    ): Payment = PaymentTestObjectBuilder.newPayment()
        .withAmount(amount.toBigDecimal())
        .withCurrency(currency)
        .withDirection(direction)
        .build()

    private fun PaymentsBusinessLogic.totalAmount(vararg payments: Payment): BigDecimal {
        return this.totalAmount(payments.toList())
    }








    @Test
    fun `should group by valuta`() {

        val actual = businessLogic.sumAmountByCurrency(
            aPayment(currency = "EUR", amount = "1.00", direction = PaymentDirection.INCOMING),
            aPayment(currency = "UAH", amount = "12.34", direction = PaymentDirection.OUTGOING),
        )

        assertThat(actual).isEqualTo(mapOf(
            Currency.getInstance("EUR") to BigDecimal("1.00"),
            Currency.getInstance("UAH") to BigDecimal("-12.34"),
        ))

        assertThat(actual.map { (currency, amount) ->
            currency.currencyCode to amount.toString()
        })
            .containsExactlyInAnyOrder(
                "EUR" to "1.00",
                "UAH" to "-12.34",
            )
    }

    private fun PaymentsBusinessLogic.sumAmountByCurrency(vararg payments: Payment) = this.sumAmountByCurrency(payments.toList())











    @Test
    fun `extracting style`() {

        val actual = listOf(
            aPayment(currency = "EUR", amount = "1.00", direction = PaymentDirection.INCOMING),
            aPayment(currency = "UAH", amount = "12.34", direction = PaymentDirection.OUTGOING),
        )

        assertThat(actual.map { payment ->
            payment.currency.currencyCode to payment.amount.toString()
        })
            .containsExactlyInAnyOrder(
                "EUR" to "1.00",
                "UAH" to "12.34",
            )

        assertThat(actual)
            //.extracting("currency", "amount")
            .extracting({ payment -> payment.currency.currencyCode }, { payment -> payment.amount.toString() })
            .containsExactlyInAnyOrder(
                tuple("EUR" , "1.00"),
                tuple("UAH" , "12.34"),
            )
    }


}