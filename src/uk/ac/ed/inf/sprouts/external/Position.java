package uk.ac.ed.inf.sprouts.external;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.ed.inf.sprouts.utils.Output;

import com.google.common.base.Joiner;

/**
 * Represents a position in the external representation.
 *
 * @author Rimas
 */
public class Position implements Serializable {

  private static final long serialVersionUID = 2071824388766229559L;

  private List<Region> regions;
  private HashMap<Integer, Integer> lives;
  private int numberOfVertices;

  public Position(int initialVertices) {
    numberOfVertices = initialVertices;
    regions = new ArrayList<Region>();
    lives = new HashMap<Integer, Integer>();
    Region initialRegion = new Region();
    for (int i = 1; i <= initialVertices; i++) {
      initialRegion.add(new Boundary(i));
      lives.put(i, 3);
    }
    regions.add(initialRegion);
  }

  public boolean isLost() {
    for (Region region : regions) {
      if (region.hasMoves(lives)) {
        return false;
      }
    }
    return true;
  }

  public void makeMove(Move oldMove) {
    Move move = Move.fromString(oldMove.toNotation());
    if (move.getInvertedBoundaries()) {
      move.removeInversion();
    }
    adjustLives(move);

    numberOfVertices++;

    Region region = findRegion(move);
    // Output.debug("Move in the region: " + region);

    Boundary fromBoundary = region.getBoundary(move.getFrom());
    Boundary toBoundary = region.getBoundary(move.getTo());

    if (fromBoundary.equals(toBoundary)) {
      // Output.debug("Move in the same boundary: " + fromBoundary);
      region.remove(fromBoundary);
      Region firstRegion = region.getRegionWithVertices(move);

      Region secondRegion = new Region();
      secondRegion.addAll(region);
      secondRegion.removeAll(firstRegion);

      int fromId = Boundary.findVertexId(fromBoundary, move.getFrom(), move.getInvertedFrom());
      int toId = Boundary.findVertexId(fromBoundary, move.getTo(), move.getInvertedTo());

      if (fromId > toId) {
        int temp = fromId;
        fromId = toId;
        toId = temp;
        move.invertBoundaries();
      }

      Boundary firstBoundary = new Boundary();
      firstBoundary.addAll(fromBoundary.subList(0, fromId + 1));
      firstBoundary.add(move.getCreatedVertex());
      if (fromBoundary.size() > 1) {
        firstBoundary.addAll(fromBoundary.subList(toId, fromBoundary.size()));
      }

      Boundary secondBoundary = new Boundary();
      secondBoundary.addAll(fromBoundary.subList(fromId, toId + 1));
      secondBoundary.add(move.getCreatedVertex());

      // Output.debug("First boundary: " + firstBoundary);
      // Output.debug("Second boundary: " + secondBoundary);
      if (!needsInvertion(move)) {
        firstRegion.add(firstBoundary);
        secondRegion.add(secondBoundary);
      } else {
        firstRegion.add(secondBoundary);
        secondRegion.add(firstBoundary);
      }

      regions.remove(region);
      regions.add(firstRegion);
      regions.add(secondRegion);

      // Output.debug("Boundaries in the first region: " + firstRegion);
      // Output.debug("Boundaries in the second region: " + secondRegion);
    } else {
      // Output.debug("Move in different boundaries:\n" + fromBoundary + "\n" + toBoundary);
      Boundary newBoundary = Boundary.joinTwoBoundaries(fromBoundary, toBoundary, move);
      region.remove(fromBoundary);
      region.remove(toBoundary);
      region.add(newBoundary);
    }
  }

  private boolean needsInvertion(Move move) {
    if (move.getFrom().equals(move.getTo())) {
      return !move.getContainsSelf();
    }
    return move.getInvertedBoundaries();
  }

  public List<Region> getRegions() {
    return regions;
  }


  public int getNumberOfVertices() {
    return numberOfVertices;
  }

  public HashMap<Integer, Integer> getLives() {
    return lives;
  }

