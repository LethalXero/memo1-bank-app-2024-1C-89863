package com.aninfo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import com.aninfo.shared.stubs.AccountStub;
import com.aninfo.shared.stubs.TransactionStub;
import com.aninfo.shared.utils.RandomGenerator;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests related to restore transaction use case.
 *
 * @see TransactionService#restore(Long, Long)
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = {Memo1BankApp.class})
public class RestoreTransactionTest {

  private Long cbu;
  private Long transactionNumber;
  private TransactionRepository transactionRepositoryMock;
  private AccountRepository accountRepositoryMock;
  private Transaction noneTransaction;
  private Account noneAccount;
  private AccountService accountServiceMock;

  @Before
  public void setUp() {
    this.noneAccount = null;
    this.noneTransaction = null;
    this.cbu = RandomGenerator.generateBetween(100L, 200L);
    this.transactionNumber = RandomGenerator.generateBetween(201L, 300L);
    this.accountRepositoryMock = mock(AccountRepository.class);
    this.transactionRepositoryMock = mock(TransactionRepository.class);
    this.accountServiceMock = mock(AccountService.class);
  }

  @Test
  public void when_transaction_is_not_found_expect_to_return_null() {
    Transaction responseExpected = null;

    Account accountFound = AccountStub.create();
    when(this.accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(this.transactionRepositoryMock.findByCbuAndNumberAndActive(
            anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.ofNullable(noneTransaction));

    Transaction response = executeRestoreTransaction();

    assertEquals(responseExpected, response);
  }

  @Test
  public void when_account_is_not_found_expect_to_return_null() {
    Transaction responseExpected = null;

    Transaction transactionFound = TransactionStub.createAnInactiveDeposit();
    when(this.transactionRepositoryMock.findByCbuAndNumberAndActive(
            anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.ofNullable(transactionFound));
    when(this.accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(noneAccount);

    Transaction response = executeRestoreTransaction();

    assertEquals(responseExpected, response);
  }

  @Test
  public void when_transaction_and_account_are_found_expect_to_return_the_transaction() {
    Transaction transactionExpected = TransactionStub.createAnInactiveDeposit();

    Account accountFound = AccountStub.create();
    Transaction transactionFound = transactionExpected;
    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(transactionRepositoryMock.findByCbuAndNumberAndActive(anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.of(transactionFound));

    Transaction transactionResponse = executeRestoreTransaction();

    assertEquals(transactionExpected, transactionResponse);
  }

  @Test
  public void when_transaction_and_account_are_found_expect_to_active_the_transaction() {
    Account accountFound = AccountStub.create();
    Transaction transactionFound = TransactionStub.createAnInactiveDeposit();

    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(transactionRepositoryMock.findByCbuAndNumberAndActive(anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.of(transactionFound));

    Transaction transactionResponse = executeRestoreTransaction();

    assertTrue(transactionResponse.isActive());
  }

  @Test
  public void
      when_deposit_transaction_and_account_are_found_expect_to_update_the_account_balance() {
    Account accountFound = AccountStub.create();
    Transaction transactionFound = TransactionStub.createAnInactiveDeposit();

    Double expectedBalance = accountFound.getBalance() + transactionFound.getAmount();

    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(transactionRepositoryMock.findByCbuAndNumberAndActive(anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.of(transactionFound));

    executeRestoreTransaction();

    assertEquals(expectedBalance, accountFound.getBalance());
  }

  @Test
  public void
      when_withdraw_transaction_and_account_are_found_expect_to_update_the_account_balance() {
    Transaction transactionFound = TransactionStub.createAnInactiveWithdraw();
    Account accountFound = AccountStub.create();

    Double expectedBalance = accountFound.getBalance() - transactionFound.getAmount();

    when(accountRepositoryMock.findAccountByCbu(anyLong())).thenReturn(accountFound);
    when(transactionRepositoryMock.findByCbuAndNumberAndActive(anyLong(), anyLong(), anyBoolean()))
        .thenReturn(Optional.of(transactionFound));

    executeRestoreTransaction();

    assertEquals(expectedBalance, accountFound.getBalance());
  }

  /**
   * Execute restore transaction.
   *
   * @return Transaction the response with the transaction or <code>null</code>.
   */
  private Transaction executeRestoreTransaction() {
    TransactionService transactionService =
        new TransactionService(
            this.transactionRepositoryMock, this.accountRepositoryMock, this.accountServiceMock);
    Optional<Transaction> response = transactionService.restore(this.cbu, this.transactionNumber);

    if (response.isPresent()) return response.get();

    return null;
  }
}
