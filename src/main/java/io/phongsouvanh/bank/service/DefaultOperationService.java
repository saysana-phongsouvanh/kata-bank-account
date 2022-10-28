package io.phongsouvanh.bank.service;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.exceptions.OutOfBalanceException;
import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationDao;
import io.phongsouvanh.bank.operations.OperationType;
import io.phongsouvanh.bank.statements.Statement;
import io.phongsouvanh.bank.statements.StatementFormatter;
import io.phongsouvanh.bank.statements.StatementPrinter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DefaultOperationService implements OperationService {

    private final OperationDao operationDao;
    private final StatementFormatter formatter;
    private final StatementPrinter printer;
    private final Clock clock;

    public DefaultOperationService(OperationDao operationDao, Clock clock, StatementFormatter formatter, StatementPrinter printer) {
        this.operationDao = operationDao;
        this.clock = clock;
        this.formatter = formatter;
        this.printer = printer;
    }

    @Override
    public Operation deposit(UUID accountId, Amount amount) {
        BigDecimal oldBalance = Optional.of(operationDao.getAccountBalanceById(accountId)).orElse(BigDecimal.ZERO);
        BigDecimal newBalance = oldBalance.add(amount.getValue());

        Operation currentOperation = new Operation(
                accountId, OperationType.DEPOSIT, amount, LocalDateTime.now(clock), newBalance
        );
        return operationDao.save(currentOperation);
    }

    @Override
    public Operation withdraw(UUID accountId, Amount amount) throws OutOfBalanceException {
        BigDecimal oldBalance = Optional.of(operationDao.getAccountBalanceById(accountId)).orElse(BigDecimal.ZERO);
        BigDecimal newBalance = oldBalance.subtract(amount.getValue());

        if(newBalance.compareTo(BigDecimal.ZERO) <= 0) throw new OutOfBalanceException();

        Operation currentOperation = new Operation(
                accountId, OperationType.WITHDRAWAL, amount, LocalDateTime.now(clock), newBalance
        );
        return operationDao.save(currentOperation);
    }

    @Override
    public void printAccountStatement(UUID accountId){
        List<Operation> operations = operationDao.getAllAccountOperationsById(accountId);
        Statement statement = new Statement(accountId, LocalDate.now(clock), operations);
        printer.print(formatter.format(statement));
    }

}
