package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChildrenGenerator {

  private final InternalPosition position;

  public ChildrenGenerator(InternalPosition position) {
    this.position = position;
  }

  public Set<InternalPosition> generateAllChildren() {
    HashSet<InternalPosition> children = new HashSet<InternalPosition>();
    List<InternalMove> moves = generateInternalMoves();
    for (InternalMove move : moves) {
      children.addAll(generateAllPossiblePositionsAfter(move));
    }
    return children;
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
          regionResult.add(new VertexInfo(vertex, regionIndex, boundaryIndex,
              vertexIndex));
        }
      }
      result.add(regionResult);
    }
    return result;
  }

  private List<InternalPosition> generateAllPossiblePositionsAfter(InternalMove move) {
    List<InternalPosition> positions = new ArrayList<InternalPosition>();
    if (move.inTheSameBoundary()) {
      positions.addAll(getPositionsAfterMove(move));
    } else {
      positions.add(getPositionAfterMove(move));
//      System.out.println("Move: " + move);
//      System.out.println("Position: " + getPositionAfterMove(move));
    }
    return positions;
  }

  /**
   * A move in the same boundary.
   */
  private List<InternalPosition> getPositionsAfterMove(InternalMove move) {
    List<InternalPosition> positions = new ArrayList<InternalPosition>();
    // TODO: implement
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
        renameVertex(move.getFrom().getVertex().getC(), InternalConstants.TEMP_1));
    toBoundary.get(move.getTo().getVertexIndex()).setC(
        renameVertex(move.getTo().getVertex().getC(), InternalConstants.TEMP_2));

    InternalBoundary newBoundary =
        InternalBoundary.joinTwoBoundaries(fromBoundary, toBoundary, move);
    region.remove(fromBoundary);
    region.remove(toBoundary);
    region.add(newBoundary);
    return newPosition.recreate();
  }

  private char renameVertex(char vertex, char temp) {
    switch (vertex) {
      case InternalConstants.CHAR_0:
        return InternalConstants.CHAR_1;
      case InternalConstants.CHAR_1:
        return temp;
      case InternalConstants.CHAR_2:
        return InternalConstants.CHAR_3;
      default:
        return vertex;
    }
  }
}
