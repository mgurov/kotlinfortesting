package com.iptiq.mgurov.kotlinfortesting.policy

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

internal class PolicyAlertsLogicTest {



    @Test //01
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

        PolicyAlertsLogic.shouldAlertOnDuePayout(givenPolicy) shouldBe true
    }

    @Test //02
    fun `should alert on outstanding payment due - DSL aka Type-Safe Builders edition`() {

        val givenPolicy = aPolicy{
            activated = Instant.now() - Duration.ofSeconds(10L)

            payment {
                direction = PaymentDirection.OUTGOING
                status = PolicyPaymentStatus.PENDING
                due = Instant.now() - Duration.ofDays(1L)
            }
        }

        PolicyAlertsLogic.shouldAlertOnDuePayout(givenPolicy) shouldBe true
    }


























    fun aPolicy(adjust: PolicyTestBuilder.()->Unit) = PolicyTestBuilder().also(adjust).build()

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

        fun payment(adjust: PolicyPaymentTestBuilder.() -> Unit) {
            val paymentBuilder = PolicyPaymentTestBuilder()
            paymentBuilder.adjust()
            this.payments += paymentBuilder.build()
        }

    }

    class PolicyPaymentTestBuilder {
        var amount: BigDecimal = BigDecimal.ONE
        var currency: Currency = Currency.getInstance("EUR")
        var direction: PaymentDirection = PaymentDirection.OUTGOING
        var status: PolicyPaymentStatus = PolicyPaymentStatus.PENDING
        var due: Instant = Instant.now().plus(1, ChronoUnit.DAYS)

        fun build() = PolicyPayment(
            amount = amount,
            currency = currency,
            direction = direction,
            status = status,
            due = due
        )
    }


    // 03
    // https://kotlinlang.org/docs/type-safe-builders.html#scope-control-dslmarker





















    @Test //04
    fun `should alert on outstanding payment due - persisted edition`() {

        givenPersistedPolicy {
            activated = Instant.now().minusSeconds(10)

            payment {
                direction = PaymentDirection.INCOMING;
                status = PolicyPaymentStatus.PENDING;
                due = Instant.now() - Duration.ofDays(1)
            }
        }

        givenPersistedPolicy {
            activated = Instant.now().minusSeconds(10)

            payment {
                direction = PaymentDirection.OUTGOING;
                status = PolicyPaymentStatus.PENDING;
                due = Instant.now() - Duration.ofDays(1)
            }
        }

        assertThat(policyAlertingService.shouldAlertOnDuePayout())
            .isTrue()
    }

    val policyRepository = PolicyRepository()
    val policyAlertingService = PolicyAlertsService(policyRepository)

    fun givenPersistedPolicy(adjust: PolicyTestBuilder.()->Unit): Policy {
        val policy = aPolicy(adjust)
        policyRepository.save(policy)
        return policy
    }





























    @Test //extra 01
    fun `should alert on outstanding payment due - mock version`() {

        val givenPolicy = mockk<Policy> {
            every { activated } returns Instant.now()
            every { payments } returns listOf(
                mockk<PolicyPayment> {
                    every { direction } returns PaymentDirection.OUTGOING
                    every { status } returns PolicyPaymentStatus.PENDING
                    every { due } returns Instant.now().minus(1, ChronoUnit.HALF_DAYS)
                }
            )
        }

        PolicyAlertsLogic.shouldAlertOnDuePayout(givenPolicy) shouldBe true
    }















}