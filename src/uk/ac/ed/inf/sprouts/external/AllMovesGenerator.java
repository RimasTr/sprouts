package uk.ac.ed.inf.sprouts.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllMovesGenerator {

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
      // if (other.idInBoundary != idInBoundary) {
      // return false;
      // }
      return true;
    }
  }

  public Set<Move> generateAllMoves(Position position) {
    Set<Move> moves = new HashSet<Move>();
    HashMap<Integer, Integer> lives = position.getLives();
    for (Region region : getRegionsWithMoves(position)) {
      List<Vertex> vertices = getAliveVertices(position, region);

      Integer createdVertex = position.getNumberOfVertices() + 1;
      List<Integer> boundariesVertices = new ArrayList<Integer>();
      for (int fromId = 0; fromId < vertices.size(); fromId++) {
        Vertex from = vertices.get(fromId);
        if (lives.get(from.number) <= 0) {
          continue;
        }
        for (int toId = fromId + 1; toId < vertices.size(); toId++) {
          Vertex to = vertices.get(toId);
          if (lives.get(to.number) <= 0) {
            continue;
          }
          if (from.number == to.number && lives.get(from.number) < 2) {
            continue;
          }
          boolean invertedFrom =
              (position.getLives().get(from.getNumber()).equals(1)) ? !Boundary
                  .meetsClockwiseExpectations(from.getBoundary(), from.getIdInBoundary()) : false;
          boolean invertedTo =
              (position.getLives().get(to.getNumber()).equals(1)) ? !Boundary
                  .meetsClockwiseExpectations(to.getBoundary(), to.getIdInBoundary()) : false;
          boolean invertedBoundaries = false;
          Integer regionVertex = null;
          if (from.getBoundary().equals(to.getBoundary())) {
            // To the same boundary
            if (from.getBoundary().size() == 2) {
              // Need to use @ to specify region.
              regionVertex = getRegionVertex(to, from, position, region);
              if (regionVertex != null && regionVertex.equals(from.getNumber())) {
                throw new RuntimeException("Blah");
              }
            }
            Set<List<Integer>> allBoundaryVerticesCombinations =
                getAllBoundaryVerticesCombinations(from, to, region);
            for (List<Integer> currentBoundaryVertices : allBoundaryVerticesCombinations) {
              moves.add(new Move(from.getNumber(), invertedFrom, to.getNumber(), invertedTo,
                  createdVertex, regionVertex, currentBoundaryVertices, false));
              if (from.number != to.number && allBoundaryVerticesCombinations.size() > 1) {
                moves.add(new Move(from.getNumber(), invertedFrom, to.getNumber(), invertedTo,
                    createdVertex, regionVertex, currentBoundaryVertices, true));
              }
            }
          } else {
            moves.add(new Move(from.getNumber(), invertedFrom, to.getNumber(), invertedTo,
                createdVertex, regionVertex, boundariesVertices, invertedBoundaries));
          }
        }
      }
    }
    return moves;
  }

  private Set<List<Integer>> getAllBoundaryVerticesCombinations(Vertex from, Vertex to,
      Region region) {
    Set<List<Integer>> result = new HashSet<List<Integer>>();
    List<Boundary> possibleBoundaries = new ArrayList<Boundary>(region);
    // possibleBoundaries.remove(from.getBoundary());
    for (List<Boundary> boundaries : allPossibleSplits(possibleBoundaries)) {
      HashSet<Integer> currentList = new HashSet<Integer>();
      for (Boundary boundary : boundaries) {
        currentList.addAll(boundary);
      }
      currentList.remove(from.number);
      currentList.remove(to.number);
      result.add(new ArrayList<Integer>(currentList));
    }
    return result;
  }

  private Integer getRegionVertex(Vertex to, Vertex from, Position position, Region region) {
    for (Vertex vertex : getAllAliveVertices(position, region)) {
      if (vertex.getNumber() != from.getNumber() && vertex.getNumber() != to.getNumber()) {
        return vertex.getNumber();
      }
    }
    return null;
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

  private List<Vertex> getAllAliveVertices(Position position, Region region) {
    List<Vertex> result = new ArrayList<Vertex>();
    for (Boundary boundary : region) {
      for (int i = 0; i < boundary.size(); i++) {
        int vertex = boundary.get(i);
        result.add(new Vertex(vertex, boundary, i));
      }
    }
    return result;
  }

  private List<Vertex> getAliveVertices(Position position, Region region) {
    List<Vertex> result = new ArrayList<Vertex>();
    int addedZeros = 0;
    for (Boundary boundary : region) {
      if (boundary.size() == 1) {
        if (addedZeros >= 2) {
          continue;
        }
        addedZeros++;
      }
      for (int i = 0; i < boundary.size(); i++) {
        int vertex = boundary.get(i);
        result.add(new Vertex(vertex, boundary, i));
        // Two copies to make sure we try to connect point to itself
        result.add(new Vertex(vertex, boundary, i));
      }
    }
    return result;
  }

  private static List<List<Boundary>> allPossibleSplits(List<Boundary> boundaries) {
    List<List<Boundary>> possibleSplits = new ArrayList<List<Boundary>>();
    if (boundaries.isEmpty()) {
      possibleSplits.add(new ArrayList<Boundary>());
      return possibleSplits;
    }
    List<Boundary> list = new ArrayList<Boundary>(boundaries);
    Boundary head = list.get(0);
    List<Boundary> rest = new ArrayList<Boundary>(list.subList(1, list.size()));
    for (List<Boundary> set : allPossibleSplits(rest)) {
      List<Boundary> newSet = new ArrayList<Boundary>();
      newSet.add(head);
      newSet.addAll(set);
      possibleSplits.add(newSet);
      possibleSplits.add(set);
    }
    return possibleSplits;
  }
}
