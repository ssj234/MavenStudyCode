package com.shisj.mvnstudy.account.persist;

public interface AccountPersistService {

	Account createAccount(Account account);
	Account readAccount(String id);
	Account updateAccount(Account account);
	void deleteAccount(Account account);
}
