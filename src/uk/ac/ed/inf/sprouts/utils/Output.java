package uk.ac.ed.inf.sprouts.utils;

import java.util.Arrays;
import java.util.List;

import uk.ac.ed.inf.sprouts.external.Move;

public class Output {

  private static final List<String> DEBUG_TAGS = Arrays.asList("GameInfo", "RegionVertex", "NewPosition");
  //"RegionVertex", "NewPosition"

  public static void out(String string) {
    System.out.println(string);
  }

  public static void move(Move move) {
    System.out.println("MOVE " + move.toNotation());
  }

  public static void won(int id) {
    System.out.println("WON " + id);
  }

  public static void error(String error) {
    System.err.println("ERROR:" + error);
  }

  public static void debug() {
    debug("");
  }

  public static void debug(String string) {
    System.out.println("-- " + string);
  }

  public static void debug(String tag, String string) {
    if (DEBUG_TAGS.contains(tag)) {
      debug("(" + tag + ") " + string);
    }
  }
}
