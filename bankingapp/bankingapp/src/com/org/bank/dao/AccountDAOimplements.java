package com.org.bank.dao;
import com.org.bank.model.Account;
import com.org.bank.util.databaseconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccountDAOimplements implements AccountDAO {

    @Override
    public void createAccount(Account account) throws Exception {
        String sql = "INSERT INTO Account ( account_holder, account_type, balance, address, contact_number) VALUES ( ?, ?, ?, ?, ?)";
        
        try (Connection con = databaseconnection.getconnection();
             PreparedStatement stmt = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getAccountholder());
            stmt.setString(2, account.getAccountType());
            stmt.setDouble(3, account.getBalance());
            stmt.setString(4, account.getAddress());
            stmt.setString(5, account.getContactNumber());
            int affectedRows=stmt.executeUpdate();
            if(affectedRows>0)
            {
            	ResultSet generatedKeys= stmt.getGeneratedKeys();
            	if(generatedKeys.next()) {
            		int accountId= generatedKeys.getInt(1);
            		if(account.getAccountType().equals("Savings")){
            			String sqlsavingAccount="INSERT INTO SavingsAccount (account_id, interest_rate) VALUES (?,?)";
            			PreparedStatement stmtSaving = con.prepareStatement(sqlsavingAccount);
            			stmtSaving.setInt(1, accountId);
            			stmtSaving.setDouble(2, 0.4);
            			stmtSaving.executeUpdate();
            		}
            		else if(account.getAccountType().equals("Current")) {
            			String sqlcurrentAccount = "INSERT INTO CurrentAccount(account_id, overdraft_limit) VALUES (?,?)";
            			PreparedStatement stmtCurrent = con.prepareStatement(sqlcurrentAccount);
            			stmtCurrent.setInt(1, accountId);
            			stmtCurrent.setDouble(2, 5000.00);
            			stmtCurrent.executeUpdate();
            		}
            	}
            }
        } 
        System.out.println("Account created successfully");
    }

    @Override
    public Account viewAccount(int accountid) throws Exception {
        // TODO: Implement method logic
    	String sql ="SELECT * FROM  account WHERE account_id = ?";
    	Account account = null;
    	try(Connection con = databaseconnection.getconnection();
    			PreparedStatement stmt= con.prepareStatement(sql)){
	    		stmt.setInt(1, accountid);
	    		ResultSet rs = stmt.executeQuery();
    		    
	    		if(rs.next()) {
	    			account = new Account(
	    					rs.getInt("account_Id"),
	    					rs.getString("account_holder"),
	    					rs.getString("account_type"),
	    					rs.getDouble("balance"),
	    					rs.getString("address"),
	    					rs.getString("contact_number")
	    				);
    	}
	    	if(account!=null) {	
	    		System.out.println("Account details retreived successful");
	    	}else
	    	{
	    		System.out.println("Account not found");
	    	}
        return account;
    	}
    }

    @Override
    public void updateAccountinfo(int accountid, String newAddress, String newContact) throws Exception {
        // TODO: Implement method logic
    	String sql = "UPDATE account SET address=?, contact_number=? WHERE account_id =?";
    	try(Connection con = databaseconnection.getconnection();
    			PreparedStatement stmt = con.prepareStatement(sql)){
    		stmt.setString(1, newAddress);
    		stmt.setString(2, newContact);
    	    stmt.setInt(3,accountid);
    	    int rowsAffected=stmt.executeUpdate();
    	    if(rowsAffected>0)
    	    {
    	    	System.out.println("Account Information updated successfully");
    	    
    	    }else
    	    {
    	    	System.out.println("Account not found");
    	    }
    	}
    }
    

    @Override
    public void getBalance(int accountid) throws Exception {
        // TODO: Implement method logic
    }
}
