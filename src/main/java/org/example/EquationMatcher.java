package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationMatcher {

  private static String patternForChar = "^[ 0-9.xX+*-=/()]+$";
  private static String patternForRoundBrackets = "^(?:\\\\(|[^()])*+(\\\\((?:\\\\(|[^()])*+\\\\))*(?(1)(?!))$";

  private Pattern pattern;

  private EquationMatcher(String pattern) {
    this.pattern = Pattern.compile(pattern);
  }

  public boolean validateChar(String equation) {
    Matcher matcher = pattern.matcher(equation);
    return matcher.matches();
  }

  public static EquationMatcher getEquationMatcherForChar() {
    return new EquationMatcher(patternForChar);
  }

  public static EquationMatcher getEquationMatcherForRoundBrackets() {
    return new EquationMatcher(patternForRoundBrackets);
  }


}
