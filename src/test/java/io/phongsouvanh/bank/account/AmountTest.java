package io.phongsouvanh.bank.account;

import io.phongsouvanh.bank.exceptions.NegativeAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountTest {

    @Test
    @DisplayName("should instantiate a new amount with a valid value")
    void shouldInstantiateAmountWithValidValue() throws Exception {
        BigDecimal bigDecimal = BigDecimal.TEN;
        Amount amount = new Amount(bigDecimal);
        assertEquals(BigDecimal.TEN, amount.getValue());
    }

    @Test
    @DisplayName("should not instantiate a new amount with an invalid value")
    void shouldNotInstantiateAmountWithInvalidValue() {
        BigDecimal bigDecimal = new BigDecimal(-10);
        assertThrows(NegativeAmountException.class, () -> new Amount(bigDecimal));
    }

}