package com.org.bank.dao;

public interface TransactionDAO {
    void deposit(int accountId, double amount) throws Exception;
    void withdraw(int accountId, double  amount) throws Exception;
    void transaction(int fromaccountId, int toaccountId, double amount) throws Exception;
    void displayTransactionHistory(int accountId);
    
}
