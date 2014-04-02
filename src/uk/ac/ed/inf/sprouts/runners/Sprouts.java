package uk.ac.ed.inf.sprouts.runners;

import java.util.Arrays;

import uk.ac.ed.inf.sprouts.players.BruteforcePlayer;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

public class Sprouts {

  private static final String GAME_TYPE = "7+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new BruteforcePlayer(true, true);
  private static final Player PLAYER2 = new RandomPlayer();

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    gameRunner.makeMoves(Arrays.asList("1(8)2", "2(9)2[3]", "3(10)9", "8(11)4", "4(12)4[]", "6(13)6", "6(14)13", "5(15)5[7]"));
    // gameRunner.run();
  }
}
