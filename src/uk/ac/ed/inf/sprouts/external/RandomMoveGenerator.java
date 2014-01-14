package uk.ac.ed.inf.sprouts.external;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class RandomMoveGenerator {

  class Vertex {

    private final int number;
    private final Boundary boundary;
    private final int idInBoundary;

    public Vertex(int number, Boundary boundary, int idInBoundary) {
      super();
      this.number = number;
      this.boundary = boundary;
      this.idInBoundary = idInBoundary;
    }

    public int getNumber() {
      return number;
    }

    public Boundary getBoundary() {
      return boundary;
    }

    public int getIdInBoundary() {
      return idInBoundary;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if ((obj == null) || (obj.getClass() != this.getClass())) {
        return false;
      }
      Vertex other = (Vertex) obj;
      if (other.number != number) {
        return false;
      }
      if (!other.boundary.equals(boundary)) {
        return false;
      }
      if (other.idInBoundary != idInBoundary) {
        return false;
      }
      return true;
    }
  }

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
    List<Vertex> vertices = getTwoAliveVertices(position, region);
    Vertex from = vertices.get(0);
    Vertex to = vertices.get(1);
    Integer createdVertex = position.getNumberOfVertices() + 1;
    Integer regionVertex = null;
    List<Integer> boundariesVertices = new ArrayList<Integer>();
    if (from.getBoundary().equals(to.getBoundary())) {
      // To the same boundary
      if (from.getBoundary().size() == 2) {
        // Need to use @ to specify region.
        regionVertex = getRegionVertex(to, from, position, region);
      }
      boundariesVertices = getBoundariesVertices(from, region);
    }

    boolean invertedFrom =
        (position.getLives().get(from.getNumber()).equals(1)) ? !Boundary
            .meetsClockwiseExpectations(from.getBoundary(), from.getIdInBoundary()) : false;
    boolean invertedTo =
        (position.getLives().get(to.getNumber()).equals(1)) ? !Boundary.meetsClockwiseExpectations(
            to.getBoundary(), to.getIdInBoundary()) : false;
    boolean invertedBoundaries = false;

    return new Move(from.getNumber(), invertedFrom, to.getNumber(), invertedTo, createdVertex,
        regionVertex, boundariesVertices, invertedBoundaries);
  }

  private List<Integer> getBoundariesVertices(Vertex vertex, Region region) {
    HashSet<Integer> result = new HashSet<Integer>();
    for (Boundary boundary : region) {
      if (!boundary.equals(vertex.getBoundary())) {
        if (random.nextBoolean()) {
          result.addAll(boundary);
        }
      }
    }
    return new ArrayList<Integer>(result);
  }

  private Integer getRegionVertex(Vertex to, Vertex from, Position position, Region region) {
    for (Vertex vertex : getAliveVertices(position, region)) {
      if (!vertex.equals(from) && !vertex.equals(to)) {
        return vertex.getNumber();
      }
    }
    return null;
  }

  private Region getRandomRegionWithMoves(Position position) {
    List<Region> regionsWithMoves = getRegionsWithMoves(position);
    return regionsWithMoves.get(random.nextInt(regionsWithMoves.size()));
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

  private List<Vertex> getAliveVertices(Position position, Region region) {
    HashMap<Integer, Integer> lives = new HashMap<Integer, Integer>(position.getLives());
    List<Vertex> result = new ArrayList<Vertex>();
    for (Boundary boundary : region) {
      for (int i = 0; i < boundary.size(); i++) {
        int vertex = boundary.get(i);
        int livesOfVertex = lives.get(vertex);
        for (int livesLeft = livesOfVertex; livesLeft > 0; livesLeft--) {
          result.add(new Vertex(vertex, boundary, i));
          lives.put(vertex, --livesOfVertex);
        }
      }
    }
    return result;
  }

  private List<Vertex> getTwoAliveVertices(Position position, Region region) {
    List<Vertex> result = getAliveVertices(position, region);
    Collections.shuffle(result, random);
    return result.subList(0, 2);
  }
}
