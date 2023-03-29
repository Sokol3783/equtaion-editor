package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EquationDAOimpl implements DAO {

  private static final Map<String, Long> cache = new HashMap<>();

  private static final String INSERT = "INSERT INTO equation (record) VALUES (?) ON CONFLICT (equation) DO UPDATE SET equation = excluded.equation RETURNING equation_id";
  private static final String GET = "SELECT record FROM equation WHERE equation_id = ?";

  @Override
  public boolean create(String value) {
    if (cache.containsKey(value)) {
      return true;
    }
    try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT)) {
      statement.setString(1, value);
      statement.setString(2, value);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        cache.put(value, resultSet.getLong("equation_id"));
        return true;
      }
    } catch (SQLException e) {
      System.out.println("Equation doesn't save");
    }
    return false;
  }

  @Override
  public long getId(String value) {
    if (cache.containsKey(value)) {
      return cache.get(value);
    }
    try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(GET)) {
      statement.setString(1, value);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        long id = resultSet.getLong("equation_id");
        cache.put(value, id);
        return id;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    throw new RuntimeException("Equation didn't save!");
  }
}
