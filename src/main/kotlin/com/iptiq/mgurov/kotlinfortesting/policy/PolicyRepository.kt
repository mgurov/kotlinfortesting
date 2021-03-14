package com.iptiq.mgurov.kotlinfortesting.policy

class PolicyRepository {

    private val policies = mutableMapOf<String, Policy>()

    fun save(policy: Policy) {
        policies[policy.id] = policy
    }

    fun getAll(): Sequence<Policy> {
        return policies.values.asSequence()
    }
}