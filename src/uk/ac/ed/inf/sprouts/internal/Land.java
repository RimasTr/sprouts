package uk.ac.ed.inf.sprouts.internal;

public class Land extends InternalPosition {

  private static final long serialVersionUID = 5728940436673623606L;

  @Override
  protected char getEndChar() {
    return InternalConstants.END_OF_LAND_CHAR;
  }
}
