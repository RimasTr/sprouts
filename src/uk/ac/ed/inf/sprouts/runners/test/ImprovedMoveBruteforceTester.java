package uk.ac.ed.inf.sprouts.runners.test;

import java.util.HashMap;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedMoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.MoveBruteforcer;
import uk.ac.ed.inf.sprouts.bruteforcers.SimpleMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.utils.Output;

public class ImprovedMoveBruteforceTester {

  public static void main(String[] args) {
    Game game = Game.fromString("4+");
//    game.makeMove("3(6)4");
//    game.makeMove("4(7)!6![5]");
//    game.makeMove("1(8)1");
//    game.makeMove("2(9)2[1,4,6,7,8]");
//    game.makeMove("5(10)5[3,4,6,7]");
//    game.makeMove("3(11)7[4,6]");
    //game.makeMove("2(12)9");

    MoveBruteforcer improvedMoveBruteforcer = new ImprovedMoveBruteforcer(game);
    improvedMoveBruteforcer.compute();
    if (improvedMoveBruteforcer.hasWinningMove()) {
      Output.debug("Winning move: " + improvedMoveBruteforcer.getWinningMove().toNotation());
    } else {
      Output.debug("Doesn't have winning move. Random move: "
          + improvedMoveBruteforcer.getRandomMove().toNotation());
    }

    Output.debug("-------------");

    MoveBruteforcer simpleMoveBruteforcer = new SimpleMoveBruteforcer(game);
    simpleMoveBruteforcer.compute();
    if (simpleMoveBruteforcer.hasWinningMove()) {
      Output.debug("Winning move: " + simpleMoveBruteforcer.getWinningMove().toNotation());
    } else {
      Output.debug("Doesn't have winning move. Random move: "
          + simpleMoveBruteforcer.getRandomMove().toNotation());
    }

    Output.debug("-------------");

    HashMap<String, Boolean> simplePositions = simpleMoveBruteforcer.getComputedPositions();
    HashMap<String, Boolean> improvedPositions = improvedMoveBruteforcer.getComputedPositions();
    for (String position : simplePositions.keySet()) {
      if (improvedPositions.containsKey(position)) {
        if (!improvedPositions.get(position).equals(simplePositions.get(position))) {
          Output.debug("Error: " + position + " is not " + simplePositions.get(position));
        }
      }
    }

    Output.debug("-------------");

    Output.debug("DONE");
  }
}
