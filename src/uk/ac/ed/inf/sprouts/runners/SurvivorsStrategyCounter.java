package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.strategy.SurvivorsStrategy;
import uk.ac.ed.inf.sprouts.utils.Output;

public class SurvivorsStrategyCounter {

  public static void main(String[] args) {
    int strategyWon = 0;
    int randomWon = 0;

    for (int i = 0; i < 20; i++) {
      Game game = Game.fromString("4+");

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

        if (game.isOver()) {
          strategyWon += 1;
          Output.debug("Strategy won");
          break;
        }

        SurvivorsStrategy strategy2 = new SurvivorsStrategy(game);
        strategy2.compute();
        move = strategy2.getRandomMove();
        game.makeMove(move);

        if (game.isOver()) {
          randomWon += 1;
          Output.debug("Random won");
        }
      }
    }
    Output.debug("Strategy: " + strategyWon);
    Output.debug("Random: " + randomWon);
  }
}
