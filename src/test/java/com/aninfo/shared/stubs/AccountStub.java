package com.aninfo.shared.stubs;

import com.aninfo.model.Account;

/**
 * Stubs related to account.
 *
 * @see Account
 */
public class AccountStub {

  /**
   * Return an account.
   *
   * @return Account
   */
  public static Account create() {
    Account account = new Account();
    return account;
  }
}
