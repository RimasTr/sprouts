package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedLandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.utils.Output;
import uk.ac.ed.inf.sprouts.utils.SavedPositionsHandler;

public class ImprovedLandsMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("7+");

    // game.makeMove("...");

    long time = System.currentTimeMillis();
    // HashMap<String, Boolean> savedPositions = SavedPositionsHandler.getSavedPositions();
    // ImprovedLandsMoveBruteforcer moveBruteforcer =
    // new ImprovedLandsMoveBruteforcer(game, 6);
    // MoveBruteforcer moveBruteforcer = new ImprovedLandsMoveBruteforcer(game, 6);
    ImprovedLandsMoveBruteforcer moveBruteforcer =
        new ImprovedLandsMoveBruteforcer(game, SavedPositionsHandler.getSavedPositions());
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      Output.debug("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else if (moveBruteforcer.hasOptimalMove()) {
      Output.debug("Doesn't have winning move. Optimal move: "
          + moveBruteforcer.getOptimalMove().toNotation());
    } else {
      Output.debug("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
    Output.debug("Time: " + (System.currentTimeMillis() - time) / 1000.0);
  }
}
