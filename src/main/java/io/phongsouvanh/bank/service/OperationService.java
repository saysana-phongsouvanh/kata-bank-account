package io.phongsouvanh.bank.service;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.operations.Operation;

import java.util.UUID;

public interface OperationService {

    Operation deposit(UUID accountId, Amount amount);

}
