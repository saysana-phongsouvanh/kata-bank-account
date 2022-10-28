package io.phongsouvanh.bank.statements;

import io.phongsouvanh.bank.operations.Operation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Statement {

    private final UUID accountId;
    private BigDecimal balance;
    private final LocalDate date;
    private List<Operation> operations = new ArrayList<>();

    public Statement(UUID accountId, LocalDate date) {
        this.accountId = accountId;
        this.date = date;
    }

    public Statement(UUID accountId, LocalDate date, List<Operation> operations) {
        this(accountId, date);
        this.operations = operations;
        updateAccountDetails();
    }

    private void updateAccountDetails() {
        operations.sort(Comparator.comparing(Operation::date).reversed());
        balance = operations.size() > 0 ? operations.get(0).balance() : BigDecimal.ZERO;
    }

    public void add(Operation operation) {
        operations.add(operation);
        updateAccountDetails();
    }

    public LocalDate getDate() {
        return date;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
