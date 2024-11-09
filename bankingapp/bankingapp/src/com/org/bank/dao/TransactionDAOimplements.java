package com.org.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.org.bank.util.databaseconnection;

public class TransactionDAOimplements implements TransactionDAO {

    @Override
    public void deposit(int accountId, double amount) throws Exception {
        try (Connection con = databaseconnection.getconnection()) {
            String updateBalanceQuery = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
            String insertTransactionQuery = "INSERT INTO transaction (account_id, transaction_type, amount) VALUES (?, 'Deposit', ?)";
            executeTransaction(con, accountId, amount, updateBalanceQuery, insertTransactionQuery);
            System.out.println("Deposit of $" + amount + " to account " + accountId + " completed.");
        }
    }

    @Override
    public void withdraw(int accountId, double amount) throws Exception {
        try (Connection con = databaseconnection.getconnection()) {
            String accountType = getAccountType(con, accountId);
            double currentBalance = getCurrentBalance(con, accountId);

            if ("Current".equalsIgnoreCase(accountType)) {
                double overdraftLimit = getOverdraftLimit(con, accountId);
                if (currentBalance - amount < -overdraftLimit) {
                    throw new Exception("Insufficient balance and overdraft limit exceeded. " +
                            "Current balance: " + currentBalance + ", Overdraft limit: " + overdraftLimit + ", Withdrawal amount: " + amount);
                }
            } else if (currentBalance < amount) {
                throw new Exception("Insufficient balance for withdrawal. " +
                        "Current balance: " + currentBalance + ", Withdrawal amount: " + amount);
            }

            // Proceed with the withdrawal
            String updateBalanceQuery = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
            String insertTransactionQuery = "INSERT INTO transaction (account_id, transaction_type, amount) VALUES (?, 'Withdrawal', ?)";
            executeTransaction(con, accountId, amount, updateBalanceQuery, insertTransactionQuery);
            System.out.println("Withdrawal of $" + amount + " from account " + accountId + " completed.");
        }
    }

    @Override
    public void transaction(int fromAccountId, int toAccountId, double amount) throws Exception {
        // Implementation of transaction method, e.g., for transferring between accounts
    }

    @Override
    public void displayTransactionHistory(int accountId) {
        // Implementation of displayTransactionHistory method
    }

    private double getCurrentBalance(Connection con, int accountId) throws SQLException {
        String checkBalanceQuery = "SELECT balance FROM account WHERE account_id = ?";
        try (PreparedStatement ps = con.prepareStatement(checkBalanceQuery)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        throw new SQLException("Account not found");
    }

    private String getAccountType(Connection con, int accountId) throws SQLException {
        String sql = "SELECT account_type FROM account WHERE account_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("account_type");
            }
        }
        throw new SQLException("Account not found");
    }

    private double getOverdraftLimit(Connection con, int accountId) throws SQLException {
        String sql = "SELECT overdraft_limit FROM CurrentAccount WHERE account_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("overdraft_limit");
            }
        }
        throw new SQLException("Current account not found or overdraft limit not set.");
    }

    private void executeUpdate(Connection con, String query, double amount, int accountId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setDouble(1, amount);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    private void executeTransaction(Connection con, int accountId, double amount, String updateBalanceQuery, String insertTransactionQuery) throws SQLException {
        con.setAutoCommit(false);
        try {
            executeUpdate(con, updateBalanceQuery, amount, accountId);
            try (PreparedStatement ps = con.prepareStatement(insertTransactionQuery)) {
                ps.setInt(1, accountId);
                ps.setDouble(2, amount);
                ps.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }
}
