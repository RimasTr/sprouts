package uk.ac.ed.inf.sprouts;

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

  public void makeMove(Move move) {
    adjustLives(move);

    Region region = findRegion(move);
    System.out.println("Move in the region: " + region);

    Boundary fromBoundary = region.getBoundary(move.getFrom());
    Boundary toBoundary = region.getBoundary(move.getTo());

    if (fromBoundary.equals(toBoundary)) {
      System.out.println("Move in the same boundary: " + fromBoundary);
    } else {
      System.out.println("Move in different boundaries:\n" + fromBoundary + "\n" + toBoundary);
      Boundary newBoundary = Boundary.joinTwoBoundaries(fromBoundary, toBoundary, move);
      region.remove(fromBoundary);
      region.remove(toBoundary);
      region.add(newBoundary);
    }
  }

  public List<Region> getRegions() {
    return regions;
  }


  public int getNumberOfVertices() {
    return numberOfVertices;
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
    possibleRegions.retainAll(findRegionsWithVertex(move.getFrom()));
    if (possibleRegions.size() > 1) {
      // Region vertex must also be in the same region:
      possibleRegions.retainAll(findRegionsWithVertex(move.getRegionVertex()));
    }
    // TODO: assert one and only one
    // Only one possible combination:
    return possibleRegions.get(0);
  }
}
