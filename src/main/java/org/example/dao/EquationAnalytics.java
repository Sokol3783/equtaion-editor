package org.example.dao;

import java.util.List;

public interface EquationAnalytics {

  List<Double> findAllUniqueRoots();

  List<String> findAllEquationWithRoot(double root);
}
