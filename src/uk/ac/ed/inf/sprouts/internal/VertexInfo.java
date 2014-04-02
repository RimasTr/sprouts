package uk.ac.ed.inf.sprouts.internal;

/**
 * A vertex with its location (as a region, boundary, and position in the boundary).
 *
 * @author Rimas
 */
public class VertexInfo {

  private final Vertex vertex;
  private final int vertexIndex;
  private final int boundaryIndex;
  private final int regionIndex;

  public VertexInfo(Vertex vertex, int regionIndex, int boundaryIndex, int vertexIndex) {
    this.vertex = vertex;
    this.regionIndex = regionIndex;
    this.boundaryIndex = boundaryIndex;
    this.vertexIndex = vertexIndex;
  }

  public Vertex getVertex() {
    return vertex;
  }

  public int getVertexIndex() {
    return vertexIndex;
  }

  public int getBoundaryIndex() {
    return boundaryIndex;
  }

  public int getRegionIndex() {
    return regionIndex;
  }

  @Override
  public String toString() {
    return vertex.toString() + "(" + regionIndex + "," + boundaryIndex + "," + vertexIndex + ")";
  }
}
