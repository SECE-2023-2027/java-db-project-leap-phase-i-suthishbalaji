package com.org.bank.model;

public class Account {
         private int accountId;
         private String accountholder;
         private String accountType;
         private double balance;
         private String address;
         private String contactNumber;
     	
		public Account(int accountId, String accountholder, String accountType, double balance, String address,
				String contactNumber) {
			
			this.accountId = accountId;
			this.accountholder = accountholder;
			this.accountType = accountType;
			this.balance = balance;
			this.address = address;
			this.contactNumber = contactNumber;
		}
		public int getAccountId() {
			return accountId;
		}
		public void setAccountId(int accountId) {
			this.accountId = accountId;
		}
		public String getAccountholder() {
			return accountholder;
		}
		public void setAccountholder(String accountholder) {
			this.accountholder = accountholder;
		}
	
		public String getAccountType() {
			return accountType;
		}
		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}
		public double getBalance() {
			return balance;
		}
		public void setBalance(double balance) {
			this.balance = balance;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getContactNumber() {
			return contactNumber;
		}
		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}
		@Override
		public String toString() {
			return "Account [account=" + accountId + ", accountholder=" + accountholder + ", accountType=" + accountType
					+ ", balance=" + balance + ", address=" + address + ", contactNumber=" + contactNumber + "]";
		}
}
