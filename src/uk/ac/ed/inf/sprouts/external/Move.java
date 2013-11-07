package uk.ac.ed.inf.sprouts.external;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.ed.inf.sprouts.internal.VertexHelper;
import uk.ac.ed.inf.sprouts.runners.Sprouts;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class Move implements Comparable<Move>, Serializable {

  private static final long serialVersionUID = -5014845674738051746L;

  private final Integer from;
  private final Boolean invertedFrom;
  private final Integer to;
  private final Boolean invertedTo;
  private final Integer createdVertex;
  private final Integer regionVertex;
  private final List<Integer> boundariesVertices;
  private Boolean invertedBoundaries;
  private Boolean containsSelf;

  public Move(Integer from, Boolean invertedFrom, Integer to, Boolean invertedTo,
      Integer createdVertex, Integer regionVertex, List<Integer> boundariesVertices,
      Boolean invertedBoundaries) {
    this.from = from;
    this.invertedFrom = invertedFrom;
    this.to = to;
    this.invertedTo = invertedTo;
    this.createdVertex = createdVertex;
    this.regionVertex = regionVertex;
    this.boundariesVertices = boundariesVertices;
    this.invertedBoundaries = invertedBoundaries;
    this.containsSelf = false;
    if (regionVertex != null && regionVertex.equals(from)) {
      System.err.println("WTF is happening? " + regionVertex + " " + from + " " + to);
    }
  }

  public static Move fromString(String moveString) {
    String regularExpression =
        Sprouts.LETTERS_MODE
            ? "(\\w+)(!)?\\((\\w+)(@(\\w+))?\\)(!)?(\\w+)(!)?(\\[(.*)\\])?"
            : "(\\d+)(!)?\\((\\d+)(@(\\d+))?\\)(!)?(\\d+)(!)?(\\[(.*)\\])?";
    Pattern pattern = Pattern.compile(regularExpression);
    Matcher matcher = pattern.matcher(moveString);

    if (matcher.matches()) {
      // System.out.println("Matches: ");
      // for (int i = 1; i <= matcher.groupCount(); i++)
      // System.out.println(i + ": " + matcher.group(i));

      Integer from = parseVertex(matcher.group(1));
      Boolean invertedFrom = matcher.group(2) != null;
      Integer to = parseVertex(matcher.group(7));
      Boolean invertedTo = matcher.group(6) != null;
      Integer createdVertex = parseVertex(matcher.group(3));
      Integer regionVertex = matcher.group(4) != null ? parseVertex(matcher.group(5)) : null;

      List<Integer> boundariesVertices = parseBoundariesVertices(matcher.group(10));
      Boolean invertedBoundaries = matcher.group(8) != null;

      return new Move(from, invertedFrom, to, invertedTo, createdVertex, regionVertex,
          boundariesVertices, invertedBoundaries);
    } else {
      System.out.println("Fail");
      return null;
    }
  }

  public void invertBoundaries() {
    invertedBoundaries = !invertedBoundaries;
  }

  public Integer getFrom() {
    return from;
  }

  public Boolean getInvertedFrom() {
    return invertedFrom;
  }

  public Integer getTo() {
    return to;
  }

  public Boolean getInvertedTo() {
    return invertedTo;
  }

  public Integer getCreatedVertex() {
    return createdVertex;
  }

  public Integer getRegionVertex() {
    return regionVertex;
  }

  public List<Integer> getBoundariesVertices() {
    return boundariesVertices;
  }

  public Boolean getInvertedBoundaries() {
    return invertedBoundaries;
  }

  public Boolean getContainsSelf() {
    return containsSelf;
  }

  @Override
  public String toString() {
    return toNotation();
//    return "Move [from=" + from + ", invertedFrom=" + invertedFrom + ", to=" + to + ", invertedTo="
//        + invertedTo + ", createdVertex=" + createdVertex + ", regionVertex=" + regionVertex
//        + ", boundariesVertices=" + boundariesVertices + ", invertedBoundaries="
//        + invertedBoundaries + "]";
  }

  public String toNotation() {
    Function<Integer, String> toNotation = new Function<Integer, String>() {
      @Override
      public String apply(Integer vertex) {
        if (!Sprouts.LETTERS_MODE) {
          return String.valueOf(vertex);
        }
        return String.valueOf(VertexHelper.getSimpleLetter(vertex));
      }
    };
    String notation =
        "" + toNotation.apply(from) + (invertedFrom ? "!" : "") + "("
            + toNotation.apply(createdVertex);
    if (regionVertex != null) {
      notation += "@" + toNotation.apply(regionVertex);
    }
    notation +=
        ")" + (invertedTo ? "!" : "") + toNotation.apply(to) + (invertedBoundaries ? "!" : "");
    if (boundariesVertices.size() > 0) {
      notation += "[" + Joiner.on(",").join(Lists.transform(boundariesVertices, toNotation)) + "]";
    }
    return notation;
  }

  private static List<Integer> parseBoundariesVertices(String verticesString) {
    List<Integer> vertices = new ArrayList<Integer>();
    if (verticesString != null) {
      String regularExpression =
          Sprouts.LETTERS_MODE
              ? "(((\\w+)|((\\w+)-(\\w+)))(,|$))"
              : "(((\\d+)|((\\d+)-(\\d+)))(,|$))";
      Pattern pattern = Pattern.compile(regularExpression);
      Matcher matcher = pattern.matcher(verticesString);
      while (matcher.find()) {
        if (matcher.group(3) != null) {
          vertices.add(parseVertex(matcher.group(3)));
        } else {
          int from = parseVertex(matcher.group(5));
          int to = parseVertex(matcher.group(6));
          for (int i = from; i <= to; i++) {
            vertices.add(i);
          }
        }

        // System.out.println("Matches: ");
        // for (int i = 1; i <= matcher.groupCount(); i++)
        // System.out.println(i + ": " + matcher.group(i));
      }
    };
    return vertices;
  }

  private static Integer parseVertex(String string) {
    if (Sprouts.LETTERS_MODE) {
      return (int) string.charAt(0) - (int) 'a' + 1;
    }
    return Integer.parseInt(string);
  }

  public void containsSelf() {
    containsSelf = true;
  }

  @Override
  public boolean equals(Object obj) {
    return toNotation().equals(((Move) obj).toNotation());
  }

  @Override
  public int hashCode() {
    return toNotation().hashCode();
  }

  @Override
  public int compareTo(Move o) {
    return toNotation().compareTo(o.toNotation());
  }
}
