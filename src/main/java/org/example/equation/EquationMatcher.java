package org.example.equation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationMatcher {

  private static String patternForChar = "^[ 0-9.xX+*-=/()]+$";
  private static String patternForRoundBrackets = "^(?:\\\\(|[^()])*+(\\\\((?:\\\\(|[^()])*+\\\\))*(?(1)(?!))$";

  private final Pattern pattern;
  private final String message;

  private EquationMatcher(String pattern, String message) {
    this.pattern = Pattern.compile(pattern);
    this.message = message;
  }

  public boolean matches(String equation) {
    Matcher matcher = pattern.matcher(equation);
    if (!matcher.matches()) {
      System.out.println(this.message);
      return false;
    }
    return true;
  }

  public static EquationMatcher getEquationMatcherForChar() {
    return new EquationMatcher(patternForChar,
        "Contain invalid char! Can matches only '0-9+-*/xX. '");
  }

  public static EquationMatcher getEquationMatcherForRoundBrackets() {
    return new EquationMatcher(patternForRoundBrackets, "Brackets doesn't close correct!");
  }


}
