package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.strategy.SurvivorsStrategy;

public class SurvivorsStrategyExample {

  public static void main(String[] args) {
    Game game = Game.fromString("5+");

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
    }
    System.out.println("Game over");
  }
}
