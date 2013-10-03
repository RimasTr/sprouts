package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;

public class MoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("3+");
    //game.makeMove("a(d)c");

    MoveBruteforcer moveBruteforcer = new MoveBruteforcer(game);
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      System.out.println("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else {
      System.out.println("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
  }
}
