package io.phongsouvanh.bank.statements;

import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatementGridFormatter implements StatementFormatter {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static int gridSize = 102;

    @Override
    public List<String> format(Statement statement) {
        List<String> formattedStatement = new ArrayList<>();
        formattedStatement.addAll(formatStatementHeader(statement.getDate()));
        formattedStatement.addAll(formatAccountDetails(statement.getAccountId(), statement.getBalance()));
        formattedStatement.addAll(formatOperationHeader());

        if(statement.getOperations().isEmpty()) {
            formattedStatement.add(String.format("|  %-100s|", "No operations were found for this account"));
        } else {
            for (Operation operation : statement.getOperations()) {
                formattedStatement.add(formatOperationLine(operation));
            }
        }

        formattedStatement.add(formatSeparator());
        return formattedStatement;
    }

    private List<String> formatStatementHeader(LocalDate date) {
        return Arrays.asList(
          formatSeparator(),
          formatEmptyLine(),
          "|" + centerString("ACCOUNT STATEMENT OF " + date)  + "|",
          formatEmptyLine()
        );
    }

    private List<String> formatAccountDetails(UUID accountId, BigDecimal balance) {
        return Arrays.asList(
            formatSeparator(),
            "|" + centerString("ACCOUNT DETAILS") + "|",
            formatSeparator(),
            String.format("|  %-20s %-77s  |", "Account Number", accountId.toString()),
            String.format("|  %-20s %-77s  |", "Balance", balance.setScale(2, RoundingMode.HALF_EVEN) + "€"),
            formatSeparator()
        );
    }

    private List<String> formatOperationHeader() {
        return Arrays.asList(
                formatEmptyLine(),
                String.format("|  %-15s %-25s %-25s %-30s  |", "TYPE", "DATE", "AMOUNT", "BALANCE"),
                formatSeparator()
        );
    }

    private String centerString (String s) {
        return String.format("%-" + StatementGridFormatter.gridSize + "s", String.format("%" + (s.length() + (StatementGridFormatter.gridSize - s.length()) / 2) + "s", s));
    }

    private String formatSeparator() {
        return "+" + "-".repeat(gridSize) + "+";
    }

    private String formatEmptyLine() {
        return "|" + " ".repeat(gridSize) + "|";
    }

    private String formatOperationLine(Operation operation){
        String type = operation.type().toString();
        String date = operation.date().format(dateTimeFormatter);
        String amount = operation.amount().getValue().setScale(2, RoundingMode.HALF_EVEN) + "€";
        amount = operation.type().equals(OperationType.WITHDRAWAL) ? "-" + amount : "+" + amount;
        String balance = operation.balance().setScale(2, RoundingMode.HALF_EVEN) + "€";
        return String.format("|  %-15s %-25s %-25s %-30s  |", type, date, amount, balance);
    }

}
