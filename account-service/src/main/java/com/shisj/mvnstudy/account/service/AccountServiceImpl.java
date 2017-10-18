package com.shisj.mvnstudy.account.service;

import com.shisj.mvnstudy.account.email.AccountEmailException;
import com.shisj.mvnstudy.account.email.AccountEmailService;
import com.shisj.mvnstudy.account.email.AccountEmailServiceImpl;
import com.shisj.mvnstudy.account.persist.Account;
import com.shisj.mvnstudy.account.persist.AccountPersistService;
import com.shisj.mvnstudy.account.persist.AccountPersistServiceImpl;

public class AccountServiceImpl implements AccountService {

	public String generateCaptchKey() {
		
		AccountPersistService service = new AccountPersistServiceImpl();
		service.createAccount(new Account());
		
		AccountEmailService email = new AccountEmailServiceImpl();
		try {
			email.sendMail("to", "subject", "htmlText");
		} catch (AccountEmailException e) {
			e.printStackTrace();
		}
		return "generateCaptchKey";
	}

}
