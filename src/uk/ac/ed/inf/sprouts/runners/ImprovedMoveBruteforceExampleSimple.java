package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.ImprovedMoveBruteforcer;
import uk.ac.ed.inf.sprouts.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;

public class ImprovedMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("4+");
    //game.makeMove("a(d)c");

    MoveBruteforcer moveBruteforcer = new ImprovedMoveBruteforcer(game);
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      System.out.println("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else {
      System.out.println("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
  }
}
