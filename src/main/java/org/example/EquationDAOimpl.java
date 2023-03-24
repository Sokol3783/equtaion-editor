package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EquationDAOimpl implements DAO {

  private static Map<String, Long> cache = new HashMap<>();

  private static String INSERT = "INSERT INTO equation (record) VALUES (?) ON CONFLICT (equation) DO UPDATE SET equation = excluded.equation RETURNING equation_id";
  private static String GET = "SELECT record FROM equation WHERE equation_id = ?";

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

  private String getKeyByValue(long id) {
    return cache.entrySet()
        .stream()
        .filter(entry -> Objects.equals(entry.getValue(), id))
        .map(s -> s.getKey().toString())
        .findFirst().get();
  }
}
