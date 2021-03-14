package com.iptiq.mgurov.kotlinfortesting.payment;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public class PaymentTestObjectBuilder {
  public String userId = UUID.randomUUID().toString();
  public String policyId = null;
  public BigDecimal amount = BigDecimal.ONE;
  public String currency = "EUR";
  public PaymentDirection direction = PaymentDirection.INCOMING;

  public Payment build() {
    return new Payment(
        userId,
        policyId,
        amount,
        Currency.getInstance(currency),
        direction
    );
  }
  
  public PaymentTestObjectBuilder withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public PaymentTestObjectBuilder withPolicyId(String policyId) {
    this.policyId = policyId;
    return this;
  }

  public PaymentTestObjectBuilder withAmount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  public PaymentTestObjectBuilder withCurrency(String currency) {
    this.currency = currency;
    return this;
  }

  public PaymentTestObjectBuilder withDirection(PaymentDirection direction) {
    this.direction = direction;
    return this;
  }

  public static PaymentTestObjectBuilder newPayment() {
    return new PaymentTestObjectBuilder();
  }
}
