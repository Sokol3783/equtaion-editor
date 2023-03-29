package org.example.equation;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationParser {

  private final String equation;
  private EquationNode head;

  private double root;

  public EquationParser(String equation) {
    this.equation = equation;
    this.head = null;
  }

  public boolean isRoot(double x) {
    root = x;
    if (this.head == null) {
      this.head = parseEquation();
    }
    return head.getValue() == 0;
  }

  public EquationNode parseEquation() {
    LinkedList<EquationNode> nodes = new LinkedList<>();
    LinkedList<Operator> operators = new LinkedList<>();
    Pattern pattern = Pattern.compile("(\\d*\\.\\d+|\\d+|[xX]|[()+\\-*/=])");
    Matcher matcher = pattern.matcher(equation);

    while (matcher.find()) {
      String token = matcher.group();
      char firstChar = token.charAt(0);

      if (Character.isDigit(firstChar)) {
        double value = Double.parseDouble(token);
        nodes.add(new EquationNode(value));
      } else if (Character.isLetter(firstChar)) {
        nodes.add(new EquationNode(token.charAt(0)));
      } else if (firstChar == '(') {
        operators.add(Operator.OPEN_PARENTHESIS);
      } else if (firstChar == ')') {
        while (!operators.isEmpty() && operators.getLast() != Operator.OPEN_PARENTHESIS) {
          Operator lastOperator = operators.removeLast();
          EquationNode right = nodes.removeLast();
          EquationNode left = nodes.removeLast();
          nodes.add(new EquationNode(lastOperator, left, right));
        }
        operators.removeLast();
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

  public String getEquation() {
    return equation;
  }

  public class EquationNode {

    private double value;
    private boolean solved;
    private final char variable;
    private final Operator operator;
    private final EquationNode left;
    private final EquationNode right;

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
      if (this.variable != '\0') {
        return root;
      }
      if (!this.solved) {
        double leftValue = this.left.getValue();
        double rightValue = this.right.getValue();
        this.value = this.operator.apply(leftValue, rightValue);
        this.solved = true;
      }
      return this.value;
    }

  }

  private enum Operator {
    ADD("+", 3) {
      @Override
      public double apply(double left, double right) {
        System.out.println(left + " + " + right);
        return left + right;
      }
    },
    SUBTRACT("-", 3) {
      @Override
      public double apply(double left, double right) {
        System.out.println(left + " - " + right);
        return left - right;
      }
    },
    MULTIPLY("*", 2) {
      @Override
      public double apply(double left, double right) {
        System.out.println(left + " * " + right);
        return left * right;
      }
    },
    DIVIDE("/", 2) {
      @Override
      public double apply(double left, double right) {
        System.out.println(left + "/" + right);
        return left / right;
      }
    },

    EQUALS("=", 4) {
      @Override
      public double apply(double left, double right) {
        System.out.println(left + " - " + right);
        return left - right;
      }
    },
    OPEN_PARENTHESIS("(", 1) {
      @Override
      public double apply(double left, double right) {
        return 0;
      }
    };

    private final String token;
    private final int precedence;

    Operator(String token, int precedence) {
      this.token = token;
      this.precedence = precedence;
    }

    public abstract double apply(double left, double right);

    public boolean hasPrecedence(Operator other) {
      return this.precedence >= other.precedence;
    }

    public static Operator fromToken(String token) {
      return switch (token) {
        case "+" -> ADD;
        case "-" -> SUBTRACT;
        case "*" -> MULTIPLY;
        case "/" -> DIVIDE;
        case "=" -> EQUALS;
        default -> throw new IllegalArgumentException("Invalid operator token: " + token);
      };
    }
  }
}

