package uk.ac.ed.inf.sprouts.internal;

public class VertexHelper {

  public static char getSimpleLetter(int externalVertex) {
    return (char) ((int) 'a' + externalVertex - 1);
  }
}
