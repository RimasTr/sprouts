package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.strategy.SurvivorsStrategy;

public class SurvivorsStrategyCounter {

  public static void main(String[] args) {
    int strategyWon = 0;
    int randomWon = 0;

    for (int i = 0; i < 20; i++) {
      Game game = Game.fromString("7+");

      while (!game.isOver()) {
        SurvivorsStrategy strategy = new SurvivorsStrategy(game);
        strategy.compute();

        Move move;
        if (strategy.hasOptimalMove()) {
          move = strategy.getOptimalMove();
          System.out.println("Optimal move: " + move.toNotation());
        } else {
          move = strategy.getRandomMove();
          System.out.println("Doesn't have optimal move. Random move: " + move.toNotation());
        }
        game.makeMove(move);

        System.out.println();

        if (game.isOver()) {
          strategyWon += 1;
          System.out.println("Strategy won");
          break;
        }

        SurvivorsStrategy strategy2 = new SurvivorsStrategy(game);
        strategy2.compute();
        move = strategy2.getRandomMove();
        game.makeMove(move);

        if (game.isOver()) {
          randomWon += 1;
          System.out.println("Random won");
        }
      }
    }
    System.out.println("Strategy: " + strategyWon);
    System.out.println("Random: " + randomWon);
  }
}
