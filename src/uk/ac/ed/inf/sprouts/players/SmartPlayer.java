package uk.ac.ed.inf.sprouts.players;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.Output;

public class SmartPlayer implements Player {

  private static final int NUMBER_OF_LIVES_TO_TRY_BRUTEFORCE = 8 * 3;
  private static final int NUMBER_OF_LIVES_TO_BRUTEFORCE = 4 * 3;
  private static final double LIVES_CONSTANT = 7.0 / 3.0;

  @Override
  public Move getMove(Game game) {
    Player player;
    if (estimateLives(game) < NUMBER_OF_LIVES_TO_BRUTEFORCE) {
      player = new BruteforcePlayer(true, true);
    } else if (estimateLives(game) < NUMBER_OF_LIVES_TO_TRY_BRUTEFORCE) {
      player = new QuickBruteforcePlayer();
    } else {
      player = new StrategyPlayer();
    }
    Output.debug("Using " + player.getClass().getSimpleName());
    Move move = player.getMove(game);
    if (move == null) {
      // QuickBruterforce didn't work
      Output.debug("Backing off to strategy");
      move = new StrategyPlayer().getMove(game);
    }
    return move;
  }

  private double estimateLives(Game game) {
    return LIVES_CONSTANT * game.getInitialSprouts() - game.getNumberOfMoves();
  }
}
