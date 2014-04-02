package uk.ac.ed.inf.sprouts.external;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.ac.ed.inf.sprouts.internal.InternalConstants;
import uk.ac.ed.inf.sprouts.internal.VertexHelper;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * Represents a boundary of the external representation.
 *
 * @author Rimas
 */
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
    if (!InternalConstants.LETTERS_MODE) {
      return Joiner.on(" ").join(this) + ".";
    }
    List<Character> letters = Lists.transform(this, new Function<Integer, Character>() {
      public Character apply(Integer i) {
        return VertexHelper.getSimpleLetter(i);
      }
    });
    return Joiner.on("").join(letters) + ".";
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

  public static boolean meetsClockwiseExpectations(Boundary boundary, int index) {
    return boundary.get((index + boundary.size() - 1) % boundary.size()) <= boundary
        .get((index + 1) % boundary.size());
  }
}
