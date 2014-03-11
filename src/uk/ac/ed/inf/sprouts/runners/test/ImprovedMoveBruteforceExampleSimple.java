package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.utils.Output;

public class ImprovedMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("6+");
    //game.makeMove("a(d)c");

    long time = System.currentTimeMillis();
    MoveBruteforcer moveBruteforcer = new ImprovedMoveBruteforcer(game);
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      Output.debug("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else {
      Output.debug("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
    Output.debug("Time: " + (System.currentTimeMillis() - time)/1000.0);
  }
}
