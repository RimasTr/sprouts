package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.SimpleMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;

public class MoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("5+");
    game.makeMove("3(6)4");
    game.makeMove("4(7)!6![5]");
    game.makeMove("1(8)1");
    game.makeMove("2(9)2[1,4,6,7,8]");
    game.makeMove("5(10)5[3,4,6,7]");
    game.makeMove("3(11)7[4,6]");
    game.makeMove("2(12)9");

    MoveBruteforcer moveBruteforcer = new SimpleMoveBruteforcer(game);
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      System.out.println("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else {
      System.out.println("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
  }
}
