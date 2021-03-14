package com.iptiq.mgurov.kotlinfortesting.policy

import java.time.Instant

class PolicyAlertsService(
    val policyRepository: PolicyRepository
) {
    fun shouldAlertOnDuePayout(): Boolean {
        val now = Instant.now()
        return null != policyRepository.getAll().find {
            PolicyAlertsLogic.shouldAlertOnDuePayout(it, now)
        }
    }
}