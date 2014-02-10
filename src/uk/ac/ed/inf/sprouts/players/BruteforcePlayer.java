package uk.ac.ed.inf.sprouts.players;

import java.util.HashMap;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedLandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.SavedPositionsHandler;

public class BruteforcePlayer implements Player {

  private boolean useSavedPositions;

  public BruteforcePlayer() {
    this(false);
  }

  public BruteforcePlayer(boolean useSavedPositions) {
    this.useSavedPositions = useSavedPositions;
  }

  @Override
  public Move getMove(Game game) {
    ImprovedLandsMoveBruteforcer player;
    if (useSavedPositions) {
      HashMap<String, Boolean> savedPositions = SavedPositionsHandler.getSavedPositions();
      player = new ImprovedLandsMoveBruteforcer(game, savedPositions);
    } else {
      player = new ImprovedLandsMoveBruteforcer(game);
    }
    player.compute();
    if (player.hasWinningMove()) {
      return player.getWinningMove();
    } else {
      return player.getRandomMove();
    }
  }
}
