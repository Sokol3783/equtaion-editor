package org.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquationAnalyticsimpl implements EquationAnalytics {

  private final static String ALL_UNIQUE_ROOTS = "SELECT number FROM equation_root INNER JOIN root USING (root_id) HAVING COUNT(DISTINCT(equation_id)) = 1";
  private final static String EQUATIONS_WITH_ROOT = "SELECT records FROM root "
      + " INNER JOIN equation_root USING(root_id)"
      + " INNER JOIN equation USING (equation_id)"
      + "WHERE number=?";

  @Override
  public List<Double> findAllUniqueRoots() {
    List<Double> result = new ArrayList<>();
    try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(ALL_UNIQUE_ROOTS)) {
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        result.add(rs.getDouble("number"));
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong");
    }
    return result;
  }

  @Override
  public List<String> findAllEquationWithRoot(double root) {
    List<String> result = new ArrayList<>();
    try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement statement = con.prepareStatement(EQUATIONS_WITH_ROOT)) {
      statement.setDouble(1, root);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        result.add(rs.getString("record"));
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong");
    }
    return result;
  }
}
