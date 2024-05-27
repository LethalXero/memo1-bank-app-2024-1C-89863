package com.aninfo.shared.stubs;

import com.aninfo.model.Transaction;
import com.aninfo.model.TransactionType;
import com.aninfo.shared.utils.RandomGenerator;
import java.util.Date;

/**
 * Stubs related to Transaction.
 *
 * @see Transaction
 */
public class TransactionStub {

  private static TransactionType type;
  private static boolean deleted;

  /**
   * Return a transaction.
   *
   * @return Transaction
   */
  private static Transaction create() {
    Long cbu = RandomGenerator.generateBetween(1L, 100L);
    Long number = RandomGenerator.generateBetween(101L, 200L);
    Date date = new Date();
    Double amount = RandomGenerator.generateBetween(100.0, 200.0);

    Transaction transaction =
        Transaction.CreateFromImport(cbu, number, type, date, amount, deleted);

    return transaction;
  }

  /**
   * Return a transaction.
   *
   * @return Transaction
   */
  public static Transaction createAnActiveDeposit() {
    deleted = false;
    type = TransactionType.Deposit;

    return create();
  }

  /**
   * Return a transaction.
   *
   * @return Transaction
   */
  public static Transaction createAnActiveWithdraw() {
    deleted = false;
    type = TransactionType.Withdraw;

    return create();
  }

  /**
   * Return a transaction.
   *
   * @return Transaction
   */
  public static Transaction createAnInactiveDeposit() {
    deleted = true;
    type = TransactionType.Deposit;

    return create();
  }

  /**
   * Return a transaction.
   *
   * @return Transaction
   */
  public static Transaction createAnInactiveWithdraw() {
    deleted = true;
    type = TransactionType.Withdraw;

    return create();
  }
}
