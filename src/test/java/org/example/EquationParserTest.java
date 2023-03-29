package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.equation.EquationParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EquationParserTest {

  @DisplayName("")
  @ParameterizedTest(name = "{index} => expected={1}, input={0}")
  @CsvSource({
      "2*x-1=3, 2",
      "x*x-1=3, 2",
      "x*x-1=3, -2",
      "2*x-1.5=3, 2.25",
      "(2*x)+5=17, 6",
      "(5*x -1)/(2-2*x)=10, 44"
  })
  void parseEquation(String input, double root) {
    EquationParser parser = new EquationParser(input);
    assertTrue(parser.isRoot(root));
  }
}