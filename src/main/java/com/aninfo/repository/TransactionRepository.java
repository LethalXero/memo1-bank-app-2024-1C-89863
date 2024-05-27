package com.aninfo.repository;

import com.aninfo.model.Transaction;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

/**
 * Repository related to Transaction
 *
 * @see Transaction
 */
@RepositoryRestResource
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

  /**
   * Return the transactions by CBU.
   *
   * @param Long cbu the account CBU.
   * @return Collection<Transaction>
   */
  @NonNull
  public Collection<Transaction> findByCbu(Long cbu);

  /**
   * Return the transaction.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @param boolean active active or inactive transaction.
   * @return Optional<Transaction>
   */
  public Optional<Transaction> findByCbuAndNumberAndActive(Long cbu, Long number, boolean active);

  /**
   * Return the transaction.
   *
   * @param Long cbu the account CBU.
   * @param Long number the transaction number.
   * @return Optional<Transaction>
   */
  public Optional<Transaction> findByCbuAndNumber(Long cbu, Long number);
}
