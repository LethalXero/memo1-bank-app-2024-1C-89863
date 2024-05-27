package com.aninfo.repository;

import com.aninfo.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findAccountByCbu(Long cbu);

	@NonNull
	List<Account> findAll();
}
