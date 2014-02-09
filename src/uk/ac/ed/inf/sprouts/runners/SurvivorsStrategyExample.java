package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.strategy.SurvivorsStrategy;
import uk.ac.ed.inf.sprouts.utils.Output;

public class SurvivorsStrategyExample {

  public static void main(String[] args) {
    Game game = Game.fromString("5+");

    while (!game.isOver()) {
      SurvivorsStrategy strategy = new SurvivorsStrategy(game);
      strategy.compute();

      Move move;
      if (strategy.hasOptimalMove()) {
        move = strategy.getOptimalMove();
        Output.debug("Optimal move: " + move.toNotation());
      } else {
        move = strategy.getRandomMove();
        Output.debug("Doesn't have optimal move. Random move: " + move.toNotation());
      }
      game.makeMove(move);

      Output.debug();
    }
    Output.debug("Game over");
  }
}
