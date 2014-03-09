package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.ed.inf.sprouts.external.Boundary;
import uk.ac.ed.inf.sprouts.external.Position;

public class InternalBoundary extends ArrayList<Vertex> implements Comparable<InternalBoundary> {

  private static final long serialVersionUID = 7283921350025148014L;

  private String abstractStringRepresentation;

  public static InternalBoundary fromExternal(Position position, Boundary boundary) {
    InternalBoundary internalBoundary = new InternalBoundary();
    for (int externalVertex : boundary) {
      if (position.getLives().get(externalVertex) > 0) {
        Vertex vertex = Vertex.fromExternal(position, boundary, externalVertex, internalBoundary);
        if (vertex != null) {
          internalBoundary.add(vertex);
        }
      }
    }
    // internalBoundary.compile();
    return internalBoundary;
  }

  public static InternalBoundary fromString(String string) {
    InternalBoundary internalBoundary = new InternalBoundary();
    for (int i = 0; i < string.length(); i++) {
      internalBoundary.add(new Vertex(string.charAt(i), internalBoundary));
    }
    // internalBoundary.compile();
    return internalBoundary;
  }

  public int getLives() {
    int lives = 0;
    for (Vertex vertex : this) {
      lives += vertex.getLives();
    }
    return lives;
  }

  public void compile() {
    abstractStringRepresentation = toAbstractStringFull();
  }

  public String toAbstractString() {
    return abstractStringRepresentation;
  }

  @Override
  public String toString() {
    char[] result = new char[this.size() + 1];
    int i = 0;
    for (Vertex vertex : this) {
      result[i++] = vertex.getC();
    }
    result[i] = InternalConstants.END_OF_BOUNDARY_CHAR;
    return new String(result);
  }

  public String toAbstractStringFull() {
    char[] result = new char[this.size() + 1];
    int i = 0;
    for (Vertex vertex : this) {
      result[i++] = vertex.toAbstractChar();
    }
    result[i] = InternalConstants.END_OF_BOUNDARY_CHAR;
    return new String(result);
  }

  @Override
  public int compareTo(InternalBoundary o) {
    return toAbstractString().compareTo(o.toAbstractString());
  }

  public List<Vertex> getVertices() {
    return this;
  }

  public boolean oneAfterTheOther(Vertex firstVertex, Vertex secondVertex) {
    int distance = lastIndexOf(secondVertex) - indexOf(firstVertex);
    return distance == 1 || (distance == size() - 1);
  }

  public void sort() {
    if (size() < 2) {
      // Nothing to sort
      return;
    }
    int minimalIndex = computeBestRotationIndex();
    Collections.rotate(this, minimalIndex);
  }

  @Override
  public boolean remove(Object o) {
    boolean b = super.remove(o);
    // compile();
    return b;
  }

  @Override
  public Vertex remove(int index) {
    Vertex v = super.remove(index);
    // this.compile();
    return v;
  }

  public int computeBestRotationIndex() {
    // Uses Booth's algorithm
    // From http://en.wikipedia.org/wiki/Lexicographically_minimal_string_rotation
    String S = toAbstractString();
    int n = S.length();
    char[] s = (S + S).toCharArray();
    int f[] = new int[2 * n];
    for (int i = 0; i < 2 * n; i++) {
      f[i] = -1;
    }
    int k = 0;
    for (int j = 1; j < 2 * n; j++) {
      int i = f[j - k - 1];
      while (i != -1 && s[j] != s[k + i + 1]) {
        if (s[j] < s[k + i + 1]) {
          k = j - i - 1;
        }
        i = f[i];
      }
      if (i == -1 && s[j] != s[k + i + 1]) {
        if (s[j] < s[k + i + 1]) {
          k = j;
        }
        f[j - k] = -1;
      } else {
        f[j - k] = i + 1;
      }
    }
    return k;
  }

  public static InternalBoundary joinTwoBoundaries(InternalBoundary fromBoundary,
      InternalBoundary toBoundary, InternalMove move) {
    int fromIndex = move.getFrom().getVertexIndex();
    int toIndex = move.getTo().getVertexIndex();

    InternalBoundary newBoundary = new InternalBoundary();
    newBoundary.addAll(fromBoundary.subList(0, fromIndex + 1));
    newBoundary.add(new Vertex(InternalConstants.TEMP_NEW, newBoundary));
    if (needsEnd(toBoundary)) {
      newBoundary.addAll(toBoundary.subList(toIndex, toBoundary.size()));
    }
    newBoundary.addAll(toBoundary.subList(0, toIndex + 1));
    newBoundary.add(new Vertex(InternalConstants.TEMP_NEW, newBoundary));
    if (needsEnd(fromBoundary)) {
      newBoundary.addAll(fromBoundary.subList(fromIndex, fromBoundary.size()));
    }
    // newBoundary.compile();
    return newBoundary;
  }

  private static boolean needsEnd(InternalBoundary boundary) {
    // TODO: does it really work?
    return boundary.size() > 1 || (boundary.size() == 1 && boundary.get(0).isUppercase());
  }
}
