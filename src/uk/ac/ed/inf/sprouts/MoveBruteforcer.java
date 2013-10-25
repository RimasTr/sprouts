package uk.ac.ed.inf.sprouts;

import java.util.HashMap;

import uk.ac.ed.inf.sprouts.external.Move;

public interface MoveBruteforcer {

  public void compute();

  public boolean hasWinningMove();

  public Move getWinningMove();

  public Move getRandomMove();

  public HashMap<String, Boolean> getComputedPositions();
}
