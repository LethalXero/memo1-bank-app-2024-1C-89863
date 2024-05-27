package com.aninfo.model;

import javax.persistence.*;

@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long cbu;

  private Double balance;

  public Account() {
    this.balance = 0.0;
  }

  public Account(Double balance) {
    this.balance = balance;
  }

  public Long getCbu() {
    return cbu;
  }

  public void setCbu(Long cbu) {
    this.cbu = cbu;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  /**
   * Add transaction.
   *
   * @param Transaction transaction the transaction to add.
   * @return void
   */
  public void add(Transaction transaction) {
    switch (transaction.getType()) {
      case Deposit:
        this.balance += transaction.getAmount();
        break;

      case Withdraw:
        this.balance -= transaction.getAmount();
        break;

      default:
        break;
    }
  }

  /**
   * Remove transaction.
   *
   * @param Transaction transaction the transaction to remove.
   * @return void
   */
  public void remove(Transaction transaction) {
    switch (transaction.getType()) {
      case Deposit:
        this.balance -= transaction.getAmount();
        break;

      case Withdraw:
        this.balance += transaction.getAmount();
        break;

      default:
        break;
    }
  }
}
