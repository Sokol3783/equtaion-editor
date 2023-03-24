package org.example;

/**
 * Hello world!
 */
public class App {

  public static void main(String[] args) {
    ConsoleMenu menu = createConsoleMenu();
    menu.run();
  }

  private static ConsoleMenu createConsoleMenu() {
    ConsoleMenu menu = new ConsoleMenu();

    return menu;
  }
}
