package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionMap extends HashMap<Vertex, List<Vertex>> {

  private static final long serialVersionUID = 4994616576185757612L;

  public PositionMap(List<Vertex> vertices) {
    for (Vertex vertex : vertices) {
      if (!vertex.isAbstract()) {
        get(vertex).add(vertex);
      }
    }
  }

  @Override
  public List<Vertex> get(Object key) {
    if (!containsKey(key)) {
      put((Vertex) key, new ArrayList<Vertex>());
    }
    return super.get(key);
  }
}
