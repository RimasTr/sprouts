package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Move {

  private final Integer from;
  private final Boolean invertedFrom;
  private final Integer to;
  private final Boolean invertedTo;
  private final Integer createdVertex;
  private final Integer regionVertex;
  private final List<Integer> boundariesVertices;

  private final Boolean invertedBoundaries;

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
  }

  public static Move fromString(String moveString) {
    Pattern pattern =
        Pattern.compile("(\\d+)(!)?\\((\\d+)(@(\\d+))?\\)(!)?(\\d+)(!)?(\\[(.*)\\])?");
    Matcher matcher = pattern.matcher(moveString);

    if (matcher.matches()) {
//      System.out.println("Matches: ");
//      for (int i = 1; i <= matcher.groupCount(); i++)
//        System.out.println(i + ": " + matcher.group(i));

      Integer from = Integer.parseInt(matcher.group(1));
      Boolean invertedFrom = matcher.group(2) != null;
      Integer to = Integer.parseInt(matcher.group(7));
      Boolean invertedTo = matcher.group(6) != null;
      Integer createdVertex = Integer.parseInt(matcher.group(3));
      Integer regionVertex = matcher.group(4) != null ? Integer.parseInt(matcher.group(5)) : null;

      List<Integer> boundariesVertices = new ArrayList<Integer>();
      if (matcher.group(10) != null) {
        StringTokenizer tokenizer = new StringTokenizer(matcher.group(10), ",");
        while (tokenizer.hasMoreTokens()) {
          boundariesVertices.add(Integer.parseInt(tokenizer.nextToken()));
        }
      }
      Boolean invertedBoundaries = matcher.group(8) != null;

      return new Move(from, invertedFrom, to, invertedTo, createdVertex, regionVertex,
          boundariesVertices, invertedBoundaries);
    } else {
      System.out.println("Fail");
      return null;
    }
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

  @Override
  public String toString() {
    return "Move [from=" + from + ", invertedFrom=" + invertedFrom + ", to=" + to + ", invertedTo="
        + invertedTo + ", createdVertex=" + createdVertex + ", regionVertex=" + regionVertex
        + ", boundariesVertices=" + boundariesVertices + ", invertedBoundaries="
        + invertedBoundaries + "]";
  }
}
