package uk.ac.ed.inf.sprouts.utils;

public class Output {

  public static void out(String string) {
    System.out.println(string);
  }

  public static void debug() {
    debug("");
  }

  public static void debug(String string) {
    System.out.println(" -- " + string);
  }
}
