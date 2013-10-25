package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import uk.ac.ed.inf.sprouts.external.Boundary;
import uk.ac.ed.inf.sprouts.external.Position;
import uk.ac.ed.inf.sprouts.external.Region;

public class InternalRegion extends ArrayList<InternalBoundary>
    implements
      Comparable<InternalRegion> {

  private static final long serialVersionUID = -183105657365459414L;

  public InternalRegion() {
    super();
  }

  public InternalRegion(Collection<InternalBoundary> boundaries) {
    super();
    this.addAll(boundaries);
  }

  public static InternalRegion fromExternal(Position position, Region region) {
    InternalRegion internalRegion = new InternalRegion();
    for (Boundary boundary : region) {
      InternalBoundary internalBoundary = InternalBoundary.fromExternal(position, boundary);
      internalRegion.add(internalBoundary);
    }
    return internalRegion;
  }

  public static InternalRegion fromString(String string) {
    InternalRegion internalRegion = new InternalRegion();
    String[] boundaries = string.split(InternalConstants.END_OF_BOUNDARY_REGEX);
    for (int i = 0; i < boundaries.length; i++) {
      internalRegion.add(InternalBoundary.fromString(boundaries[i]));
    }
    return internalRegion;
  }

  public List<Vertex> getVertices() {
    ArrayList<Vertex> result = new ArrayList<Vertex>();
    for (InternalBoundary boundary : this) {
      result.addAll(boundary.getVertices());
    }
    return result;
  }

  public int getLives() {
    int lives = 0;
    for (InternalBoundary boundary : this) {
      lives += boundary.getLives();
    }
    return lives;
  }

  @Override
  public String toString() {
    String result = "";
    for (InternalBoundary boundary : this) {
      result += boundary.toString();
    }
    return result + InternalConstants.END_OF_REGION_CHAR;
  }

  @Override
  public int compareTo(InternalRegion o) {
    return toString().compareTo(o.toString());
  }

  public void deleteEmptyBoundaries() {
    for (Iterator<InternalBoundary> it = this.iterator(); it.hasNext();) {
      InternalBoundary boundary = it.next();
      if (boundary.isEmpty()) {
        it.remove();
      }
    }
  }

  public void mergeBoundaries() {
    if (size() < 2) {
      // Too small, nothing to merge
      return;
    }
    for (int i = size() - 1; i > 0; i--) {
      get(0).addAll(get(i));
      remove(i);
    }
  }

  public void sort() {
    for (InternalBoundary boundary : this) {
      boundary.sort();
    }
    Collections.sort(this);
  }

  public void inverseOrientation() {
    for (InternalBoundary boundary : this) {
      Collections.reverse(boundary);
    }
  }
}
