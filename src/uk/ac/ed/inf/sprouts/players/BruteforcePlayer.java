package uk.ac.ed.inf.sprouts.players;

import java.util.HashMap;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedLandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.SavedPositionsHandler;

/**
 * The main brute-force search strategy player. Uses ImprovedLandsMoveBruteforcer.
 *
 * @author Rimas
 */
public class BruteforcePlayer implements Player {

  private boolean useSavedPositions;
  private boolean useOptimalMove;

  public BruteforcePlayer() {
    this(true, true);
  }

  public BruteforcePlayer(boolean useSavedPositions, boolean useOptimalMove) {
    this.useSavedPositions = useSavedPositions;
    this.useOptimalMove = useOptimalMove;
  }

  @Override
  public Move getMove(Game game) {
    ImprovedLandsMoveBruteforcer player;
    if (useSavedPositions) {
      HashMap<String, Boolean> savedPositions = SavedPositionsHandler.getSavedPositions();
      player = new ImprovedLandsMoveBruteforcer(game, savedPositions, useOptimalMove);
    } else {
      player = new ImprovedLandsMoveBruteforcer(game, useOptimalMove);
    }
    player.compute();
    if (player.hasWinningMove()) {
      return player.getWinningMove();
    } else {
      return player.getRandomMove();
    }
  }
}
