package uk.ac.ed.inf.sprouts.players;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.Output;

public class SmartPlayer implements Player {

  private static final int NUMBER_OF_LIVES_TO_BRUTEFORCE = 5 * 3;

  @Override
  public Move getMove(Game game) {
    Player player;
    if (7.0 / 3.0 * game.getInitialSprouts() - game.getNumberOfMoves() < NUMBER_OF_LIVES_TO_BRUTEFORCE) {
      player = new BruteforcePlayer(true);
    } else {
      player = new StrategyPlayer();
    }
    Output.debug("Using " + player.getClass().getSimpleName());
    return player.getMove(game);
  }
}
