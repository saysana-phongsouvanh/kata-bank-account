package io.phongsouvanh.bank.operations;

import io.phongsouvanh.bank.account.Amount;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Operation(
        UUID accountId,
        OperationType type,
        Amount amount,
        LocalDateTime date,
        BigDecimal balance
) { }
