package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.SimpleMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;

public class MoveBruteforceExampleFull {

  public static void main(String[] args) {
    Game game = Game.fromString("4+");

    while (!game.isOver()) {
      MoveBruteforcer moveBruteforcer = new SimpleMoveBruteforcer(game);
      moveBruteforcer.compute();

      Move move;
      if (moveBruteforcer.hasWinningMove()) {
        move = moveBruteforcer.getWinningMove();
        System.out.println("Winning move: " + move.toNotation());
      } else {
        move = moveBruteforcer.getRandomMove();
        System.out.println("Doesn't have winning move. Random move: " + move.toNotation());
      }
      game.makeMove(move);

      System.out.println();
    }
    System.out.println("Game over");
  }
}
