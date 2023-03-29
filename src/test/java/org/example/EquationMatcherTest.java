package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.equation.EquationMatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EquationMatcherTest {

  @DisplayName("Equation contain valid char")
  @ParameterizedTest(name = "{index} => expected={1}, input={0}")
  @CsvSource({
      "2*x+5ad=17,false",
      "2*x+5=17, true",
      "-1.3*5cc/x=1.2, false",
      "5-(2x+1)=15, true",
      "(5x -1)/(2-2x)=10, true"
  })
  public void testCorrectSymbols(String input, boolean expected) {
    EquationMatcher matcher = EquationMatcher.getEquationMatcherForChar();
    boolean result = matcher.matches(input);
    assertEquals(expected, result);
  }


  @DisplayName("Equation brackets closed correct")
  @ParameterizedTest(name = "{index} => expected={1}, input={0}")
  @CsvSource({
      "(2*x)+5=17,true",
      "(2*x+)5=17, false",
      "((-1.3*5)/x)+3=1.2, false",
      "(5-(2*x+1)=15, true",
      "(5*x -1)/(2-2*x)=10, true"
  })
  public void testCorrectBrackets(String input, boolean expected) {
    EquationMatcher matcher = EquationMatcher.getEquationMatcherForRoundBrackets();
    boolean result = matcher.matches(input);
    assertEquals(expected, result);
  }
}