package com.aninfo;

import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.model.TransactionType;
import com.aninfo.service.AccountService;
import com.aninfo.service.TransactionService;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@SpringBootApplication
@EnableSwagger2
public class Memo1BankApp {

  @Autowired private AccountService accountService;

  @Autowired private TransactionService transactionService;

  public static void main(String[] args) {
    SpringApplication.run(Memo1BankApp.class, args);
  }

  @PostMapping("/accounts")
  @ResponseStatus(HttpStatus.CREATED)
  public Account createAccount(@RequestBody Account account) {
    return accountService.createAccount(account);
  }

  @GetMapping("/accounts")
  public Collection<Account> getAccounts() {
    return accountService.getAccounts();
  }

  @GetMapping("/accounts/{cbu}")
  public ResponseEntity<Account> getAccount(@PathVariable Long cbu) {
    Optional<Account> accountOptional = accountService.findById(cbu);
    return ResponseEntity.of(accountOptional);
  }

  @PutMapping("/accounts/{cbu}")
  public ResponseEntity<Account> updateAccount(
      @RequestBody Account account, @PathVariable Long cbu) {
    Optional<Account> accountOptional = accountService.findById(cbu);

    if (!accountOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    account.setCbu(cbu);
    accountService.save(account);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/accounts/{cbu}")
  public void deleteAccount(@PathVariable Long cbu) {
    accountService.deleteById(cbu);
  }

  @PutMapping("/accounts/{cbu}/withdraw")
  public Account withdraw(@PathVariable Long cbu, @RequestParam Double sum) {
    return accountService.withdraw(cbu, sum);
  }

  @PutMapping("/accounts/{cbu}/deposit")
  public Account deposit(@PathVariable Long cbu, @RequestParam Double sum) {
    return accountService.deposit(cbu, sum);
  }

  /**
   * Endpoint to create transaction.
   *
   * @param Long cbu the account CBU.
   * @param TransactionType type the transaction type.
   * @param Double sum the transaction sum.
   * @return Transaction|null the created transaction or <code>null</code> if there was an issue.
   */
  @PostMapping("/accounts/{cbu}/transactions")
  public Transaction createTransaction(
      @PathVariable Long cbu, @RequestParam TransactionType type, @RequestParam Double sum) {
    return transactionService.create(cbu, type, sum);
  }

  /**
   * Endpoint to get all transactions.
   *
   * @param Long cbu the account CBU.
   * @return Collection<Transaction>
   */
  @GetMapping("/accounts/{cbu}/transactions")
  public Collection<Transaction> getTransactions(@PathVariable Long cbu) {
    return transactionService.findByCbu(cbu);
  }

  /**
   * Endpoint to get a transaction.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @return ResponseEntity<Transaction>
   */
  @GetMapping("/accounts/{cbu}/transactions/{number}")
  public ResponseEntity<Transaction> getTransaction(
      @PathVariable Long cbu, @PathVariable Long number) {
    Optional<Transaction> transactionOptional = transactionService.find(cbu, number);
    return ResponseEntity.of(transactionOptional);
  }

  /**
   * Endpoint to delete a transaction.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @return ResponseEntity<Transaction>
   */
  @DeleteMapping("/accounts/{cbu}/transactions/{number}")
  public ResponseEntity<Transaction> deleteTransaction(
      @PathVariable Long cbu, @PathVariable Long number) {

    Optional<Transaction> transaction = transactionService.delete(cbu, number);

    return ResponseEntity.of(transaction);
  }

  /**
   * Endpoint to restore a deleted transaction.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @return ResponseEntity<Transaction>
   */
  @PutMapping("/accounts/{cbu}/transactions/{number}")
  public ResponseEntity<Transaction> restoreTransaction(
      @PathVariable Long cbu, @PathVariable Long number) {
    Optional<Transaction> transaction = transactionService.restore(cbu, number);

    return ResponseEntity.of(transaction);
  }

  @Bean
  public Docket apiDocket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build();
  }
}
