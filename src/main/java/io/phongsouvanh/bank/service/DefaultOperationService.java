package io.phongsouvanh.bank.service;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.exceptions.OutOfBalanceException;
import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationDao;
import io.phongsouvanh.bank.operations.OperationType;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class DefaultOperationService implements OperationService {

    private final OperationDao operationDao;
    private final Clock clock;

    public DefaultOperationService(OperationDao operationDao, Clock clock) {
        this.operationDao = operationDao;
        this.clock = clock;
    }

    @Override
    public Operation deposit(UUID accountId, Amount amount) {
        BigDecimal oldBalance = operationDao.getAccountBalanceById(accountId);
        BigDecimal newBalance = oldBalance.add(amount.getValue());

        Operation currentOperation = new Operation(
                accountId, OperationType.DEPOSIT, amount, LocalDateTime.now(clock), newBalance
        );
        return operationDao.save(currentOperation);
    }

    @Override
    public Operation withdrawal(UUID accountId, Amount amount) throws OutOfBalanceException {
        BigDecimal oldBalance = operationDao.getAccountBalanceById(accountId);
        BigDecimal newBalance = oldBalance.subtract(amount.getValue());

        if(newBalance.compareTo(BigDecimal.ZERO) <= 0) throw new OutOfBalanceException();

        Operation currentOperation = new Operation(
                accountId, OperationType.WITHDRAWAL, amount, LocalDateTime.now(clock), newBalance
        );
        return operationDao.save(currentOperation);
    }

}
