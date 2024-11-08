package com.arg.crud;

import java.sql.*;
import java.util.Scanner;

public class Employee {
    private static final String URL = "jdbc:mysql://localhost:3306/crud";
    private static final String USER = "root";
    private static final String PASSWORD = "tarun2006";
    private static Connection con;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n--- CRUD Menu ---");
                System.out.println("1. Create Employee");
                System.out.println("2. Read Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createEmployee(scanner);
                        break;
                    case 2:
                        readEmployees();
                        break;
                    case 3:
                        updateEmployee(scanner);
                        break;
                    case 4:
                        deleteEmployee(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 5);

            con.close();
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createEmployee(Scanner scanner) {
        try {
            System.out.print("Enter name: ");
            String name = scanner.next();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            System.out.print("Enter department: ");
            String department = scanner.next();

            String query = "INSERT INTO Employee (name, age, department) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, department);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error creating employee: " + e.getMessage());
        }
    }

    private static void readEmployees() {
        try {
            String query = "SELECT * FROM Employee";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String department = rs.getString("department");
                System.out.printf("ID: %d | Name: %s | Age: %d | Department: %s\n", id, name, age, department);
            }
        } catch (SQLException e) {
            System.out.println("Error reading employees: " + e.getMessage());
        }
    }

    private static void updateEmployee(Scanner scanner) {
        try {
            System.out.print("Enter employee ID to update: ");
            int id = scanner.nextInt();
            System.out.print("Enter new name: ");
            String name = scanner.next();
            System.out.print("Enter new age: ");
            int age = scanner.nextInt();
            System.out.print("Enter new department: ");
            String department = scanner.next();

            String query = "UPDATE Employee SET name = ?, age = ?, department = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, department);
            pstmt.setInt(4, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }

    private static void deleteEmployee(Scanner scanner) {
        try {
            System.out.print("Enter employee ID to delete: ");
            int id = scanner.nextInt();

            String query = "DELETE FROM Employee WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Employee ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}
