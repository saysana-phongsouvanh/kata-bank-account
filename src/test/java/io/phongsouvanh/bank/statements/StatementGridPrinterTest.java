package io.phongsouvanh.bank.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StatementGridPrinterTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private StatementGridPrinter statementGridPrinter;

    @BeforeEach
    public void setUp() {
        this.statementGridPrinter = new StatementGridPrinter();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("should print a formatted statement with operations")
    void shouldPrintFormattedStatementWithOperations() {
        final UUID accountId = UUID.randomUUID();
        final String formattedStatement = """
                +------------------------------------------------------------------------------------------------------+
                |                                                                                                      |
                |                                   ACCOUNT STATEMENT OF 2022-10-26                                    |
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
                +------------------------------------------------------------------------------------------------------+
                """.formatted(accountId);
        final List<String> formattedStatementAsList = Arrays.stream(formattedStatement.split("\n")).toList();

        statementGridPrinter.print(formattedStatementAsList);
        assertEquals(formattedStatement.replaceAll("\n", "\r\n"), outputStream.toString());
    }

    @Test
    @DisplayName("should print a formatted statement without operations")
    void shouldPrintFormattedStatementWithoutOperations() {
        final UUID accountId = UUID.randomUUID();
        final String formattedStatement = """
                +------------------------------------------------------------------------------------------------------+
                |                                                                                                      |
                |                                   ACCOUNT STATEMENT OF 2022-10-26                                    |
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
                +------------------------------------------------------------------------------------------------------+
                """.formatted(accountId);
        final List<String> formattedStatementAsList = Arrays.stream(formattedStatement.split("\n")).toList();

        statementGridPrinter.print(formattedStatementAsList);
        assertEquals(formattedStatement.replaceAll("\n", "\r\n"), outputStream.toString());
    }

}