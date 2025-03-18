package com.iptiq.mgurov.kotlinfortesting.payment

import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class TotalAmountBusinessLogicTest {

    private val businessLogic = PaymentsBusinessLogic()


    @Test // 00
    fun `should sum payments up`() {
        val actual = businessLogic.totalAmount(listOfPayments)

        assertThat(actual).isEqualTo(BigDecimal("13.34"))
    }





























    private val listOfPayments = listOf(
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





























    @Test // 01
    fun `should sum payments up - local dependencies`() {
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

        actual shouldBe BigDecimal("13.34")
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















    @Test //03
    fun `should take direction into account - functions with defaults`() {

        // NB:
        val actual = businessLogic.totalAmount(
            aPayment(amount = "1.00", direction = PaymentDirection.INCOMING),
            aPayment(amount = "12.34", direction = PaymentDirection.OUTGOING),
        )

        actual shouldBe BigDecimal("-11.34")
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







    @Test //04
    fun `should group by valuta`() {

        val productionActual = businessLogic.sumAmountByCurrency(listOf(
            aPayment(currency = "EUR", amount = "1.00", direction = PaymentDirection.INCOMING),
            aPayment(currency = "EUR", amount = "2.00", direction = PaymentDirection.INCOMING),
            aPayment(currency = "UAH", amount = "12.34", direction = PaymentDirection.OUTGOING),
        ))

        productionActual shouldBe mapOf(
            Currency.getInstance("EUR") to BigDecimal("3.00"),
            Currency.getInstance("UAH") to BigDecimal("-12.34"),
        )

        // ----------------------------------------- OR ---------------------------------------

        val actual = businessLogic.sumAmountByCurrency(
            aPayment(currency = "EUR", amount = "1.00", direction = PaymentDirection.INCOMING),
            aPayment(currency = "EUR", amount = "2.00", direction = PaymentDirection.INCOMING),
            aPayment(currency = "UAH", amount = "12.34", direction = PaymentDirection.OUTGOING),
        )

        actual shouldBe mapOf(
            "EUR" to "3.00",
            "UAH" to "-12.34",
        )

    }

    private fun PaymentsBusinessLogic.sumAmountByCurrency(vararg payments: Payment) = this.sumAmountByCurrency(payments.toList())
        .map { (currency, sum) -> currency.currencyCode to sum.toString() }.toMap()





    // 05 - go to the PolicyAlertsLogicTest
































    @Test
    fun `should take direction into account - copy template`() {

        val actual = businessLogic.totalAmount(
            aPaymentTemplate().copy(amount = "1.00".toBigDecimal(), direction = PaymentDirection.INCOMING),
            aPaymentTemplate().copy(amount = "12.34".toBigDecimal(), direction = PaymentDirection.OUTGOING),
        )

        assertThat(actual).isEqualTo(BigDecimal("-11.34"))

        //not for everything
        //nesting can be nasty
    }

    private fun aPaymentTemplate(): Payment = PaymentTestObjectBuilder.newPayment()
        .withAmount(BigDecimal.ONE)
        .withCurrency("EUR")
        .withDirection(PaymentDirection.INCOMING)
        .build()


























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