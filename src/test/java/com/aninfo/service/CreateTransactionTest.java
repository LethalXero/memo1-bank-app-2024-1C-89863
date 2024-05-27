package com.aninfo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.model.TransactionType;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import com.aninfo.shared.stubs.AccountStub;
import com.aninfo.shared.stubs.TransactionStub;
import com.aninfo.shared.utils.RandomGenerator;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests related to create transaction use case.
 *
 * @see TransactionService#create(Long, TransactionType, Double)
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = {Memo1BankApp.class})
public class CreateTransactionTest {

  private TransactionType type;
  private TransactionRepository transactionRepositoryMock;
  private AccountRepository accountRepositoryMock;
  private AccountService accountServiceMock;
  private Account accountFound;
  private Account noneAccount;
  private boolean accountDepositCalled;
  private boolean accountWithdrawCalled;
  private Long cbu;
  private Long cbuCalled;
  private Double amount;
  private Double amountCalled;

  @Before
  public void setUp() {
    this.noneAccount = null;
    this.type = TransactionType.Deposit;
    this.cbu = RandomGenerator.generateBetween(100L, 200L);
    this.amount = RandomGenerator.generateBetween(201.0, 300.0);
    this.accountRepositoryMock = mock(AccountRepository.class);
    this.transactionRepositoryMock = mock(TransactionRepository.class);
    this.accountServiceMock = mock(AccountService.class);
  }

  @Test
  public void when_account_is_not_found_expect_to_return_null() {
    when(this.accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(noneAccount);

    Transaction response = this.executeCreateTransaction();

    assertNull(response);
  }

  @Test
  public void when_transaction_is_deposit_expect_to_return_the_transaction() {
    this.type = TransactionType.Deposit;

    Account accountFound = AccountStub.create();
    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(transactionRepositoryMock.save(any(Transaction.class)))
        .thenAnswer(
            invocation -> {
              Transaction savedTransaction = invocation.getArgument(0);
              return savedTransaction;
            });

    Transaction translationResponse = executeCreateTransaction();

    assertEquals(this.cbu, translationResponse.getCbu());
    assertEquals(this.type, translationResponse.getType());
    assertEquals(this.amount, translationResponse.getAmount());
  }

  @Test
  public void when_transaction_is_withdraw_expect_to_return_the_transaction() {
    this.type = TransactionType.Withdraw;

    Account accountFound = AccountStub.create();
    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(transactionRepositoryMock.save(any(Transaction.class)))
        .thenAnswer(
            invocation -> {
              Transaction savedTransaction = invocation.getArgument(0);
              return savedTransaction;
            });

    Transaction translationResponse = executeCreateTransaction();

    assertEquals(this.cbu, translationResponse.getCbu());
    assertEquals(this.type, translationResponse.getType());
    assertEquals(this.amount, translationResponse.getAmount());
  }

  @Test
  public void when_transaction_is_deposit_expect_account_deposit_to_be_successfully_called() {
    this.type = TransactionType.Deposit;
    this.accountDepositCalled = false;

    initTransactionAndAccountRepositoriesMock();
    when(accountServiceMock.deposit(anyLong(), anyDouble()))
        .thenAnswer(
            invocation -> {
              this.accountDepositCalled = true;
              this.cbuCalled = invocation.getArgument(0);
              this.amountCalled = invocation.getArgument(1);

              return null;
            });

    executeCreateTransaction();

    assertTrue(this.accountDepositCalled);
    assertEquals(this.cbu, this.cbuCalled);
    assertEquals(this.amount, this.amountCalled);
  }

  @Test
  public void when_transaction_is_deposit_and_amount_is_negative_expect_to_return_null() {
    Transaction transactionExpected = null;

    this.type = TransactionType.Deposit;
    this.amount = -10.0;

    initTransactionAndAccountRepositoriesMock();

    Transaction response = executeCreateTransaction();

    assertEquals(transactionExpected, response);
  }

  @Test
  public void when_transaction_is_withdraw_expect_account_withdraw_to_be_successfully_called() {
    this.type = TransactionType.Withdraw;
    this.accountWithdrawCalled = false;

    initTransactionAndAccountRepositoriesMock();
    when(accountServiceMock.withdraw(anyLong(), anyDouble()))
        .thenAnswer(
            invocation -> {
              this.accountWithdrawCalled = true;
              this.cbuCalled = invocation.getArgument(0);
              this.amountCalled = invocation.getArgument(1);

              return null;
            });

    executeCreateTransaction();

    assertTrue(this.accountWithdrawCalled);
    assertEquals(this.cbu, this.cbuCalled);
    assertEquals(this.amount, this.amountCalled);
  }

  @Test
  public void
      when_transaction_is_withdraw_and_amount_is_more_than_balance_account_expect_to_return_null() {
    Transaction transactionExpected = null;

    this.type = TransactionType.Withdraw;
    initTransactionAndAccountRepositoriesMock();
    this.amount = -this.accountFound.getBalance() - 10.0;

    Transaction response = executeCreateTransaction();

    assertEquals(transactionExpected, response);
  }

  /** Init the transaction and account repository mock. */
  private void initTransactionAndAccountRepositoriesMock() {
    this.accountFound = AccountStub.create();
    Transaction transactionFound = TransactionStub.createAnActiveDeposit();
    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(this.accountFound);
    when(transactionRepositoryMock.findByCbuAndNumberAndActive(anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.of(transactionFound));
  }

  /**
   * Execute delete transaction.
   *
   * @return Transaction the response.
   */
  private Transaction executeCreateTransaction() {
    TransactionService transactionService =
        new TransactionService(
            this.transactionRepositoryMock, this.accountRepositoryMock, this.accountServiceMock);
    Transaction response = transactionService.create(this.cbu, this.type, this.amount);

    return response;
  }
}
