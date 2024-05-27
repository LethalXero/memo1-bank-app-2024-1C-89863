package com.aninfo.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.aninfo.shared.utils.RandomGenerator;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests related to transaction model.
 *
 * @see Transaction
 */
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = {Memo1BankApp.class})
public class TransactionTest {

  private TransactionType type;

  @Before
  public void setUp() {
    this.type = TransactionType.Deposit;
  }

  @Test
  public void
      when_transaction_is_created_from_import_with_valid_values_expect_does_not_to_throw_exception() {

    Long cbu = RandomGenerator.generateBetween(1L, 100L);
    Long number = RandomGenerator.generateBetween(101L, 200L);
    Date date = new Date();
    Double amount = RandomGenerator.generateBetween(100.0, 200.0);
    boolean active = true;

    Transaction transaction = Transaction.CreateFromImport(cbu, number, type, date, amount, active);

    assertEquals(cbu, transaction.getCbu());
    assertEquals(number, transaction.getNumber());
    assertEquals(type, transaction.getType());
    assertEquals(date, transaction.getDate());
    assertEquals(amount, transaction.getAmount());
    assertEquals(active, transaction.isActive());
  }

  @Test
  public void when_transaction_is_created_with_valid_values_expect_does_not_to_throw_exception() {

    Long cbu = RandomGenerator.generateBetween(1L, 100L);
    Long number = null;
    Double amount = RandomGenerator.generateBetween(100.0, 200.0);
    boolean active = true;

    Transaction transaction = null;
    try {
      transaction = Transaction.Create(cbu, type, amount);
    } catch (Exception e) {
      assertTrue(false);
    }

    assertEquals(cbu, transaction.getCbu());
    assertEquals(number, transaction.getNumber());
    assertEquals(type, transaction.getType());
    assertEquals(amount, transaction.getAmount());
    assertEquals(active, transaction.isActive());
  }
}
