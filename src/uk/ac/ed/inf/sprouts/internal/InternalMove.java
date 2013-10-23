package uk.ac.ed.inf.sprouts.internal;

public class InternalMove {

  private final VertexInfo from;
  private final VertexInfo to;

  public InternalMove(VertexInfo from, VertexInfo to) {
    this.from = from;
    this.to = to;
  }

  public VertexInfo getFrom() {
    return from;
  }

  public VertexInfo getTo() {
    return to;
  }

  public boolean inTheSameBoundary() {
    return from.getBoundaryIndex() == to.getBoundaryIndex();
  }
}
