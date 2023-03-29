package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EquationRootDAOImpl implements DAO {

  private static final Map<String, Long> cache = new HashMap<>();

  private static final String INSERT = "INSERT INTO root (number)  VALUES (?)  ON CONFLICT (number) DO UPDATE SET number = excluded.number RETURNING root_id";
  private static final String GET = "SELECT root_id FROM root WHERE number = ?";

  @Override
  public boolean create(String value) {
    if (cache.containsKey(value)) {
      return true;
    }
    try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(INSERT)) {
      statement.setDouble(1, Double.parseDouble(value));
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        cache.put(value, resultSet.getLong("root_id"));
        return true;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
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
