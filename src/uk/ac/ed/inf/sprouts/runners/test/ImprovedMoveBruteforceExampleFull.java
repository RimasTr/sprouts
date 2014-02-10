package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.Output;

public class ImprovedMoveBruteforceExampleFull {

  public static void main(String[] args) {
    Game game = Game.fromString("1+");

    while (!game.isOver()) {
      MoveBruteforcer moveBruteforcer = new ImprovedMoveBruteforcer(game);
      moveBruteforcer.compute();

      Move move;
      if (moveBruteforcer.hasWinningMove()) {
        move = moveBruteforcer.getWinningMove();
        Output.debug("Winning move: " + move.toNotation());
      } else {
        move = moveBruteforcer.getRandomMove();
        Output.debug("Doesn't have winning move. Random move: " + move.toNotation());
      }
      game.makeMove(move);

      Output.debug();
    }
    Output.debug("Game over");
  }
}
