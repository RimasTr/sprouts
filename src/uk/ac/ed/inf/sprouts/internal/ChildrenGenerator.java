package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uk.ac.ed.inf.sprouts.ImprovedMoveBruteforcer;

import com.google.common.collect.Collections2;

public class ChildrenGenerator {

  private final InternalPosition position;

  public ChildrenGenerator(InternalPosition position) {
    this.position = position;
  }

  public Set<InternalPosition> generateAllChildren() {
    HashSet<InternalPosition> children = new HashSet<InternalPosition>();
    List<InternalMove> moves = generateInternalMoves();
    // TODO: remove
    String posString = position.toString();
    for (InternalMove move : moves) {
      if (posString.equals(ImprovedMoveBruteforcer.DEBUG_POSITION)) {
        System.out.println("Move :" + move);
        System.out.println("Positions :" + generateAllPossiblePositionsAfter(move));
      }
      children.addAll(generateAllPossiblePositionsAfter(move));
    }
    return children;
  }

  public Set<String> generateAllChildrenStrings() {
    return new HashSet<String>(Collections2.transform(generateAllChildren(),
        InternalPosition.toString));
  }

  private List<InternalMove> generateInternalMoves() {
    List<InternalMove> internalMoves = new ArrayList<InternalMove>();
    List<ArrayList<VertexInfo>> vertexInfos = getVertexInfos();
    for (ArrayList<VertexInfo> regionVertices : vertexInfos) {
      for (int i = 0; i < regionVertices.size(); i++) {
        for (int j = i; j < regionVertices.size(); j++) {
          VertexInfo from = regionVertices.get(i);
          VertexInfo to = regionVertices.get(j);
          if (i == j && from.getVertex().getLives() < 2) {
            // Can't connect to itself if it has only 1 life
            continue;
          } else if (from.getVertex().getC() == to.getVertex().getC()
              && !from.getVertex().isAbstract() && !to.getVertex().isAbstract()) {
            // Can't connect non-abstract vertices (a-zA-Z) to other occurrence of them
            continue;
          }
          internalMoves.add(new InternalMove(from, to));
        }
      }
    }
    return internalMoves;
  }

  private List<ArrayList<VertexInfo>> getVertexInfos() {
    ArrayList<ArrayList<VertexInfo>> result = new ArrayList<ArrayList<VertexInfo>>();
    for (int regionIndex = 0; regionIndex < position.size(); regionIndex++) {
      ArrayList<VertexInfo> regionResult = new ArrayList<VertexInfo>();
      InternalRegion region = position.get(regionIndex);
      int foundZeros = 0;
      for (int boundaryIndex = 0; boundaryIndex < region.size(); boundaryIndex++) {
        InternalBoundary boundary = region.get(boundaryIndex);
        for (int vertexIndex = 0; vertexIndex < boundary.size(); vertexIndex++) {
          Vertex vertex = boundary.get(vertexIndex);
          if (vertex.getLives() >= 3) {
            // zero
            if (++foundZeros > 2) {
              // Already have 2 zeros in this region, don't add more
              continue;
            }
          }
          regionResult.add(new VertexInfo(vertex, regionIndex, boundaryIndex, vertexIndex));
        }
      }
      result.add(regionResult);
    }
    return result;
  }

  private List<InternalPosition> generateAllPossiblePositionsAfter(InternalMove move) {
    List<InternalPosition> positions = new ArrayList<InternalPosition>();
    if (move.inTheSameBoundary()) {
      List<InternalPosition> positionsAfterMove = getPositionsAfterMove(move);
      positions.addAll(positionsAfterMove);
      // System.out.println("Move: " + move);
      // System.out.println("Position: " + positionsAfterMove);
    } else {
      positions.add(getPositionAfterMove(move));
    }
    return positions;
  }

