package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class InternalPositionWithLands extends ArrayList<Land> {

  private static final long serialVersionUID = -7575398053641141623L;

  public InternalPositionWithLands(InternalPosition position) {
    this.addAll(splitIntoLands(position));
  }

  private List<Land> splitIntoLands(InternalPosition position) {
    List<Land> result = new ArrayList<Land>();
    if (position.isEmpty()) {
      return result;
    }
    Multimap<Integer, Integer> regionGraph = createGraph(position);
    // BFS
    Set<Integer> visited = new HashSet<Integer>();
    Queue<Integer> queue = new LinkedList<Integer>();
    for (int startingRegion = 0; startingRegion < position.size(); startingRegion++) {
      if (!visited.contains(startingRegion)) {
        Land currentLand = new Land();
        queue.add(startingRegion);
        visited.add(startingRegion);
        while (!queue.isEmpty()) {
          Integer region = queue.remove();
          currentLand.add(position.get(region));
          for (Integer child : regionGraph.get(region)) {
            if (!visited.contains(child)) {
              queue.add(child);
              visited.add(child);
            }
          }
        }
        result.add(currentLand);
      }
    }
    return result;
  }

  private Multimap<Integer, Integer> createGraph(InternalPosition position) {
    List<ArrayList<Vertex>> uppercaseVertices = getUppercaseVertices(position);
    Map<Vertex, Integer> vertexMap = new HashMap<Vertex, Integer>();
    Multimap<Integer, Integer> graph = HashMultimap.create();
    for (int regionId = 0; regionId < uppercaseVertices.size(); regionId++) {
      for (Vertex v : uppercaseVertices.get(regionId)) {
        if (!vertexMap.containsKey(v)) {
          vertexMap.put(v, regionId);
        } else {
          int previousRegion = vertexMap.get(v);
          graph.put(regionId, previousRegion);
          graph.put(previousRegion, regionId);
        }
      }
    }
    return graph;
  }

  private List<ArrayList<Vertex>> getUppercaseVertices(InternalPosition position) {
    ArrayList<ArrayList<Vertex>> result = new ArrayList<ArrayList<Vertex>>();
    for (int regionIndex = 0; regionIndex < position.size(); regionIndex++) {
      ArrayList<Vertex> regionResult = new ArrayList<Vertex>();
      InternalRegion region = position.get(regionIndex);
      for (int boundaryIndex = 0; boundaryIndex < region.size(); boundaryIndex++) {
        InternalBoundary boundary = region.get(boundaryIndex);
        for (int vertexIndex = 0; vertexIndex < boundary.size(); vertexIndex++) {
          Vertex vertex = boundary.get(vertexIndex);
          if (vertex.isUppercase()) {
            regionResult.add(vertex);
          }
        }
      }
      result.add(regionResult);
    }
    return result;
  }

  @Override
  public String toString() {
    String result = "";
    for (Land land : this) {
      result += land.toString();
    }
    result += InternalConstants.END_OF_POSITION_CHAR;
    return result;
  }
}
