package main;

import java.sql.*;
import java.util.Scanner;

public class ExpenseTrack {

    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "suthishbalaji1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeCategories() {
        String[] defaultCategories = {"Food", "Transport", "Entertainment", "Shopping", "Utilities"};
        String insertCategorySQL = "INSERT IGNORE INTO categories (category_name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertCategorySQL)) {

            for (String category : defaultCategories) {
                stmt.setString(1, category);
                stmt.executeUpdate();
            }
            System.out.println("Default categories initialized.");
        } catch (SQLException e) {
            System.err.println("Error initializing categories: " + e.getMessage());
        }
    }

    public static int authenticateUser(String username, String password) {
        String querySQL = "SELECT user_id FROM users WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }

        return -1; 
    }

    public static void registerUser(String username, String password) {
        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("User registered successfully.");
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }

    public static void showAvailableCategories() {
        String selectCategorySQL = "SELECT category_name FROM categories";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectCategorySQL)) {

            System.out.println("Available Categories:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("category_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving categories: " + e.getMessage());
        }
    }
    
    public static void addExpense(int userId, String category, double amount, String description) {
        String getCategorySQL = "SELECT category_id FROM categories WHERE category_name = ?";
        String insertExpenseSQL = "INSERT INTO expenses (user_id, category_id, amount, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement getCategoryStmt = conn.prepareStatement(getCategorySQL);
             PreparedStatement insertExpenseStmt = conn.prepareStatement(insertExpenseSQL)) {

            getCategoryStmt.setString(1, category);
            ResultSet rs = getCategoryStmt.executeQuery();

            if (rs.next()) {
                int categoryId = rs.getInt("category_id");

                insertExpenseStmt.setInt(1, userId);
                insertExpenseStmt.setInt(2, categoryId);
                insertExpenseStmt.setDouble(3, amount);
                insertExpenseStmt.setString(4, description);
                insertExpenseStmt.executeUpdate();

                System.out.println("Expense added successfully.");
            } else {
                System.out.println("Category not found. Please add the category first.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
        }
    }

    public static void viewExpensesByUser(int userId) {
        String querySQL = "SELECT e.expense_id, c.category_name, e.amount, e.description " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "WHERE e.user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.printf("ID: %d, Category: %s, Amount: %.2f, Description: %s\n",
                        rs.getInt("expense_id"), rs.getString("category_name"),
                        rs.getDouble("amount"), rs.getString("description"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing expenses: " + e.getMessage());
        }
    }

    public static void viewExpensesByCategory(String category) {
        String querySQL = "SELECT e.expense_id, e.amount, e.description " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "WHERE c.category_name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            boolean hasExpenses = false;
            while (rs.next()) {
                System.out.printf("ID: %d, Amount: %.2f, Description: %s\n",
                        rs.getInt("expense_id"), rs.getDouble("amount"),
                        rs.getString("description"));
                hasExpenses = true;
            }

            if (!hasExpenses) {
                System.out.println("No expenses found for the category: " + category);
            }
        } catch (SQLException e) {
            System.err.println("Error viewing expenses by category: " + e.getMessage());
        }
    }

    public static void addCategory(String category) {
        String insertCategorySQL = "INSERT IGNORE INTO categories (category_name) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertCategorySQL)) {
            stmt.setString(1, category);
            stmt.executeUpdate();
            System.out.println("Category added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
        }
    }

    public static void deleteExpense(int expenseId) {
        String deleteSQL = "DELETE FROM expenses WHERE expense_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSQL)) {

            stmt.setInt(1, expenseId);
            stmt.executeUpdate();
            System.out.println("Expense deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
        }
    }

    public static void updateExpense(int expenseId, double amount, String description) {
        String updateSQL = "UPDATE expenses SET amount = ?, description = ? WHERE expense_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSQL)) {

            stmt.setDouble(1, amount);
            stmt.setString(2, description);
            stmt.setInt(3, expenseId);
            stmt.executeUpdate();
            System.out.println("Expense updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
        }
    }

    // Method to generate monthly expense report
    public static void generateMonthlyExpenseReport(int userId, int month, int year) {
        String querySQL = "SELECT c.category_name, SUM(e.amount) AS total_amount, COUNT(e.expense_id) AS expense_count " +
                          "FROM expenses e " +
                          "JOIN categories c ON e.category_id = c.category_id " +
                          "WHERE e.user_id = ? AND MONTH(e.expense_date) = ? AND YEAR(e.expense_date) = ? " +
                          "GROUP BY c.category_name";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            ResultSet rs = stmt.executeQuery();

            double totalExpenses = 0;
            boolean hasExpenses = false;
            while (rs.next()) {
                hasExpenses = true;
                String category = rs.getString("category_name");
                double totalAmount = rs.getDouble("total_amount");
                int expenseCount = rs.getInt("expense_count");

                System.out.printf("Category: %-15s | Total Amount: %.2f | Number of Expenses: %d\n",
                        category, totalAmount, expenseCount);
                totalExpenses += totalAmount;
            }

            if (hasExpenses) {
                System.out.printf("\nTotal Expenses for %02d/%d: %.2f\n", month, year, totalExpenses);
            } else {
                System.out.println("No expenses recorded for the specified month.");
            }
        } catch (SQLException e) {
            System.err.println("Error generating monthly expense report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Expense Tracker Application");

        initializeCategories();

        try {
            while (true) {
                System.out.println("\nPlease Login or Register:");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.print("Choose an option: ");
                int loginChoice = scanner.nextInt();
                scanner.nextLine();

                String username, password;
                int userId = -1;

                switch (loginChoice) {
                    case 1:
                        System.out.print("Enter username: ");
                        username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        password = scanner.nextLine();
                        userId = authenticateUser(username, password);

                        if (userId == -1) {
                            System.out.println("Invalid credentials. Please try again.");
                            continue;
                        }
                        break;

                    case 2:
                        System.out.print("Enter new username: ");
                        username = scanner.nextLine();
                        System.out.print("Enter new password: ");
                        password = scanner.nextLine();
                        registerUser(username, password);
                        continue;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

                while (true) {
                    System.out.println("\nExpense Tracker Menu:");
                    System.out.println("1. Add Expense");
                    System.out.println("2. View All Expenses");
                    System.out.println("3. View Expenses by Category");
                    System.out.println("4. Add Category");
                    System.out.println("5. Delete Expense");
                    System.out.println("6. Update Expense");
                    System.out.println("7. Generate Monthly Expense Report");
                    System.out.println("8. Exit");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter category name: ");
                            String category = scanner.nextLine();
                            showAvailableCategories();
                            System.out.print("Enter amount: ");
                            double amount = scanner.nextDouble();
                            scanner.nextLine();
                            System.out.print("Enter description: ");
                            String description = scanner.nextLine();
                            addExpense(userId, category, amount, description);
                            break;

                        case 2:
                            viewExpensesByUser(userId);
                            break;

                        case 3:
                            System.out.print("Enter category name to view expenses: ");
                            String categoryName = scanner.nextLine();
                            viewExpensesByCategory(categoryName);
                            break;

                        case 4:
                            System.out.print("Enter category name to add: ");
                            String newCategory = scanner.nextLine();
                            addCategory(newCategory);
                            break;

                        case 5:
                            System.out.print("Enter expense ID to delete: ");
                            int expenseId = scanner.nextInt();
                            deleteExpense(expenseId);
                            break;

                        case 6:
                            System.out.print("Enter expense ID to update: ");
                            int expenseIdToUpdate = scanner.nextInt();
                            System.out.print("Enter new amount: ");
                            double newAmount = scanner.nextDouble();
                            scanner.nextLine();
                            System.out.print("Enter new description: ");
                            String newDescription = scanner.nextLine();
                            updateExpense(expenseIdToUpdate, newAmount, newDescription);
                            break;

                        case 7:
                            System.out.print("Enter month (1-12): ");
                            int month = scanner.nextInt();
                            System.out.print("Enter year (YYYY): ");
                            int year = scanner.nextInt();
                            generateMonthlyExpenseReport(userId, month, year);
                            break;

                        case 8:
                            System.out.println("Goodbye!");
                            return;

                        default:
                            System.out.println("Invalid choice, try again.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
