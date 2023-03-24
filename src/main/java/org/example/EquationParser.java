package org.example;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationParser {

  private String equation;
  private EquationNode root;

  public EquationParser(String equation) {
    this.equation = equation;
    this.root = null;
  }

  public double solve(double x) {
    if (this.root == null) {
      this.root = parseEquation(this.equation);
    }
    this.root.setValue(x);
    return this.root.getValue();
  }

  public boolean hasOtherRoot(double x) {
    double value = solve(x);
    return value == 0;
  }

  private EquationNode parseEquation(String equation) {
    LinkedList<EquationNode> nodes = new LinkedList<>();
    LinkedList<Operator> operators = new LinkedList<>();

    Pattern pattern = Pattern.compile("(\\d*\\.\\d+|\\d+|[a-zA-Z])");
    Matcher matcher = pattern.matcher(equation);

    while (matcher.find()) {
      String token = matcher.group();
      char firstChar = token.charAt(0);

      if (Character.isDigit(firstChar)) {
        double value = Double.parseDouble(token);
        nodes.add(new EquationNode(value));
      } else if (Character.isLetter(firstChar)) {
        nodes.add(new EquationNode(token.charAt(0)));
      } else {
        Operator operator = Operator.fromToken(token);
        while (!operators.isEmpty() && operator.hasPrecedence(operators.getLast())) {
          Operator lastOperator = operators.removeLast();
          EquationNode right = nodes.removeLast();
          EquationNode left = nodes.removeLast();
          nodes.add(new EquationNode(lastOperator, left, right));
        }
        operators.add(operator);
      }
    }

    while (!operators.isEmpty()) {
      Operator operator = operators.removeLast();
      EquationNode right = nodes.removeLast();
      EquationNode left = nodes.removeLast();
      nodes.add(new EquationNode(operator, left, right));
    }

    return nodes.getLast();
  }

  private static class EquationNode {

    private double value;
    private boolean solved;
    private char variable;
    private Operator operator;
    private EquationNode left;
    private EquationNode right;

    public EquationNode(double value) {
      this.value = value;
      this.solved = true;
      this.variable = '\0';
      this.operator = null;
      this.left = null;
      this.right = null;
    }

    public EquationNode(char variable) {
      this.value = 0.0;
      this.solved = false;
      this.variable = variable;
      this.operator = null;
      this.left = null;
      this.right = null;
    }

    public EquationNode(Operator operator, EquationNode left, EquationNode right) {
      this.value = 0.0;
      this.solved = false;
      this.variable = '\0';
      this.operator = operator;
      this.left = left;
      this.right = right;
    }

    public double getValue() {
      if (!this.solved) {
        double leftValue = this.left.getValue();
        double rightValue = this.right.getValue();
        this.value = this.operator.apply(leftValue, rightValue);
        this.solved = true;
      }
      return this.value;
    }

    public void setValue(double value) {
      if (this.variable != '\0') {
        this.value = value;
        this.solved = true;
      }
    }

    public boolean isSolved() {
      return this.solved;
    }
  }

  private static enum Operator {
    ADD("+", 1) {
      @Override
      public double apply(double left, double right) {
        return left + right;
      }
    },
    SUBTRACT("-", 1) {
      @Override
      public double apply(double left, double right) {
        return left - right;
      }
    },
    MULTIPLY("*", 2) {
      @Override
      public double apply(double left, double right) {
        return left * right;
      }
    },
    DIVIDE("/", 2) {
      @Override
      public double apply(double left, double right) {
        return left / right;
      }
    };

    private final String token;
    private final int precedence;

    private Operator(String token, int precedence) {
      this.token = token;
      this.precedence = precedence;
    }

    public abstract double apply(double left, double right);

    public boolean hasPrecedence(Operator other) {
      return this.precedence >= other.precedence;
    }

    public static Operator fromToken(String token) {
      switch (token) {
        case "+":
          return ADD;
        case "-":
          return SUBTRACT;
        case "*":
          return MULTIPLY;
        case "/":
          return DIVIDE;
        default:
          throw new IllegalArgumentException("Invalid operator token: " + token);
      }
    }
  }
}

