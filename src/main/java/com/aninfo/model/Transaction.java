package com.aninfo.model;

import com.aninfo.exceptions.InvalidValueException;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Model implementation of a transaction.
 */
@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long number;

  private Long cbu;
  private Date date;
  private Double amount;
  private boolean active;
  private TransactionType type;

  public static final boolean ACTIVE = true;
  public static final boolean INACTIVE = false;

  public Transaction() {}

  /**
   * Private constructor.
   *
   * @param Long number the transaction number.
   * @param TransactionType type the transaction type.
   * @param Date date the transaction date.
   * @param Double amount the transaction amount.
   * @param boolean active if the transaction is active.
   */
  private Transaction(Long cbu, TransactionType type, Date date, Double amount, boolean active) {
    this.cbu = cbu;
    this.type = type;
    this.date = date;
    this.amount = amount;
    this.active = active;
  }

  /**
   * Private constructor.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @param TransactionType type the transaction type.
   * @param Date date the transaction date.
   * @param Double amount the transaction amount.
   * @param boolean active if the transaction is active.
   */
  private Transaction(
      Long cbu, Long number, TransactionType type, Date date, Double amount, boolean active) {
    this.cbu = cbu;
    this.number = number;
    this.type = type;
    this.date = date;
    this.amount = amount;
    this.active = active;
  }

  /**
   * Constructor.
   *
   * @param Long cbu the account CBU.
   * @param TransactionType type the transaction type.
   * @param Double amount the transaction amount.
   */
  public static Transaction Create(Long cbu, TransactionType type, Double amount)
      throws InvalidValueException {

    switch (type) {
      case Deposit:
        if (amount <= 0) throw new InvalidValueException();
        break;

      case Withdraw:
        break;

      default:
        break;
    }

    Date date = new Date();
    boolean active = true;

    Transaction transaction = new Transaction(cbu, type, date, amount, active);

    return transaction;
  }

  /**
   * Constructor for importation.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @param TransactionType type the transaction type.
   * @param Date date the transaction date.
   * @param Double amount the transaction amount.
   * @param boolean active if the transaction is active.
   */
  public static Transaction CreateFromImport(
      Long cbu, Long number, TransactionType type, Date date, Double amount, boolean active) {
    Transaction transaction = new Transaction(cbu, number, type, date, amount, active);

    return transaction;
  }

  /**
   * Return the transaction number.
   *
   * @return Long
   */
  public Long getNumber() {
    return number;
  }

  /**
   * Return the transaction amount.
   *
   * @return Double
   */
  public Double getAmount() {
    return amount;
  }

  /**
   * Return the transaction date.
   *
   * @return Date
   */
  public Date getDate() {
    return date;
  }

  /**
   * Return the transaction CBU.
   *
   * @return Long
   */
  public Long getCbu() {
    return cbu;
  }

  /**
   * Return if the transaction is deleted.
   *
   * @return boolean <code>true</code> if the transaction is deleted, otherwise <code>false</code>.
   */
  public Boolean isActive() {
    return active;
  }

  /**
   * Delete logically.
   *
   * @return void
   */
  public void delete() {
    active = false;
  }

  /**
   * Restore logically.
   *
   * @return void
   */
  public void restore() {
    active = true;
  }

  /**
   * Return the transaction type.
   *
   * @return TransactionType
   */
  public TransactionType getType() {
    return type;
  }
}
