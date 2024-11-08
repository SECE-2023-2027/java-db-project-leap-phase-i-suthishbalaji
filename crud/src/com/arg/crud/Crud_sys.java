package com.arg.crud;

import java.sql.*;
import java.util.Scanner;

public class Crud_sys {
    private static final String URL = "jdbc:mysql://localhost:3306/crud";
    private static final String USER = "root";
    private static final String PASSWORD = "tarun2006";
    private static Connection con;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
            
//            String sql="INSERT INTO Employee(name,age,department) VALUES (?,?,?);";
//            PreparedStatement statement=con.prepareStatement(sql);
//            statement.setString(1,"Suthish");
//            statement.setInt(2,20);
//            statement.setString(3,"cse");
//            statement.executeUpdate();
//            System.out.print("Record created");
            String sql="SELECT id,age,name,department FROM Employee"

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

 }
    


