package com.iptiq.mgurov.kotlinfortesting.policy

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class Policy(
    val id: String,
    val userId: String,
    val activated: Instant,
    val expiration: Instant?,
    val payments: List<PolicyPayment>,
)

data class PolicyPayment(
    val amount: BigDecimal,
    val currency: Currency,
    val direction: PaymentDirection,
    val status: PolicyPaymentStatus,
    val due: Instant,
)

enum class PolicyPaymentStatus {
    PENDING,
    FULFILLED,
    FAILED
}

enum class PaymentDirection{
    OUTGOING,
    INCOMING,
}