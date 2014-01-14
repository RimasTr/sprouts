package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;

public class ImprovedMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("5+");
    //game.makeMove("a(d)c");

    long time = System.currentTimeMillis();
    MoveBruteforcer moveBruteforcer = new ImprovedMoveBruteforcer(game);
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
