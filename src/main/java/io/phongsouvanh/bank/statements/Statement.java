package io.phongsouvanh.bank.statements;

import io.phongsouvanh.bank.operations.Operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Statement {

    private final UUID accountId;
    private BigDecimal balance;
    private final LocalDateTime date;
    private List<Operation> operations = new ArrayList<>();

    public Statement(UUID accountId, LocalDateTime date) {
        this.accountId = accountId;
        this.date = date;
    }

    public Statement(UUID accountId, LocalDateTime date,  List<Operation> operations) {
        this.accountId = accountId;
        this.date = date;
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

    public LocalDateTime getDate() {
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
