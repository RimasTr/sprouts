package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Joiner;

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
    throw new IllegalArgumentException("The region doesn't have given vertex");
  }

  public Region getRegionWithVertices(List<Integer> vertices) {
    HashSet<Boundary> set = new HashSet<Boundary>();
    for (int vertex : vertices) {
      set.add(getBoundary(vertex));
    }
    return new Region(set);
  }

  public boolean hasMoves(HashMap<Integer, Integer> lives) {
    int totalLives = 0;
    for (int vertex : lives.keySet()) {
      if (this.containsVertex(vertex)) {
        totalLives += lives.get(vertex);
      }
    }
    return (totalLives > 1);
  }

  @Override
  public String toString() {
    return Joiner.on(";").join(this);
  }
}
