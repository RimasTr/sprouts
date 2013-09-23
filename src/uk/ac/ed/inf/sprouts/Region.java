package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;

import com.google.common.base.Joiner;

public class Region extends ArrayList<Boundary> {

  private static final long serialVersionUID = 2093789352359011100L;

  public boolean containsVertex(int vertex) {
    for (Boundary boundary : this) {
      if (boundary.contains(vertex)) {
        return true;
      }
    }
    return false;
  }

  public Boundary getBoundary(int vertex) {
    for (Boundary boundary : this) {
      if (boundary.contains(vertex)) {
        return boundary;
      }
    }
    throw new IllegalArgumentException("The region doesn't have given vertex");
  }

  @Override
  public String toString() {
    return Joiner.on(";").join(this);
  }
}
