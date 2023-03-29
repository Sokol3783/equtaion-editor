package org.example;

import java.util.ArrayList;
import java.util.List;
import org.example.equation.EquationMatcher;
import org.example.menu.ConsoleMenu;

public class App {

  public static void main(String[] args) {
    ConsoleMenu menu = ConsoleMenu.getInstance(getMatchers());
    menu.run();
  }

  private static List<EquationMatcher> getMatchers() {
    List<EquationMatcher> matchers = new ArrayList<>();
    matchers.add(EquationMatcher.getEquationMatcherForChar());
    matchers.add(EquationMatcher.getEquationMatcherForRoundBrackets());
    return matchers;
  }
}
