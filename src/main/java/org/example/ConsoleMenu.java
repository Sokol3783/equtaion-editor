package org.example;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {

  private final Map<String, Runnable> commands;

  public ConsoleMenu() {
    commands = new LinkedHashMap<>();
  }

  public void addCommand(String name, Runnable command) {
    commands.put(name, command);
  }

  public void run() {

    commands.put("print", printMenu());
    commands.put("exit", () -> {
    });

    Scanner scanner = new Scanner(System.in);
    while (true) {
      printMenu().run();
      String input = scanner.nextLine();
      if (input.equals("exit")) {
        break;
      }
      Runnable command = commands.get(input);
      if (command != null) {
        command.run();
      } else {
        System.out.println("Unknown command: " + input);
      }
    }
  }

  private Runnable printMenu() {
    return () -> {
      System.out.println("Enter a command:");
      for (
          String command : commands.keySet()) {
        System.out.println("- " + command);
      }
      System.out.println("- exit");
    };
  }
}
