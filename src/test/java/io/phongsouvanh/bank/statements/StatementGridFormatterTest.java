package io.phongsouvanh.bank.statements;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StatementGridFormatterTest {

    private final Clock clock = Clock.fixed(Instant.parse("2022-10-25T15:30:00Z"), ZoneId.of("UTC"));
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private StatementGridFormatter statementGridFormatter;

    @BeforeEach
    public void setup() {
        this.statementGridFormatter  = new StatementGridFormatter();
    }

    @Test
    @DisplayName("should format statement with operations")
    void shouldFormatStatementWithOperations() throws Exception {
        final UUID accountId = UUID.randomUUID();
        final LocalDateTime date = LocalDateTime.now(clock);
        final List<Operation> operations = Arrays.asList(
            new Operation(accountId, OperationType.DEPOSIT, new Amount(new BigDecimal(10000)), LocalDateTime.now(clock), new BigDecimal(10000)),
            new Operation(accountId, OperationType.WITHDRAWAL, new Amount(new BigDecimal(100)), LocalDateTime.now(clock).plusMinutes(5), new BigDecimal(9900)),
            new Operation(accountId, OperationType.WITHDRAWAL, new Amount(new BigDecimal(7000)), LocalDateTime.now(clock).plusMinutes(6), new BigDecimal(2900))
        );
        final Statement statement = new Statement(accountId, LocalDateTime.now(clock), operations);
        final String expectedStatement = """
                +------------------------------------------------------------------------------------------------------+
                |                                                                                                      |
                |                                   ACCOUNT STATEMENT OF %s                                    |
                |                                                                                                      |
                +------------------------------------------------------------------------------------------------------+
                |                                           ACCOUNT DETAILS                                            |
                +------------------------------------------------------------------------------------------------------+
                |  Account Number       %s                                           |
                |  Balance              2900.00€                                                                       |
                +------------------------------------------------------------------------------------------------------+
                |                                                                                                      |
                |  TYPE            DATE                      AMOUNT                    BALANCE                         |
                +------------------------------------------------------------------------------------------------------+
                |  WITHDRAWAL      2022-10-25 15:36:00       -7000.00€                 2900.00€                        |
                |  WITHDRAWAL      2022-10-25 15:35:00       -100.00€                  9900.00€                        |
                |  DEPOSIT         2022-10-25 15:30:00       +10000.00€                10000.00€                       |
                +------------------------------------------------------------------------------------------------------+"""
                .formatted(date.format(dateFormatter), accountId);

        final String resultStatement = statementGridFormatter
                .format(statement)
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
        assertEquals(expectedStatement, resultStatement);
    }

    @Test
    @DisplayName("should format statement without operations")
    void shouldFormatStatementWithoutOperations() {
        final UUID accountId = UUID.randomUUID();
        final LocalDateTime date = LocalDateTime.now(clock);
        final List<Operation> operations = new ArrayList<>();
        final Statement statement = new Statement(accountId, LocalDateTime.now(clock), operations);
        final String expectedStatement = """
                +------------------------------------------------------------------------------------------------------+
                |                                                                                                      |
                |                                   ACCOUNT STATEMENT OF %s                                    |
                |                                                                                                      |
                +------------------------------------------------------------------------------------------------------+
                |                                           ACCOUNT DETAILS                                            |
                +------------------------------------------------------------------------------------------------------+
                |  Account Number       %s                                           |
                |  Balance              0.00€                                                                          |
                +------------------------------------------------------------------------------------------------------+
                |                                                                                                      |
                |  TYPE            DATE                      AMOUNT                    BALANCE                         |
                +------------------------------------------------------------------------------------------------------+
                |  No operations were found for this account                                                           |
                +------------------------------------------------------------------------------------------------------+"""
                .formatted(date.format(dateFormatter), accountId);

        final String resultStatement = statementGridFormatter
                .format(statement)
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
        assertEquals(expectedStatement, resultStatement);
    }

}