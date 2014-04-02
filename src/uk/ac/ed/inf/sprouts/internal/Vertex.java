package uk.ac.ed.inf.sprouts.internal;

import uk.ac.ed.inf.sprouts.external.Boundary;
import uk.ac.ed.inf.sprouts.external.Position;

/**
 * Represents a particular instance of the spot in the internal representation.
 *
 * @author Rimas
 */
public class Vertex {

  private final InternalBoundary boundary;
  private char c;
  private int i;

  public Vertex(char c, InternalBoundary boundary) {
    super();
    this.c = c;
    this.i = (int) c;
    this.boundary = boundary;
  }

  public char getC() {
    return c;
  }

  public void setC(char c) {
    this.c = c;
    this.i = (int) c;
  }

  public InternalBoundary getBoundary() {
    return boundary;
  }

  public static Vertex fromExternal(Position position, Boundary boundary, int externalVertex,
      InternalBoundary internalBoundary) {
    return new Vertex(VertexHelper.getSimpleLetter(externalVertex), internalBoundary);
  }

  public int getLives() {
    switch (c) {
      case '0':
        return 3;
      case '1':
        return 2;
      case '2':
        return 1;
      case '3':
        return 0;
      default:
        // All letters
        return 1;
    }
  }

  @Override
  public String toString() {
    return String.valueOf(c); // + "(" + boundary.get(0).getC() + ")";
  }

  public char toAbstractChar() {
    if (isAbstract()) {
      return c;
    }
    if (isUppercase()) {
      return InternalConstants.FIRST_UPPERCASE_LETTER;
    }
    return InternalConstants.FIRST_LOWERCASE_LETTER;
  }

  @Override
  public int hashCode() {
    return i;
  }

  @Override
  public boolean equals(Object v) {
    return this.hashCode() == v.hashCode();
  }

  public boolean isAbstract() {
    return InternalConstants.ABSTRACT_CHARS.contains(c);
  }

  public boolean isUppercase() {
    return Character.isUpperCase(c);
  }
}
