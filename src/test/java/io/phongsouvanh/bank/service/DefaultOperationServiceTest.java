package io.phongsouvanh.bank.service;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.exceptions.NegativeAmountException;
import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationDao;
import io.phongsouvanh.bank.operations.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultOperationServiceTest {

    @Mock
    private OperationDao operationDao;
    private final Clock clock = Clock.fixed(Instant.parse("2022-10-25T15:30:00Z"), ZoneId.of("UTC"));
    private DefaultOperationService service;

    @BeforeEach
    void setUp() {
        this.service = new DefaultOperationService(operationDao, clock);
    }

    @Test
    @DisplayName("should operate a deposit on an account given a valid amount")
    void shouldOperateDepositGivenValidAmount() throws Exception {
        // Arrange
        final UUID accountId = UUID.randomUUID();
        final Amount amount = new Amount(new BigDecimal(10));
        final Operation savedOperation = new Operation(
                accountId, OperationType.DEPOSIT, amount, LocalDateTime.now(clock), new BigDecimal(20)
        );
        final Operation expectedOperation = new Operation(
                accountId, OperationType.DEPOSIT, amount, LocalDateTime.now(clock), new BigDecimal(20)
        );

        when(operationDao.getAccountBalanceById(accountId)).thenReturn(new BigDecimal(10));
        when(operationDao.save(any())).thenReturn(savedOperation);

        // Act
        final Operation operation = service.deposit(accountId, amount);

        // Assert
        assertEquals(operation, expectedOperation);
        final InOrder orderVerifier = inOrder(operationDao);
        orderVerifier.verify(operationDao).getAccountBalanceById(accountId);
        orderVerifier.verify(operationDao).save(savedOperation);
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("should not operate a deposit on an account given an invalid amount")
    void shouldNotOperateDepositGivenInvalidAmount() throws NegativeAmountException {
        // Arrange
        final UUID accountId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NegativeAmountException.class, () -> {
            service.deposit(accountId, new Amount(new BigDecimal(-10)));
        });
        verifyNoInteractions(operationDao);
    }

}