package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomMoveGenerator {

  private final long seed;
  private final Random random;

  public RandomMoveGenerator() {
    super();
    this.seed = System.currentTimeMillis();
    System.out.println("Seed: " + seed);
    random = new Random(seed);
  }

  public RandomMoveGenerator(long seed) {
    super();
    this.seed = seed;
    System.out.println("Seed: " + seed);
    random = new Random(seed);
  }

  public Move generateRandomMove(Position position) {

    Region region = getRandomRegionWithMoves(position);
    List<Integer> vertices = getTwoAliveVertices(position, region);
    Integer from = vertices.get(0);
    Integer to = vertices.get(1);
    Integer createdVertex = position.getNumberOfVertices() + 1;
    Integer regionVertex = null; // TODO: implement
    List<Integer> boundariesVertices = new ArrayList<Integer>();
    boolean invertedFrom = (position.getLives().get(from).equals(1)) ? random.nextBoolean() : false;
    boolean invertedTo = (position.getLives().get(to).equals(1)) ? random.nextBoolean() : false;
    boolean invertedBoundaries = false;

    return new Move(from, invertedFrom, to, invertedTo, createdVertex, regionVertex,
        boundariesVertices, invertedBoundaries);
  }

  private Region getRandomRegionWithMoves(Position position) {
    List<Region> regionsWithMoves = getRegionsWithMoves(position);
    Random generator = new Random(System.currentTimeMillis());
    return regionsWithMoves.get(generator.nextInt(regionsWithMoves.size()));
  }

  private List<Region> getRegionsWithMoves(Position position) {
    List<Region> regionsWithMoves = new ArrayList<Region>();
    for (Region region : position.getRegions()) {
      if (region.hasMoves(position.getLives())) {
        regionsWithMoves.add(region);
      }
    }
    return regionsWithMoves;
  }

  private List<Integer> getAliveVertices(Position position, Region region) {
    List<Integer> result = new ArrayList<Integer>();
    for (Integer vertex : position.getLives().keySet()) {
      if (region.containsVertex(vertex)) {
        for (int i = 0; i < position.getLives().get(vertex); i++) {
          result.add(vertex);
        }
      }
    }
    return result;
  }

  private List<Integer> getTwoAliveVertices(Position position, Region region) {
    List<Integer> result = getAliveVertices(position, region);
    Collections.shuffle(result, random);
    return result.subList(0, 2);
  }
}
