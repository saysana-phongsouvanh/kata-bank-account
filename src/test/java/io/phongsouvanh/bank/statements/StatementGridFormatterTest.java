package io.phongsouvanh.bank.statements;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StatementGridFormatterTest {

    private final Clock clock = Clock.fixed(Instant.parse("2022-10-25T15:30:00Z"), ZoneId.of("UTC"));

    private StatementGridFormatter statementGridFormatter;

    @BeforeEach
    public void setup() {
        this.statementGridFormatter  = new StatementGridFormatter();
    }

    @Test
    @DisplayName("should format statement with operations")
    void shouldFormatStatementWithOperations() throws Exception {
        final UUID accountId = UUID.nameUUIDFromBytes(new byte[]{42});
        final List<Operation> operations = Arrays.asList(
            new Operation(accountId, OperationType.DEPOSIT, new Amount(new BigDecimal(10000)), LocalDateTime.now(clock), new BigDecimal(10000)),
            new Operation(accountId, OperationType.WITHDRAWAL, new Amount(new BigDecimal(100)), LocalDateTime.now(clock).plusMinutes(5), new BigDecimal(9900)),
            new Operation(accountId, OperationType.WITHDRAWAL, new Amount(new BigDecimal(7000)), LocalDateTime.now(clock).plusMinutes(6), new BigDecimal(2900))
        );
        final Statement statement = new Statement(accountId, LocalDate.now(clock), operations);
        final List<String> expectedStatement = Arrays.asList(
                "+------------------------------------------------------------------------------------------------------+",
                "|                                                                                                      |",
                "|                                   ACCOUNT STATEMENT OF 2022-10-25                                    |",
                "|                                                                                                      |",
                "+------------------------------------------------------------------------------------------------------+",
                "|                                           ACCOUNT DETAILS                                            |",
                "+------------------------------------------------------------------------------------------------------+",
                "|  Account Number       3389dae3-61af-39b0-8c9c-8e7057f60cc6                                           |",
                "|  Balance              2900.00€                                                                       |",
                "+------------------------------------------------------------------------------------------------------+",
                "|                                                                                                      |",
                "|  TYPE            DATE                      AMOUNT                    BALANCE                         |",
                "+------------------------------------------------------------------------------------------------------+",
                "|  WITHDRAWAL      2022-10-25 15:36:00       -7000.00€                 2900.00€                        |",
                "|  WITHDRAWAL      2022-10-25 15:35:00       -100.00€                  9900.00€                        |",
                "|  DEPOSIT         2022-10-25 15:30:00       +10000.00€                10000.00€                       |",
                "+------------------------------------------------------------------------------------------------------+"
            );

        final List<String> resultStatement = statementGridFormatter.format(statement);
        assertEquals(expectedStatement, resultStatement);
    }

    @Test
    @DisplayName("should format statement without operations")
    void shouldFormatStatementWithoutOperations() {
        final UUID accountId = UUID.nameUUIDFromBytes(new byte[]{42});
        final List<Operation> operations = new ArrayList<>();
        final Statement statement = new Statement(accountId, LocalDate.now(clock), operations);
        final List<String> expectedStatement = Arrays.asList(
                "+------------------------------------------------------------------------------------------------------+",
                "|                                                                                                      |",
                "|                                   ACCOUNT STATEMENT OF 2022-10-25                                    |",
                "|                                                                                                      |",
                "+------------------------------------------------------------------------------------------------------+",
                "|                                           ACCOUNT DETAILS                                            |",
                "+------------------------------------------------------------------------------------------------------+",
                "|  Account Number       3389dae3-61af-39b0-8c9c-8e7057f60cc6                                           |",
                "|  Balance              0.00€                                                                          |",
                "+------------------------------------------------------------------------------------------------------+",
                "|                                                                                                      |",
                "|  TYPE            DATE                      AMOUNT                    BALANCE                         |",
                "+------------------------------------------------------------------------------------------------------+",
                "|  No operations were found for this account                                                           |",
                "+------------------------------------------------------------------------------------------------------+"
        );

        final List<String> resultStatement = statementGridFormatter.format(statement);
        assertEquals(expectedStatement, resultStatement);
    }

}