  /**
   * A move in the same boundary.
   */
  private List<InternalPosition> getPositionsAfterMove(InternalMove move) {
    List<InternalPosition> positions = new ArrayList<InternalPosition>();
    InternalPosition partialPosition = position.clone();
    InternalRegion moveRegion = partialPosition.get(move.getRegionIndex());
    InternalBoundary moveBoundary = moveRegion.get(move.getFrom().getBoundaryIndex());
    // Rename vertices:
    if (move.getFrom().getVertexIndex() == move.getTo().getVertexIndex()) {
      // Same vertex:
      moveBoundary.get(move.getFrom().getVertexIndex()).setC(
          renameVertexTwice(moveBoundary.get(move.getFrom().getVertexIndex()).getC()));
    } else {
      moveBoundary.get(move.getFrom().getVertexIndex()).setC(
          renameVertex(moveBoundary.get(move.getFrom().getVertexIndex()).getC(),
              InternalConstants.TEMP_1, moveBoundary.size()));
      moveBoundary.get(move.getTo().getVertexIndex()).setC(
          renameVertex(moveBoundary.get(move.getTo().getVertexIndex()).getC(),
              InternalConstants.TEMP_2, moveBoundary.size()));
    }
    // Remove current region/boundary
    moveRegion.remove(move.getFrom().getBoundaryIndex());
    partialPosition.remove(move.getRegionIndex());
    // TODO: detect equivalent boundaries and reduce the number of splits?
    Set<List<InternalBoundary>> splits = allPossibleSplits(moveRegion);
    // System.out.println("Number of splits: " + splits.size());
    // System.out.println("Splits: " + splits);
    for (List<InternalBoundary> split : splits) {
      InternalRegion firstRegion = new InternalRegion(split);
      InternalRegion secondRegion = new InternalRegion();
      secondRegion.addAll(moveRegion);
      for (InternalBoundary boundary : firstRegion) {
        secondRegion.remove(boundary);
      }

      int fromId = move.getFrom().getVertexIndex();
      int toId = move.getTo().getVertexIndex();

      InternalBoundary firstBoundary = new InternalBoundary();
      firstBoundary.addAll(moveBoundary.subList(0, fromId + 1));
      firstBoundary.add(new Vertex(InternalConstants.TEMP_NEW, firstBoundary));
      if (moveBoundary.size() > 1) {
        firstBoundary.addAll(moveBoundary.subList(toId, moveBoundary.size()));
      }

      InternalBoundary secondBoundary = new InternalBoundary();
      secondBoundary.addAll(moveBoundary.subList(fromId, toId + 1));
      secondBoundary.add(new Vertex(InternalConstants.TEMP_NEW, firstBoundary));

      // System.out.println("First boundary: " + firstBoundary);
      // System.out.println("Second boundary: " + secondBoundary);
      firstRegion.add(firstBoundary);
      secondRegion.add(secondBoundary);

      InternalPosition newPosition = new InternalPosition(partialPosition);
      newPosition.add(firstRegion);
      newPosition.add(secondRegion);
      positions.add(newPosition.recreate());
      // positions.add(newPosition);
    }
    return positions;
  }

  /**
   * A move in 2 different boundaries.
   */
  private InternalPosition getPositionAfterMove(InternalMove move) {
    InternalPosition newPosition = position.clone();
    InternalRegion region = newPosition.get(move.getRegionIndex());
    InternalBoundary fromBoundary = region.get(move.getFrom().getBoundaryIndex());
    InternalBoundary toBoundary = region.get(move.getTo().getBoundaryIndex());
    // Rename vertices:
    fromBoundary.get(move.getFrom().getVertexIndex()).setC(
        renameVertex(move.getFrom().getVertex().getC(), InternalConstants.TEMP_1,
            fromBoundary.size()));
    toBoundary.get(move.getTo().getVertexIndex()).setC(
        renameVertex(move.getTo().getVertex().getC(), InternalConstants.TEMP_2, toBoundary.size()));

    InternalBoundary newBoundary =
        InternalBoundary.joinTwoBoundaries(fromBoundary, toBoundary, move);
    region.remove(fromBoundary);
    region.remove(toBoundary);
    region.add(newBoundary);
    // if (position.toString().equals("0.A.}0.A.}!")) {
    // return newPosition;
    // }
    return newPosition.recreate();
  }

  private char renameVertex(char vertex, char temp, int size) {
    switch (vertex) {
      case InternalConstants.CHAR_0:
        return InternalConstants.CHAR_1;
      case InternalConstants.CHAR_1:
        if (size > 1) return temp;
        return InternalConstants.CHAR_2;
      case InternalConstants.CHAR_2:
        return InternalConstants.CHAR_3;
      default:
        return vertex;
    }
  }

  private char renameVertexTwice(char vertex) {
    switch (vertex) {
      case InternalConstants.CHAR_0:
        return InternalConstants.TEMP_1;
      case InternalConstants.CHAR_1:
        return InternalConstants.CHAR_3;
      default:
        throw new RuntimeException("Not enough lives");
    }
  }

  // TODO: Create an abstract one to reuse in Boundary
  private static Set<List<InternalBoundary>> allPossibleSplits(List<InternalBoundary> boundaries) {
    Set<List<InternalBoundary>> possibleSplits = new HashSet<List<InternalBoundary>>();
    if (boundaries.isEmpty()) {
      possibleSplits.add(new ArrayList<InternalBoundary>());
      return possibleSplits;
    }
    List<InternalBoundary> list = new ArrayList<InternalBoundary>(boundaries);
    InternalBoundary head = list.get(0);
    List<InternalBoundary> rest = new ArrayList<InternalBoundary>(list.subList(1, list.size()));
    for (List<InternalBoundary> set : allPossibleSplits(rest)) {
      List<InternalBoundary> newSet = new ArrayList<InternalBoundary>();
      newSet.add(head);
      newSet.addAll(set);
      possibleSplits.add(newSet);
      possibleSplits.add(set);
    }
    return possibleSplits;
  }
}
