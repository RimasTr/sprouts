package uk.ac.ed.inf.sprouts;

public enum GameType {
  NORMAL, MISERE;

  public static GameType fromSymbol(String symbol) {
    if (symbol.equals("-")) {
      return MISERE;
    }
    // + or anything else
    return NORMAL;
  }
}
