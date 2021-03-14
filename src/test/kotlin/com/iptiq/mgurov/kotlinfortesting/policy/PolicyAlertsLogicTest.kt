package com.iptiq.mgurov.kotlinfortesting.policy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

internal class PolicyAlertsLogicTest {



    @Test
    fun `should alert on outstanding payment due`() {

        val givenPolicy = Policy(
            id = "whatever",
            userId = "whatever",
            activated = Instant.now(),
            expiration = null,
            payments = listOf(
                PolicyPayment(
                    amount = BigDecimal("12.34"),
                    currency = Currency.getInstance("EUR"),
                    direction = PaymentDirection.OUTGOING,
                    status = PolicyPaymentStatus.PENDING,
                    due = Instant.now().minus(1, ChronoUnit.HALF_DAYS)
                )
            )
        )

        val actual = PolicyAlertsLogic.shouldAlertOnDuePayout(givenPolicy)

        assertThat(actual).isTrue()
    }






    @Test
    fun `should alert on outstanding payment due - builder edition`() {

        val givenPolicy = PolicyTestBuilder.aPolicy()
            .withActivated(Instant.now().minusSeconds(10))
            .addPayments(
                PolicyPaymentTestBuilder.aPayment()
                    .withDirection(PaymentDirection.OUTGOING)
                    .withStatus(PolicyPaymentStatus.PENDING)
                    .withDue(Instant.now().minus(1, ChronoUnit.DAYS))
            )
            .build()

        val actual = PolicyAlertsLogic.shouldAlertOnDuePayout(givenPolicy)

        assertThat(actual).isTrue()
    }


    class PolicyTestBuilder {
        var id: String = UUID.randomUUID().toString()
        var userId: String = UUID.randomUUID().toString()
        var activated: Instant = Instant.now()
        var expiration: Instant? = null
        var payments: List<PolicyPayment> = emptyList()

        fun build(): Policy {
            return Policy(
                id,
                userId,
                activated,
                expiration,
                payments
            )
        }

        fun withActivated(activated: Instant): PolicyTestBuilder {
            this.activated = activated
            return this
        }

        fun addPayments(vararg policyPayments: PolicyPaymentTestBuilder): PolicyTestBuilder {
            val newPayments = policyPayments.map { it.build() }.toList()
            this.payments += newPayments
            return this
        }

        companion object {
            fun aPolicy() = PolicyTestBuilder()
        }
    }

    class PolicyPaymentTestBuilder {
        var amount: BigDecimal = BigDecimal.ONE
        var currency: Currency = Currency.getInstance("EUR")
        var direction: PaymentDirection = PaymentDirection.OUTGOING
        var status: PolicyPaymentStatus = PolicyPaymentStatus.PENDING
        var due: Instant = Instant.now().plus(1, ChronoUnit.DAYS)

        fun withDirection(direction: PaymentDirection): PolicyPaymentTestBuilder {
            this.direction = direction
            return this
        }

        fun withStatus(status: PolicyPaymentStatus): PolicyPaymentTestBuilder {
            this.status = status
            return this
        }

        fun build() = PolicyPayment(
            amount = amount,
            currency = currency,
            direction = direction,
            status = status,
            due = due
        )

        fun withDue(due: Instant): PolicyPaymentTestBuilder {
            this.due = due
            return this
        }

        companion object {
            fun aPayment() = PolicyPaymentTestBuilder()
        }
    }







    @Test
    fun `should alert on outstanding payment due - DSL aka Type-Safe Builders edition`() {

        val givenPolicy = aPolicy {
            activated = Instant.now().minusSeconds(10)

            payment {
                direction = PaymentDirection.OUTGOING
                status = PolicyPaymentStatus.PENDING
                due = Instant.now().minus(1, ChronoUnit.DAYS)
            }
        }

        val actual = PolicyAlertsLogic.shouldAlertOnDuePayout(givenPolicy)

        assertThat(actual).isTrue()
    }

    fun aPolicy(adjust: PolicyTestBuilder.()->Unit): Policy {
        val builder = PolicyTestBuilder()
        builder.adjust()
        return builder.build()
    }

    private fun PolicyTestBuilder.payment(adjust: PolicyPaymentTestBuilder.() -> Unit) {
        val paymentBuilder = PolicyPaymentTestBuilder()
        paymentBuilder.adjust()
        this.payments += paymentBuilder.build()
    }






    @Test
    fun `should alert on outstanding payment due - persisted edition`() {

        givenPersistedPolicy {
            activated = Instant.now().minusSeconds(10)

            payment {
                direction = PaymentDirection.INCOMING; status = PolicyPaymentStatus.PENDING; due = Instant.now().minus(1, ChronoUnit.DAYS)
            }
        }

        givenPersistedPolicy {
            activated = Instant.now().minusSeconds(10)

            payment {
                direction = PaymentDirection.OUTGOING; status = PolicyPaymentStatus.PENDING; due = Instant.now().minus(1, ChronoUnit.DAYS)
            }
        }

        assertThat(policyAlertingService.shouldAlertOnDuePayout())
            .isTrue()
    }

    val policyRepository = PolicyRepository()
    val policyAlertingService = PolicyAlertsService(policyRepository)

    fun givenPersistedPolicy(adjust: PolicyTestBuilder.()->Unit): Policy {
        val builder = PolicyTestBuilder()
        builder.adjust()
        val policy = builder.build()
        policyRepository.save(policy)
        return policy
    }






}