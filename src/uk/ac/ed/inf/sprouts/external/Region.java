package uk.ac.ed.inf.sprouts.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Joiner;

/**
 * Represents a region of the external representation.
 *
 * @author Rimas
 */
public class Region extends ArrayList<Boundary> {

  private static final long serialVersionUID = 2093789352359011100L;

  public Region() {
    super();
  }

  public Region(Collection<Boundary> boundaries) {
    super();
    addAll(boundaries);
  }

  public boolean containsVertex(int vertex) {
    for (Boundary boundary : this) {
      if (boundary.contains(vertex)) {
        return true;
      }
    }
    return false;
  }

  public boolean containsSeveralInstancesOfVertex(int vertex) {
    int count = 0;
    for (Boundary boundary : this) {
      if (boundary.contains(vertex)) {
        count++;
      }
    }
    return count > 1;
  }

  public Boundary getBoundary(int vertex) {
    for (Boundary boundary : this) {
      if (boundary.contains(vertex)) {
        return boundary;
      }
    }
    // The region doesn't have given vertex
    return null;
  }

  public Region getRegionWithVertices(Move move) {
    List<Integer> vertices = move.getBoundariesVertices();
    HashSet<Boundary> set = new HashSet<Boundary>();
    boolean containsSelf = false;
    for (int vertex : vertices) {
      Boundary boundary = getBoundary(vertex);
      if (boundary != null) {
        set.add(boundary);
      } else {
        containsSelf = true;
      }
    }
    if (containsSelf) {
      move.containsSelf();
    }
    return new Region(set);
  }

  public boolean hasMoves(HashMap<Integer, Integer> lives) {
    return (getLives(lives) > 1);
  }

  public int getLives(HashMap<Integer, Integer> lives) {
    int totalLives = 0;
    for (int vertex : lives.keySet()) {
      if (this.containsVertex(vertex)) {
        totalLives += lives.get(vertex);
      }
    }
    return totalLives;
  }

  public boolean hasAliveVerticesExcept(HashMap<Integer, Integer> lives, Integer from, Integer to) {
    int totalLives = 0;
    for (int vertex : lives.keySet()) {
      if (!from.equals(vertex) && !to.equals(vertex) && this.containsVertex(vertex)) {
        totalLives += lives.get(vertex);
      }
    }
    return (totalLives > 0);
  }

  @Override
  public String toString() {
    return Joiner.on("").join(this) + "}";
  }
}
