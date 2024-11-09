package com.org.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseconnection {
      private static final String URL = "jdbc:mysql://127.0.0.1:3306/bank_app";
      private static final String USERNAME = "root";
      private static final String PASSWORD = "sri.@2005";
      
      public static Connection  getconnection() throws SQLException{
    	  return DriverManager.getConnection(URL,USERNAME,PASSWORD);
      }
}
