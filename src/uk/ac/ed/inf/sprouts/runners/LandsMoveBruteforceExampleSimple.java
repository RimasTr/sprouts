package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.bruteforcers.LandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;

public class LandsMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("4+");
    //game.makeMove("a(d)c");

    long time = System.currentTimeMillis();
    MoveBruteforcer moveBruteforcer = new LandsMoveBruteforcer(game);
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      System.out.println("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else {
      System.out.println("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
    System.out.println("Time: " + (System.currentTimeMillis() - time)/1000.0);
  }
}
