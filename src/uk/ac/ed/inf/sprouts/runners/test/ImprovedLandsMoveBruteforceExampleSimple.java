package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedLandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.utils.Output;

public class ImprovedLandsMoveBruteforceExampleSimple {

  public static void main(String[] args) {
    Game game = Game.fromString("6+");
    // game.makeMove("4(5)4[1]");
    // game.makeMove("5(7)5[1,2,4,6]");
    // game.makeMove("4(8)4[1,2,5,7]");
    // game.makeMove("5(9@1)7");
    // game.makeMove("2(10)2[1,4,5,7,8,9]");
    // game.makeMove("1(11)8");
    // game.makeMove("2(12)1");
    // game.makeMove("1(13)10[2,4,5,7,8,9,11,12]");
    // game.makeMove("6(14)4");
    // game.makeMove("3(15)3");
    // game.makeMove("12(16)!13[1,2,5,7,9,10]");
    // game.makeMove("3(17)15");

    // game.makeMove("5(7)5[1,2,4,6]");
    // game.makeMove("4(8)4[1,2,5,7]");
    // game.makeMove("2(9)2");
    // game.makeMove("1(10)1[4,5,7,8]");
    // game.makeMove("6(11)8");
    // game.makeMove("2(12)9");
    // game.makeMove("1(13@4)10![4,5,7,8]");
    // game.makeMove("5(14@4)7![1,4,8,10,13]");
    // game.makeMove("13(15)14");
    // game.makeMove("6(16)6[4,8,11]");
    // game.makeMove("16(17)4");
    // game.makeMove("11(18)17[16,4,6,8]");
    // game.makeMove("3(19)3");
    // game.makeMove("3(20@5)19![5,7]");

    long time = System.currentTimeMillis();
    // HashMap<String, Boolean> savedPositions = SavedPositionsHandler.getSavedPositions();
    // ImprovedLandsMoveBruteforcer moveBruteforcer =
    // new ImprovedLandsMoveBruteforcer(game, 6);
    // MoveBruteforcer moveBruteforcer = new ImprovedLandsMoveBruteforcer(game, 6);
    ImprovedLandsMoveBruteforcer moveBruteforcer =
        new ImprovedLandsMoveBruteforcer(game);
    moveBruteforcer.compute();
    if (moveBruteforcer.hasWinningMove()) {
      Output.debug("Winning move: " + moveBruteforcer.getWinningMove().toNotation());
    } else if (moveBruteforcer.hasOptimalMove()) {
      Output.debug("Doesn't have winning move. Optimal move: "
          + moveBruteforcer.getOptimalMove().toNotation());
    } else {
      Output.debug("Doesn't have winning move. Random move: "
          + moveBruteforcer.getRandomMove().toNotation());
    }
    Output.debug("Time: " + (System.currentTimeMillis() - time) / 1000.0);
  }
}
