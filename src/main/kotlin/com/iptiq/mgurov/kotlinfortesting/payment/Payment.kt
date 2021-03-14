package com.iptiq.mgurov.kotlinfortesting.payment

import java.math.BigDecimal
import java.util.*

data class Payment(
    val userId: String,
    val policyId: String?,
    val amount: BigDecimal,
    val currency: Currency,
    val direction: PaymentDirection,
)

enum class PaymentDirection{
    OUTGOING,
    INCOMING,
}