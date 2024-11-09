package com.org.bank.dao;

import com.org.bank.model.Account;

public interface AccountDAO {
	void createAccount(Account account) throws Exception;
	Account viewAccount(int accountid) throws Exception;
	void updateAccountinfo(int accountid, String newAddress, String newcontact) throws Exception;
	void getBalance(int accountid) throws Exception;

}
