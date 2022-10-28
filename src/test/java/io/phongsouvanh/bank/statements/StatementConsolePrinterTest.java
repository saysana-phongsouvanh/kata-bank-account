package io.phongsouvanh.bank.statements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatementConsolePrinterTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private StatementConsolePrinter statementConsolePrinter;

    @BeforeEach
    public void setUp() {
        this.statementConsolePrinter = new StatementConsolePrinter();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("should print something if a list was passed")
    void shouldPrintSomething() {
        final List<String> formattedStatement = Arrays.asList(
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
                "|  WITHDRAWAL      2022-10-22 15:15:00       -100.00€                  9900.00€                        |",
                "|  DEPOSIT         2022-10-23 18:30:00       +10000.00€                10000.00€                       |",
                "+------------------------------------------------------------------------------------------------------+"
        );

        statementConsolePrinter.print(formattedStatement);
        assertEquals(formattedStatement, Arrays.stream(outputStream.toString().split(System.lineSeparator())).toList() );
    }

    @Test
    @DisplayName("should print nothing if no list was passed")
    void shouldPrintNothing() {
        statementConsolePrinter.print(List.of());
        assertEquals("", outputStream.toString());
    }

}