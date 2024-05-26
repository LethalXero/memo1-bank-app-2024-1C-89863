package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.repository.AccountRepository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  @Autowired private AccountRepository accountRepository;

  public Account createAccount(Account account) {
    return accountRepository.save(account);
  }

  public Collection<Account> getAccounts() {
    return accountRepository.findAll();
  }

  public Optional<Account> findById(Long cbu) {
    return accountRepository.findById(cbu);
  }

  public void save(Account account) {
    accountRepository.save(account);
  }

  public void deleteById(Long cbu) {
    accountRepository.deleteById(cbu);
  }

  @Transactional
  public Account withdraw(Long cbu, Double sum) {
    Account account = accountRepository.findAccountByCbu(cbu);

    if (account.getBalance() < sum) {
      throw new InsufficientFundsException("Insufficient funds");
    }

    account.setBalance(account.getBalance() - sum);
    accountRepository.save(account);

    return account;
  }

  /**
   * Make a deposit.
   *
   * @param cbu the account CBU.
   * @param sum the sum.
   * @return Account
   */
  @Transactional
  public Account deposit(Long cbu, Double sum) {
    AtomicReference<Double> sumRef = new AtomicReference<>(sum);

    Account account = accountRepository.findAccountByCbu(cbu);

    try {

      Map<Predicate<Double>, IOperation> actions =
          new HashMap<>(
              Map.of(
                  value -> (value <= 0), new DepositInvalidValue(), // TODO: extract value to config
                  value -> (value >= 2000),
                      new ApplyBankAccountPromotion(10.0, 500.0) // TODO: extract value to config
                  ));

      for (Map.Entry<Predicate<Double>, IOperation> entry : actions.entrySet()) {
        if (entry.getKey().test(sum)) entry.getValue().execute(sumRef);
      }

      account.setBalance(account.getBalance() + sumRef.get());
      accountRepository.save(account);

    } catch (Exception e) {
      if (e instanceof DepositNegativeSumException)
        throw new DepositNegativeSumException(e.getMessage());
      // TODO: catch other exceptions
    }

    return account;
  }
}
