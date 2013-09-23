package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Joiner;

public class Boundary extends ArrayList<Integer> {

  private static final long serialVersionUID = 3388951717137457680L;

  public Boundary() {
    super();
  }

  public Boundary(int i) {
    super();
    add(i);
  }

  public Boundary(Collection<Integer> vertices) {
    super();
    addAll(vertices);
  }

  @Override
  public String toString() {
    return "(" + Joiner.on(",").join(this) + ")";
  }

  public static Boundary joinTwoBoundaries(Boundary fromBoundary, Boundary toBoundary, Move move) {
    int fromIndex = findVertexId(fromBoundary, move.getFrom(), move.getInvertedFrom());
    int toIndex = findVertexId(toBoundary, move.getTo(), move.getInvertedTo());

    Boundary newBoundary = new Boundary();
    newBoundary.addAll(fromBoundary.subList(0, fromIndex + 1));
    newBoundary.add(move.getCreatedVertex());
    if (toBoundary.size() > 1) {
      newBoundary.addAll(toBoundary.subList(toIndex, toBoundary.size()));
    }
    newBoundary.addAll(toBoundary.subList(0, toIndex + 1));
    newBoundary.add(move.getCreatedVertex());
    if (fromBoundary.size() > 1) {
      newBoundary.addAll(fromBoundary.subList(fromIndex, fromBoundary.size()));
    }
    return newBoundary;
  }

  public static int findVertexId(Boundary boundary, int vertex, boolean inverted) {
    int firstOccurence = boundary.indexOf(vertex);
    if (Collections.frequency(boundary, vertex) == 1) {
      // Only one point, cool
      return firstOccurence;
    } else {
      // Need to choose from two:
      if (meetsClockwiseExpectations(boundary, firstOccurence) == !inverted) {
        return firstOccurence;
      } else {
        return boundary.lastIndexOf(vertex);
      }
    }
  }

  private static boolean meetsClockwiseExpectations(Boundary boundary, int index) {
    return boundary.get((index - 1) % boundary.size()) < boundary
        .get((index + 1) % boundary.size());
  }
}
