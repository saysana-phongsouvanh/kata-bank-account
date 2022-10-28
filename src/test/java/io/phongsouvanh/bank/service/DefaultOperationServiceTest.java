package io.phongsouvanh.bank.service;

import io.phongsouvanh.bank.account.Amount;
import io.phongsouvanh.bank.exceptions.NegativeAmountException;
import io.phongsouvanh.bank.exceptions.OutOfBalanceException;
import io.phongsouvanh.bank.operations.Operation;
import io.phongsouvanh.bank.operations.OperationDao;
import io.phongsouvanh.bank.operations.OperationType;
import io.phongsouvanh.bank.statements.StatementFormatter;
import io.phongsouvanh.bank.statements.StatementPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultOperationServiceTest {

    @Mock
    private OperationDao operationDao;

    @Mock
    private StatementFormatter formatter;

    @Mock
    private StatementPrinter printer;

    private final static Clock clock = Clock.fixed(Instant.parse("2022-10-25T15:30:00Z"), ZoneId.of("UTC"));
    private DefaultOperationService service;

    @BeforeEach
    void setUp() {
        this.service = new DefaultOperationService(operationDao, clock, formatter, printer);
    }

    @Nested
    @DisplayName("deposit tests")
    class DepositTests {
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
        void shouldNotOperateDepositGivenInvalidAmount() {
            // Arrange
            final UUID accountId = UUID.randomUUID();

            // Act & Assert
            assertThrows(NegativeAmountException.class, () -> service.deposit(accountId, new Amount(new BigDecimal(-10))));
            verifyNoInteractions(operationDao);
        }
    }

    @Nested
    @DisplayName("withdraw tests")
    class WithdrawTests {
        @Test
        @DisplayName("should operate a withdraw on an account given a valid amount")
        void shouldOperateWithdrawGivenValidAmount() throws Exception {
            // Arrange
            final UUID accountId = UUID.randomUUID();
            final Amount amount = new Amount(new BigDecimal(5));
            final Operation savedOperation = new Operation(
                    accountId, OperationType.WITHDRAWAL, amount, LocalDateTime.now(clock), new BigDecimal(5)
            );
            final Operation expectedOperation = new Operation(
                    accountId, OperationType.WITHDRAWAL, amount, LocalDateTime.now(clock), new BigDecimal(5)
            );

            when(operationDao.getAccountBalanceById(accountId)).thenReturn(new BigDecimal(10));
            when(operationDao.save(any())).thenReturn(savedOperation);

            // Act
            final Operation operation = service.withdraw(accountId, amount);

            // Assert
            assertEquals(operation, expectedOperation);
            final InOrder orderVerifier = inOrder(operationDao);
            orderVerifier.verify(operationDao).getAccountBalanceById(accountId);
            orderVerifier.verify(operationDao).save(savedOperation);
            orderVerifier.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should not operate a withdraw on an account given an invalid amount")
        void shouldNotOperateWithdrawGivenInvalidAmount() {
            // Arrange
            final UUID accountId = UUID.randomUUID();

            // Act & Assert
            assertThrows(NegativeAmountException.class, () -> service.withdraw(accountId, new Amount(new BigDecimal(-5))));
            verifyNoInteractions(operationDao);
        }

        @Test
        @DisplayName("should not operate a withdraw on account since it will make account out of balance")
        void shouldNotOperateWithdrawSinceOutOfBalance() {
            final UUID accountId = UUID.randomUUID();
            when(operationDao.getAccountBalanceById(accountId)).thenReturn(new BigDecimal(5));

            assertThrows(OutOfBalanceException.class, () -> service.withdraw(accountId, new Amount(new BigDecimal(20))));
            verify(operationDao).getAccountBalanceById(accountId);
            verifyNoMoreInteractions(operationDao);
        }
    }

    @Nested
    @DisplayName("print account statement tests")
    class PrintAccountStatementTests {
        @Test
        @DisplayName("should print an account statement for the specified account with operations")
        void shouldPrintWithOperations() throws Exception {
            // Arrange
            final UUID accountId = UUID.randomUUID();
            final List<Operation> operations = Arrays.asList(
                new Operation(accountId, OperationType.DEPOSIT, new Amount(new BigDecimal(10000)), LocalDateTime.now(clock).minusMonths(4).minusDays(8), new BigDecimal(10000)),
                new Operation(accountId, OperationType.WITHDRAWAL, new Amount(new BigDecimal(100)), LocalDateTime.now(clock).plusMinutes(5).minusDays(5), new BigDecimal(9900)),
                new Operation(accountId, OperationType.WITHDRAWAL, new Amount(new BigDecimal(7000)), LocalDateTime.now(clock).plusMinutes(6), new BigDecimal(2900))
            );
            when(operationDao.getAllAccountOperationsById(accountId)).thenReturn(operations);

            // Act
            service.printAccountStatement(accountId);

            // Assert
            final InOrder orderVerifier = inOrder(operationDao, formatter, printer);
            orderVerifier.verify(operationDao).getAllAccountOperationsById(accountId);
            orderVerifier.verify(formatter).format(any());
            orderVerifier.verify(printer).print(any());
            orderVerifier.verifyNoMoreInteractions();
        }

    }

}