package io.phongsouvanh.bank.operations;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OperationDao {

    Operation save(Operation operation);
    BigDecimal getAccountBalanceById(UUID accountId);
    List<Operation> getAllAccountOperationsById(UUID accountId);
}
