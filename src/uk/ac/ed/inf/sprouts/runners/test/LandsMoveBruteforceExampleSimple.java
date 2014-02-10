package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.bruteforcers.LandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.utils.Output;

public class LandsMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("5+");
    game.makeMove("1(6)1");
    game.makeMove("1(7@2)6[5,4]");
    game.makeMove("7(8)4");
    game.makeMove("2(9)2");

    long time = System.currentTimeMillis();
    MoveBruteforcer moveBruteforcer = new LandsMoveBruteforcer(game);
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
