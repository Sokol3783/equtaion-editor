package org.example.menu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.dao.DAO;
import org.example.dao.EquationAnalytics;
import org.example.dao.EquationAnalyticsimpl;
import org.example.dao.EquationDAOimpl;
import org.example.dao.EquationRootDAOImpl;
import org.example.equation.EquationMatcher;
import org.example.equation.EquationParser;

public class ConsoleMenu {

  private static final Scanner scanner = new Scanner(System.in);

  private final Map<String, Runnable> commands;

  private EquationParser parser;

  private final List<EquationMatcher> matchers;

  private ConsoleMenu(List<EquationMatcher> matchers) {
    commands = new LinkedHashMap<>();
    this.matchers = matchers;
  }

  public static ConsoleMenu getInstance(List<EquationMatcher> matchers) {
    ConsoleMenu menu = new ConsoleMenu(matchers);
    menu.addCommand("Enter equation", menu.parseEquation());
    menu.addCommand("Save equation", menu.saveEquation());
    menu.addCommand("Save root", menu.saveEquationRoot());
    menu.addCommand("Print all unique roots", menu.printUniqueRoots());
    menu.addCommand("Print all equations with root", menu.printAllEquationsWithRoot());
    menu.addCommand("Print", menu.printMenu());

    return menu;
  }

  private Runnable printAllEquationsWithRoot() {
    return () -> {
      if (scanner.hasNextDouble()) {
        EquationAnalytics analytics = new EquationAnalyticsimpl();
        List<String> allEquationWithRoot = analytics.findAllEquationWithRoot(
            scanner.nextDouble());
        if (!allEquationWithRoot.isEmpty()) {
          allEquationWithRoot.forEach(System.out::println);
        } else {
          System.out.println("There are no solved equations");
        }
      }
    };
  }

  private Runnable printUniqueRoots() {
    return () -> {
      EquationAnalytics analytics = new EquationAnalyticsimpl();
      List<Double> roots = analytics.findAllUniqueRoots();
      if (!roots.isEmpty()) {
        roots.forEach(System.out::println);
      } else {
        System.out.println("There are no unique roots");
      }
    };
  }

  private Runnable saveEquationRoot() {
    return () -> {
      if (parser != null) {
        System.out.println("Enter root:");
        if (scanner.hasNextDouble()) {
          double i = Double.parseDouble(scanner.nextLine());
          if (parser.isRoot(i)) {
            DAO daoRoots = new EquationRootDAOImpl();
            DAO daoEquation = new EquationRootDAOImpl();
            daoEquation.saveRoot(daoEquation.getIdByValue(parser.getEquation()),
                daoRoots.create(String.valueOf(i)));
          } else {
            System.out.println("It's not valid root!");
          }
        }
      } else {
        System.out.println("First enter equation! Nothing to save");
      }
    };
  }

  private Runnable saveEquation() {
    return () -> {
      if (parser != null) {
        DAO equation = new EquationDAOimpl();
        equation.create(parser.getEquation());
        System.out.println("Equation saved");
      } else {
        System.out.println("First enter equation! Nothing to save");
      }

    };
  }

  private void addCommand(String name, Runnable command) {
    commands.put(name, command);
  }

  public void run() {

    while (true) {
      printMenu().run();
      String input = scanner.nextLine();
      if (input.equals("exit")) {
        break;
      }
      Runnable command = commands.get(input);
      if (command != null) {
        command.run();
        System.out.println("\n Enter next command:");
      } else {
        System.out.println("Unknown command: " + input);
      }
    }
  }

  private Runnable printMenu() {
    return () -> {
      System.out.println("Enter a  command:");
      for (
          String command : commands.keySet()) {
        System.out.println("- " + command);
      }
      System.out.println("- exit");
    };
  }

  private Runnable parseEquation() {
    return () -> {
      if (scanner.hasNext()) {
        String equation = scanner.nextLine();
        if (isValid(equation)) {
          parser = new EquationParser(equation);
        }
      }
    };
  }

  private boolean isValid(String equation) {
    boolean[] valid = {true};
    matchers.forEach(s -> {
      if (!s.matches(equation)) {
        valid[0] = false;
      }
    });
    return valid[0];
  }
}

