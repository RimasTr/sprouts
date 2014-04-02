package uk.ac.ed.inf.sprouts.internal;

import java.util.Comparator;

/**
 * Compares two boundaries in either normal or abstract way.
 * 
 * @author Rimas
 */
public class InternalBoundaryComparator implements Comparator<InternalBoundary> {

  private final boolean compareAsAbstract;

  public InternalBoundaryComparator(boolean compareAsAbstract) {
    this.compareAsAbstract = compareAsAbstract;
  }

  @Override
  public int compare(InternalBoundary b1, InternalBoundary b2) {
    return b1.toString(compareAsAbstract).compareTo(b2.toString(compareAsAbstract));
  }
}
