package uk.ac.ed.inf.sprouts.players;

import java.util.HashMap;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedLandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.SavedPositionsHandler;

/**
 * Check if we have already computed the winning move for this position.
 *
 * @author Rimas
 */
public class QuickBruteforcePlayer implements Player {

  @Override
  public Move getMove(Game game) {
    ImprovedLandsMoveBruteforcer player;
    HashMap<String, Boolean> savedPositions = SavedPositionsHandler.getSavedPositions();
    player = new ImprovedLandsMoveBruteforcer(game, savedPositions, false);
    player.quickCompute();
    if (player.hasWinningMove()) {
      return player.getWinningMove();
    } else {
      return null;
    }
  }
}
