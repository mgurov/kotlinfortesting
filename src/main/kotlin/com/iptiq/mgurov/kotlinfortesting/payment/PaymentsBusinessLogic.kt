package com.iptiq.mgurov.kotlinfortesting.payment

import java.math.BigDecimal
import java.util.*

class PaymentsBusinessLogic {
    fun totalAmount(payments: List<Payment>): BigDecimal {
        return payments.sumOf {
            when (it.direction) {
                PaymentDirection.OUTGOING -> -it.amount
                PaymentDirection.INCOMING -> it.amount
            }
        }
    }

    fun sumAmountByCurrency(payments: List<Payment>): Map<Currency, BigDecimal> {
        return payments.groupBy { it.currency }.mapValues { (_, currencyPayments) ->
            this.totalAmount(currencyPayments)
        }
    }
}