package com.org.bank;
import com.org.bank.dao.TransactionDAO;
import com.org.bank.dao.TransactionDAOimplements;
import java.util.*;
import com.org.bank.dao.AccountDAO;
import com.org.bank.dao.AccountDAOimplements;
import com.org.bank.model.Account;
public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		AccountDAO accountDAO = new AccountDAOimplements();
		TransactionDAO transactionDAO= new TransactionDAOimplements();
		Scanner scanner=new Scanner(System.in);
		
        while(true) {
        	System.out.println("\n---Banking Application Menu---");
        	System.out.println("1.Create a new account");
        	System.out.println("2.view Account details");
        	System.out.println("3.Update account details");
        	System.out.println("4.Deposit money");
        	System.out.println("5.withdraw money");
        	System.out.println("6.transfer money");
        	System.out.println("8.Check balance");
        	System.out.println("9.Exit");
        	System.out.println("Enter your choice");
        	int choice = scanner.nextInt();
    		scanner.nextLine();
    		switch(choice)
    		{
    		case 1:
    			//create a new account
    			System.out.println("Enter account holder name");
    			String accountholder=scanner.nextLine();
    			System.out.println("Enter account type (savings/current):");
    			String accountType=scanner.nextLine();
    			System.out.println("Enter initial balance");
    			double balance = scanner.nextDouble();
    			scanner.nextLine();
    			System.out.println("Enter address");
    			String address=scanner.nextLine();
    			System.out.println("Enter contact number");
    			String contactNumber=scanner.nextLine();
    			Account  newAccount =new  Account(0,accountholder,accountType,balance,address,contactNumber);
        		try {
        			accountDAO.createAccount(newAccount);
        		} catch (Exception e)
        		{
        			e.printStackTrace();
        		}
        		break;
    		case 2:
    		    System.out.println("Enter account ID to view");
    		    int viewAccountID = scanner.nextInt();
    		    Account retrievedAccount = accountDAO.viewAccount(viewAccountID);
    		    if (retrievedAccount != null) {
    		        System.out.println("Account ID: " + retrievedAccount.getAccountId());
    		        System.out.println("Account Holder: " + retrievedAccount.getAccountholder());
    		        System.out.println("Account Type: " + retrievedAccount.getAccountType());
    		        System.out.println("Balance: " + retrievedAccount.getBalance());
    		        System.out.println("Address: " + retrievedAccount.getAddress());
    		        System.out.println("Contact Number: " + retrievedAccount.getContactNumber());
    		    } else {
    		        System.out.println("Account not found");
    		    }
    		    break;
    		case 3:
    			System.out.println("Enter account ID to update:");
    			int updateAccountID= scanner.nextInt();
    			scanner.nextLine();
    			System.out.println("Enter new Address");
    			String newAddress= scanner.nextLine();
    			System.out.println("Enter new contact number");
    			String newcontact = scanner.nextLine();
    			accountDAO.updateAccountinfo(updateAccountID, newAddress, newcontact);
    			break;
    		case 4: 
    			System.out.println("Enter account ID to deposit into: ");
    			int depositAccountId = scanner.nextInt();
    			System.out.println("Enter amount to deposit");
    			double depositAmount = scanner.nextDouble();
    			transactionDAO.deposit(depositAccountId, depositAmount);
    			break;
    		case 5:
    			System.out.println("Enter account Id to withdraw from:");
    			int withdrawAccountId = scanner.nextInt();
    			System.out.println("Enter amount to withdraw");
    			double withdrawAmount = scanner.nextDouble();
    			transactionDAO.withdraw(withdrawAccountId, withdrawAmount);
    			break;
        	}
    		
    	
    			
        }
	}

}


