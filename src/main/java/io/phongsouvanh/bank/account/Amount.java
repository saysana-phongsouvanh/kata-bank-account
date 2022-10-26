package io.phongsouvanh.bank.account;

import io.phongsouvanh.bank.exceptions.NegativeAmountException;

import java.math.BigDecimal;
import java.util.Objects;

public class Amount {

    private final BigDecimal value;

    public Amount(BigDecimal value) throws NegativeAmountException {
        if(value.compareTo(BigDecimal.ZERO) < 0) throw new NegativeAmountException();
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Objects.equals(value, amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
