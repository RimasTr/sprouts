package uk.ac.ed.inf.sprouts.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import uk.ac.ed.inf.sprouts.external.Position;
import uk.ac.ed.inf.sprouts.external.Region;

import com.google.common.base.Function;

public class InternalPosition extends ArrayList<InternalRegion> {

  private static final long serialVersionUID = 2586045556331715946L;

  public static Function<InternalPosition, String> toString =
      new Function<InternalPosition, String>() {
        public String apply(InternalPosition position) {
          return position.toString();
        }
      };

  public InternalPosition() {
    super();
  }

  public InternalPosition(InternalPosition position) {
    super(position);
  }

  public static InternalPosition fromExternal(Position position) {
    InternalPosition internalPosition = new InternalPosition();
    List<Region> regions = position.getRegions();
    for (Region region : regions) {
      InternalRegion internalRegion = InternalRegion.fromExternal(position, region);
      internalPosition.add(internalRegion);
    }
    // Output.debug("Original position:\n" + internalPosition);
    internalPosition.optimize();
    return internalPosition;
  }

  public static InternalPosition fromStringUnoptimized(String string) {
    InternalPosition internalPosition = new InternalPosition();
    StringTokenizer regions =
        new StringTokenizer(string, String.valueOf(InternalConstants.END_OF_REGION_CHAR));
    int total = regions.countTokens();
    while (total > 1) {
      internalPosition.add(InternalRegion.fromString(regions.nextToken()));
      total--;
    }
    return internalPosition;
  }

  public static InternalPosition fromString(String string) {
    InternalPosition internalPosition = InternalPosition.fromStringUnoptimized(string);
    internalPosition.optimize();
    return internalPosition;
  }

  private List<Vertex> getVertices() {
    ArrayList<Vertex> result = new ArrayList<Vertex>();
    for (InternalRegion region : this) {
      result.addAll(region.getVertices());
    }
    return result;
  }

  // TODO: private
  public void optimize() {
    // Output.debug("Before:    " + this);
    PositionMap map = getMap();
    detectAbstractVertices(map);
    // Output.debug("After1:    " + this);
    deleteEmptyBoundariesAndRegions();
    // Output.debug("After2:    " + this);
    // recompute map
    map = new PositionMap(getVertices());
    detectAbstractVertices(map);
    // Output.debug("After3:    " + this);
    canonize();
    // Output.debug("After4:    " + this);
  }

  private void detectAbstractVertices(PositionMap map) {
    List<Vertex> removedVertices = new ArrayList<Vertex>();
    for (Vertex vertex : map.keySet()) {
      List<Vertex> occurrences = map.get(vertex);
      // if (vertex.getC() == 'a') {
      // Output.debug("Vertex: " + vertex.getC());
      // Output.debug("Occurences: " + occurrences);
      // }
      if (vertex.getC() == InternalConstants.CHAR_3) {
        for (Vertex v : occurrences) {
          removedVertices.add(v);
        }
        continue;
      }
      switch (occurrences.size()) {
        case 1:
          // only occurs once
          if (vertex.isUppercase()) {
            vertex.setC(InternalConstants.CHAR_2);
            break;
          } else {
            // lowercase
            InternalBoundary boundary = occurrences.get(0).getBoundary();
            switch (boundary.size()) {
              case 1:
                // in a boundary with a single vertex
                // replace by 0
                vertex.setC(InternalConstants.CHAR_0);
                break;
              default:
                // in a boundary with several vertices
                // replace by 1
                vertex.setC(InternalConstants.CHAR_1);
                break;
            }
          }
          break;
        case 2:
          // occurs twice
          Vertex firstVertex = occurrences.get(0);
          Vertex secondVertex = occurrences.get(1);
          InternalBoundary firstBoundary = firstVertex.getBoundary();
          InternalBoundary secondBoundary = secondVertex.getBoundary();
          if (firstBoundary == secondBoundary) {
            // same boundary
            if (firstBoundary.oneAfterTheOther(firstVertex, secondVertex)) {
              // one after the other
              // replace by 2
              firstBoundary.remove(firstVertex);
              secondVertex.setC(InternalConstants.CHAR_2);
            } else {
              // lowercase
              if (Character.isUpperCase(firstVertex.getC())) {
                char letter = map.getNextLowercase();
                firstVertex.setC(letter);
                secondVertex.setC(letter);
              }
            }
          } else {
            // different boundaries (and regions)
            // uppercase
            if (Character.isLowerCase(firstVertex.getC())) {
              char letter = map.getNextUppercase();
              firstVertex.setC(letter);
              secondVertex.setC(letter);
            }
          }
          break;
        default:
          // occurs 3 times
          // remove
          // for (Vertex v : occurrences) {
          // v.getBoundary().remove(v);
          // }
          for (Vertex v : occurrences) {
            removedVertices.add(v);
          }
          break;
      }
    }

    // Remove vertices
    for (Vertex v : removedVertices) {
      v.getBoundary().remove(v);
    }
  }

  private void deleteEmptyBoundariesAndRegions() {
    for (Iterator<InternalRegion> it = this.iterator(); it.hasNext();) {
      InternalRegion region = it.next();
      region.deleteEmptyBoundaries();
      if (region.getLives() < 2) {
        it.remove();
      } else if (region.getLives() < 4) {
        region.mergeBoundaries();
      }
    }
  }

  private void canonize() {
    sort();
    renameLowercase();
    renameUppercase();
    sort();
  }

  private void renameLowercase() {
    renameLetters(false);
  }

  private void renameUppercase() {
    renameLetters(true);
  }

  private void renameLetters(boolean isLowerCase) {
    HashMap<Character, Character> renamingMap = new HashMap<Character, Character>();
    String string = toString();
    char lastUsed =
        isLowerCase
            ? InternalConstants.FIRST_LOWERCASE_LETTER
            : InternalConstants.FIRST_UPPERCASE_LETTER;
    for (char c : string.toCharArray()) {
      if ((isLowerCase && Character.isLowerCase(c)) || (!isLowerCase && Character.isUpperCase(c))) {
        if (!renamingMap.containsKey(c)) {
          renamingMap.put(c, lastUsed++);
        }
      }
    }
    rename(renamingMap);
  }

  private void rename(HashMap<Character, Character> renamingMap) {
    PositionMap positionMap = getMap();
    for (char fromChar : renamingMap.keySet()) {
      char toChar = renamingMap.get(fromChar);
      for (Vertex vertex : positionMap.get(new Vertex(fromChar, null))) {
        vertex.setC(toChar);
      }
    }
  }

  private void sort() {
    for (InternalRegion region : this) {
      region.sort();
      String normalRepresentation = region.toString();
      region.inverseOrientation();
      region.sort();
      String reverseRepresentation = region.toString();
      if (normalRepresentation.compareTo(reverseRepresentation) < 0) {
        region.inverseOrientation();
        region.sort();
      }
    }
    Collections.sort(this);
  }

  public PositionMap getMap() {
    return new PositionMap(getVertices());
  }

  protected char getEndChar() {
    return InternalConstants.END_OF_POSITION_CHAR;
  }

  @Override
  public String toString() {
    String result = "";
    for (InternalRegion region : this) {
      result += region.toString();
    }
    result += getEndChar();
    return result;
  }

  public boolean isLost() {
    return this.isEmpty();
  }

  @Override
  public InternalPosition clone() {
    // TODO: more efficient?
    return InternalPosition.fromStringUnoptimized(this.toString());
  }

  public InternalPosition recreate() {
    InternalPosition position = this.clone();
    position.optimize();
    return position;
  }
}
