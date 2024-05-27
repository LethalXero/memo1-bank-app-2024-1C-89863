package com.aninfo.service;

import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.model.TransactionType;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import java.util.Collection;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service related to transaction.
 *
 * @see Transaction
 */
@Service
public class TransactionService {

  @Autowired private TransactionRepository transactionRepository;
  @Autowired private AccountRepository accountRepository;
  @Autowired private AccountService accountService;

  @Autowired
  public TransactionService(
      TransactionRepository transactionRepository,
      AccountRepository accountRepository,
      AccountService accountService) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
    this.accountService = accountService;
  }

  /**
   * Create a transaction.
   *
   * @param Transaction transaction the transaction to create.
   * @return Transaction|null the created transaction or <code>null</code> if there was an issue.
   */
  @Transactional
  public Transaction create(Long cbu, TransactionType type, Double sum) {
    Account account = accountRepository.findAccountByCbu(cbu);

    if (account == null) return null;

    switch (type) {
      case Deposit:
        try {
          accountService.deposit(cbu, sum);

          Transaction transaction = Transaction.Create(cbu, type, sum);

          return transactionRepository.save(transaction);

        } catch (Exception e) {
          // TODO: Add exception handling
        }

      case Withdraw:
        try {
          accountService.withdraw(cbu, sum);

          Transaction transaction = Transaction.Create(cbu, type, sum);

          return transactionRepository.save(transaction);

        } catch (Exception e) {
          // TODO: Add exception handling
        }

      default:
        break;
    }

    return null;
  }

  /**
   * Find a transaction.
   *
   * @param cbu the account CBU.
   * @param number the transaction number.
   * @return ResponseEntity<Transaction>
   */
  public Optional<Transaction> find(Long cbu, Long number) {
    return transactionRepository.findByCbuAndNumber(cbu, number);
  }

  /**
   * Find the transactions related to the account CBU.
   *
   * @param Long cbu Transaction account.
   * @return Collection<Transaction> the transactions related to the account CBU.
   */
  public Collection<Transaction> findByCbu(Long cbu) {
    return transactionRepository.findByCbu(cbu);
  }

  /**
   * Delete a transaction related to an account.
   *
   * @param Long cbu Account CBU.
   * @param Long number Transaction number.
   * @return Optional<Transaction>
   */
  @Transactional
  public Optional<Transaction> delete(Long cbu, Long number) {
    Optional<Transaction> transaction =
        transactionRepository.findByCbuAndNumberAndActive(cbu, number, Transaction.ACTIVE);

    Account account = accountRepository.findAccountByCbu(cbu);

    if (transaction.isPresent() && account != null) {
      transaction.get().delete();
      transactionRepository.save(transaction.get());

      account.remove(transaction.get());
      accountRepository.save(account);

      return transaction;
    }

    if (account == null) return Optional.empty();

    return transaction;
  }

  /**
   * Restore a transaction related to an account.
   *
   * @param Long cbu Account CBU.
   * @param Long number Transaction number.
   * @return Optional<Transaction>
   */
  @Transactional
  public Optional<Transaction> restore(Long cbu, Long number) {
    Optional<Transaction> transaction =
        transactionRepository.findByCbuAndNumberAndActive(cbu, number, Transaction.INACTIVE);
    Account account = accountRepository.findAccountByCbu(cbu);

    if (transaction.isPresent() && account != null) {
      transaction.get().restore();
      transactionRepository.save(transaction.get());

      account.add(transaction.get());
      accountRepository.save(account);

      return transaction;
    }

    if (account == null) return Optional.empty();

    return transaction;
  }
}
