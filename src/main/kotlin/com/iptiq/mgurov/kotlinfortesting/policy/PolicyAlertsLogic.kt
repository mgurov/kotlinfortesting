package com.iptiq.mgurov.kotlinfortesting.policy

import java.time.Instant

object PolicyAlertsLogic {
    fun shouldAlertOnDuePayout(
        policy: Policy,
        now: Instant = Instant.now(),
    ): Boolean {

        if (policy.activated.isAfter(now)) {
            return false
        }

        val alertingPolicy = policy.payments.find { payment ->
            payment.direction == PaymentDirection.OUTGOING
                    && payment.status == PolicyPaymentStatus.PENDING
                    && payment.due.isBefore(now)
        }

        return null != alertingPolicy
    }
}