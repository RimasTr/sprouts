package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ed.inf.sprouts.external.Boundary;
import uk.ac.ed.inf.sprouts.external.Position;

public class InternalBoundary extends ArrayList<Vertex> implements Comparable<InternalBoundary> {

  private static final long serialVersionUID = 7283921350025148014L;

  public static InternalBoundary fromExternal(Position position, Boundary boundary) {
    InternalBoundary internalBoundary = new InternalBoundary();
    for (int externalVertex : boundary) {
      Vertex vertex = Vertex.fromExternal(position, boundary, externalVertex, internalBoundary);
      if (vertex != null) {
        internalBoundary.add(vertex);
      }
    }
    return internalBoundary;
  }

  public static InternalBoundary fromString(String string) {
    InternalBoundary internalBoundary = new InternalBoundary();
    for (int i = 0; i < string.length(); i++) {
      internalBoundary.add(new Vertex(string.charAt(i), internalBoundary));
    }
    return internalBoundary;
  }

  public int getLives() {
    int lives = 0;
    for (Vertex vertex : this) {
      lives += vertex.getLives();
    }
    return lives;
  }

  @Override
  public String toString() {
    return toString(false);
  }

  public String toAbstractString() {
    return toString(true);
  }

  public String toString(boolean isAbstract) {
    String result = "";
    for (Vertex vertex : this) {
      if (isAbstract) {
        result += vertex.toAbstractString();
      } else {
        result += vertex;
      }
    }
    return result + InternalConstants.END_OF_BOUNDARY_CHAR;
  }

  public String toString(int start) {
    String result = "";
    for (int i = 0; i < size(); i++) {
      result += this.get((start + i) % size()).toAbstractString();
    }
    return result + InternalConstants.END_OF_BOUNDARY_CHAR;
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
    // TODO: make more efficient
    if (size() < 2) {
      // Nothing to sort
      return;
    }
    String minimalString = this.toString(0);
    int minimalIndex = 0;
    for (int i = 1; i < size(); i++) {
      String current = this.toString(i);
      if (current.compareTo(minimalString) < 0) {
        minimalString = current;
        minimalIndex = i;
      }
    }
    for (int i = 0; i < minimalIndex; i++) {
      Vertex vertex = get(0);
      remove(0);
      add(vertex);
    }
  }

  public static InternalBoundary joinTwoBoundaries(InternalBoundary fromBoundary,
      InternalBoundary toBoundary, InternalMove move) {
    int fromIndex = move.getFrom().getVertexIndex();
    int toIndex = move.getTo().getVertexIndex();

    InternalBoundary newBoundary = new InternalBoundary();
    newBoundary.addAll(fromBoundary.subList(0, fromIndex + 1));
    newBoundary.add(new Vertex(InternalConstants.TEMP_NEW, newBoundary));
    if (toBoundary.size() > 1) {
      newBoundary.addAll(toBoundary.subList(toIndex, toBoundary.size()));
    }
    newBoundary.addAll(toBoundary.subList(0, toIndex + 1));
    newBoundary.add(new Vertex(InternalConstants.TEMP_NEW, newBoundary));
    if (fromBoundary.size() > 1) {
      newBoundary.addAll(fromBoundary.subList(fromIndex, fromBoundary.size()));
    }
    return newBoundary;
  }
}
