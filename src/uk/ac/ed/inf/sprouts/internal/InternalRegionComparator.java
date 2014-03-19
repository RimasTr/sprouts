package uk.ac.ed.inf.sprouts.internal;

import java.util.Comparator;

public class InternalRegionComparator implements Comparator<InternalRegion> {

  private final boolean compareAsAbstract;

  public InternalRegionComparator(boolean compareAsAbstract) {
    this.compareAsAbstract = compareAsAbstract;
  }

  @Override
  public int compare(InternalRegion r1, InternalRegion r2) {
    return r1.toString(compareAsAbstract).compareTo(r2.toString(compareAsAbstract));
  }
}
