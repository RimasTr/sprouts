package uk.ac.ed.inf.sprouts.utils;

import uk.ac.ed.inf.sprouts.external.Move;

public class Output {

  public static void out(String string) {
    System.out.println(string);
    System.out.flush();
  }

  public static void move(Move move) {
    System.out.println("MOVE " + move.toNotation());
    System.out.flush();
  }

  public static void won(int id) {
    System.out.println("WON " + id);
    System.out.flush();
  }

  public static void debug() {
    debug("");
  }

  public static void debug(String string) {
    System.out.println("-- " + string);
    System.out.flush();
  }
}
