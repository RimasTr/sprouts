package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import uk.ac.ed.inf.sprouts.external.Boundary;
import uk.ac.ed.inf.sprouts.external.Position;
import uk.ac.ed.inf.sprouts.external.Region;

import com.google.common.base.Joiner;

/**
 * Represents a region in the internal representation.
 *
 * @author Rimas
 */
public class InternalRegion extends ArrayList<InternalBoundary> {

  private static final long serialVersionUID = -183105657365459414L;

  private String stringRepresentation;
  private String abstractStringRepresentation;

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
    StringTokenizer boundaries =
        new StringTokenizer(string, String.valueOf(InternalConstants.END_OF_BOUNDARY_CHAR));
    while (boundaries.hasMoreTokens()) {
      internalRegion.add(InternalBoundary.fromString(boundaries.nextToken()));
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
    return Joiner.on("").join(this) + InternalConstants.END_OF_REGION_CHAR;
  }

  public String toAbstractString() {
    return abstractStringRepresentation;
  }

  public String toNormalString() {
    return stringRepresentation;
  }

  public String toAbstractStringFull() {
    StringBuilder result = new StringBuilder();
    for (InternalBoundary boundary : this) {
      result.append(boundary.toAbstractStringFull());
    }
    result.append(InternalConstants.END_OF_REGION_CHAR);
    return result.toString();
  }

  public String toStringFull() {
    StringBuilder result = new StringBuilder();
    for (InternalBoundary boundary : this) {
      result.append(boundary.toStringFull());
    }
    result.append(InternalConstants.END_OF_REGION_CHAR);
    return result.toString();
  }

  public String toString(boolean asAbstract) {
    return asAbstract ? toAbstractString() : toNormalString();
  }

  public void compile() {
    stringRepresentation = toStringFull();
    abstractStringRepresentation = toAbstractStringFull();
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

  public void sort(boolean asAbstract) {
    for (InternalBoundary boundary : this) {
      boundary.compile();
      boundary.sort(asAbstract);
      boundary.compile();
    }
    Collections.sort(this, new InternalBoundaryComparator(asAbstract));
  }

  public void inverseOrientation() {
    for (InternalBoundary boundary : this) {
      Collections.reverse(boundary);
    }
  }
}
