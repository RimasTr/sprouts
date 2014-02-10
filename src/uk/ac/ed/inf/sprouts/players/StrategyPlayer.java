package uk.ac.ed.inf.sprouts.players;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.strategy.SurvivorsStrategy;

public class StrategyPlayer implements Player {

  @Override
  public Move getMove(Game game) {
    SurvivorsStrategy player = new SurvivorsStrategy(game);
    player.compute();
    if (player.hasOptimalMove()) {
      return player.getOptimalMove();
    } else {
      return player.getRandomMove();
    }
  }
}
