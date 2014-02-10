package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.SimpleMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.utils.Output;

public class MoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("3+");
//    game.makeMove("3(6)4");
//    game.makeMove("4(7)!6![5]");
//    game.makeMove("1(8)1");
//    game.makeMove("2(9)2[1,4,6,7,8]");
//    game.makeMove("5(10)5[3,4,6,7]");
//    game.makeMove("3(11)7[4,6]");
//    game.makeMove("2(12)9");

    long time = System.currentTimeMillis();

    MoveBruteforcer moveBruteforcer = new SimpleMoveBruteforcer(game);
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