  @Override
  public String toString() {
    return Joiner.on("\n").join(regions) + "\n" + "Lives: " + lives.toString() + "\n";

  }

  private List<Region> findRegionsWithVertex(Integer vertex) {
    List<Region> result = new ArrayList<Region>();
    for (Region region : regions) {
      if (region.containsVertex(vertex)) {
        result.add(region);
      }
    }
    return result;
  }

  private void adjustLives(Move move) {
    // TODO: assert have enough lives
    lives.put(move.getFrom(), lives.get(move.getFrom()) - 1);
    lives.put(move.getTo(), lives.get(move.getTo()) - 1);
    // TODO: assert correct vertex created
    lives.put(move.getCreatedVertex(), 1);
  }

  private Region findRegion(Move move) {
    // Regions with "from" vertex:
    List<Region> possibleRegions = findRegionsWithVertex(move.getFrom());
    // Vertex "to" must be in the same region:
    possibleRegions.retainAll(findRegionsWithVertex(move.getTo()));
    Output.debug("RegionVertex", "Looking for regions");
    if (possibleRegions.size() == 1) {
      return possibleRegions.get(0);
    }
    Output.debug("RegionVertex", "More than one");
    if (move.getRegionVertex() != null) {
      // Region vertex must also be in the same region:
      possibleRegions.retainAll(findRegionsWithVertex(move.getRegionVertex()));
    }
    if (possibleRegions.size() == 1) {
      return possibleRegions.get(0);
    }
    Output.debug("RegionVertex", "Still more than one");
    // Check if some points in the boundaries list are in only one of the regions:
    for (int vertex : move.getBoundariesVertices()) {
      possibleRegions.retainAll(findRegionsWithVertex(vertex));
    }
    if (possibleRegions.size() == 1) {
      return possibleRegions.get(0);
    }
    // Still not clear, try ! symbols:
    // TODO: maybe check the second vertex as well? But the first is enough, actually...
    Output.debug("RegionVertex", "Trying ! symbols");
    Output.debug("RegionVertex", "Possible regions: " + possibleRegions.toString());
    Boundary possibleBoundary = possibleRegions.get(0).getBoundary(move.getFrom());
    Output.debug("RegionVertex", "Possible boundary: " + possibleBoundary.toString());
    int vertexId = Boundary.findVertexId(possibleBoundary, move.getFrom(), move.getInvertedFrom());
    if (possibleBoundary.size() != 2) {
      if (Boundary.meetsClockwiseExpectations(possibleBoundary, vertexId) == !move
          .getInvertedFrom()) {
        return possibleRegions.get(0);
      } else {
        return possibleRegions.get(1);
      }
    }
    if (move.getRegionVertex() == null) {
      Output.debug("RegionVertex", "And still more than one");
      // Take into account that the region vertex was empty
      boolean first =
          !possibleRegions.get(0).hasAliveVerticesExcept(lives, move.getFrom(), move.getTo());
      boolean second =
          !possibleRegions.get(1).hasAliveVerticesExcept(lives, move.getFrom(), move.getTo());
      if (!(first && second)) { // If both have no alive vertices - no use for us.
        if (first) {
          return possibleRegions.get(0);
        }
        if (second) {
          return possibleRegions.get(1);
        }
      }
    }
    if (possibleRegions.size() < 1) {
      Output.error("Could not find the region");
      return null;
    }
    if (possibleRegions.size() > 1) {
      Output.debug("RegionVertex", "Warning, might be ambiguos");
      // Actually it doesn't matter in which region the move is going to be made in these cases.
    }
    return possibleRegions.get(0);
  }

  @SuppressWarnings("unchecked")
  public Position clone() {
    try {
      Position clone = (Position) super.clone();
      clone.regions = (List<Region>) ((ArrayList<Region>) regions).clone();
      clone.lives = (HashMap<Integer, Integer>) lives.clone();
      clone.numberOfVertices = numberOfVertices;
      return clone;
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }
}
