package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static final String DB_URL = "jdbc:postgresql://localhost/equation_app";
  private static final String USER = "postgres";
  private static final String PASS = "postgres";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, USER, PASS);
  }
}
