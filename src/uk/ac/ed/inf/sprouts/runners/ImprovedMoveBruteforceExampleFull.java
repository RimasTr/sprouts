package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;

public class ImprovedMoveBruteforceExampleFull {

  public static void main(String[] args) {
    Game game = Game.fromString("1+");

    while (!game.isOver()) {
      MoveBruteforcer moveBruteforcer = new ImprovedMoveBruteforcer(game);
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
