package uk.ac.ed.inf.sprouts.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Joiner;

public class Position {

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

  public void makeMove(Move move) {
//    System.out.println("Making move: " + move);

    adjustLives(move);

    numberOfVertices++;

    Region region = findRegion(move);
    System.out.println("Move in the region: " + region);

    Boundary fromBoundary = region.getBoundary(move.getFrom());
    Boundary toBoundary = region.getBoundary(move.getTo());

    if (fromBoundary.equals(toBoundary)) {
//      System.out.println("Move in the same boundary: " + fromBoundary);
      region.remove(fromBoundary);
      // TODO: handle inversion
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

//      System.out.println("First boundary: " + firstBoundary);
//      System.out.println("Second boundary: " + secondBoundary);
      if (needsInvertion(move)) {
        firstRegion.add(firstBoundary);
        secondRegion.add(secondBoundary);
      } else {
        firstRegion.add(secondBoundary);
        secondRegion.add(firstBoundary);
      }

      regions.remove(region);
      regions.add(firstRegion);
      regions.add(secondRegion);

      // System.out.println("Boundaries in the first region: " + firstRegion);
      // System.out.println("Boundaries in the second region: " + secondRegion);
    } else {
//      System.out.println("Move in different boundaries:\n" + fromBoundary + "\n" + toBoundary);
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
    if (possibleRegions.size() > 1) {
      // TODO: better if structure
      if (move.getRegionVertex() != null) {
        // Region vertex must also be in the same region:
        possibleRegions.retainAll(findRegionsWithVertex(move.getRegionVertex()));
      } else {
        // Check if some points in the boundaries list are in only one of the regions:
        for (int vertex : move.getBoundariesVertices()) {
          possibleRegions.retainAll(findRegionsWithVertex(vertex));
        }
        // TODO: empty also means that there are no alive vertices in that region, should check it
        // as well
        if (possibleRegions.size() > 1) {
          // Still not clear, try ! symbols:
          Boundary possibleBoundary = possibleRegions.get(0).getBoundary(move.getFrom());
          int vertexId =
              Boundary.findVertexId(possibleBoundary, move.getFrom(), move.getInvertedFrom());
          if (possibleBoundary.size() != 2) {
            if (Boundary.meetsClockwiseExpectations(possibleBoundary, vertexId)) {
              return possibleRegions.get(1);
            } else {
              return possibleRegions.get(0);
            }
          } else {
            // Take into account that the region vertex was empty
            if (possibleRegions.get(0).hasAliveVerticesExcept(lives, move.getFrom(), move.getTo())) {
              return possibleRegions.get(1);
            }
            System.out.println("Warning, might be ambiguos");
          }
        }
      }
    }
    // TODO: assert one and only one
    // Only one possible combination:
    return possibleRegions.get(0);
  }
}
