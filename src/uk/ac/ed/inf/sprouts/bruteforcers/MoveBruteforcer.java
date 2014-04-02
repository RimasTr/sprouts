package uk.ac.ed.inf.sprouts.bruteforcers;

import java.util.HashMap;

import uk.ac.ed.inf.sprouts.external.Move;

/**
 * Common interface for all search-based strategies.
 * 
 * @author Rimas
 */
public interface MoveBruteforcer {

  public void compute();

  public boolean hasWinningMove();

  public Move getWinningMove();

  public Move getRandomMove();

  public HashMap<String, Boolean> getComputedPositions();
}
