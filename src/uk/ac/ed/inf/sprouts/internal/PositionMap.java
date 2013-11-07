package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionMap extends HashMap<Vertex, List<Vertex>> {

  private static final long serialVersionUID = 4994616576185757612L;

  private char nextLowercase = InternalConstants.FIRST_LOWERCASE_LETTER;
  private char nextUppercase = InternalConstants.FIRST_UPPERCASE_LETTER;

  public PositionMap(List<Vertex> vertices) {
    for (Vertex vertex : vertices) {
      if (!vertex.isAbstract()) {
        get(vertex).add(vertex);
        if (Character.isLowerCase(vertex.getC())
            && !InternalConstants.TEMP_LETTERS.contains(vertex.getC())
            && vertex.getC() >= nextLowercase) {
          nextLowercase = (char) (vertex.getC() + 1);
        } else if (Character.isUpperCase(vertex.getC()) && vertex.getC() >= nextUppercase) {
          nextUppercase = (char) (vertex.getC() + 1);
        }
      }
    }
  }

  public char getNextLowercase() {
    return nextLowercase++;
  }

  public char getNextUppercase() {
    return nextUppercase++;
  }

  @Override
  public List<Vertex> get(Object key) {
    if (!containsKey(key)) {
      put((Vertex) key, new ArrayList<Vertex>());
    }
    return super.get(key);
  }
}
