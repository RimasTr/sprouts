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
      for (int boundaryIndex = 0; boundaryIndex < region.size(); boundaryIndex++) {
        InternalBoundary boundary = region.get(boundaryIndex);
        for (int vertexIndex = 0; vertexIndex < boundary.size(); vertexIndex++) {
          regionResult.add(new VertexInfo(boundary.get(vertexIndex), regionIndex, boundaryIndex,
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
      positions.add(getPositionAfterMove(move));
    } else {
      positions.addAll(getPositionsAfterMove(move));
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
    // TODO: implement
    return position;
  }
}
