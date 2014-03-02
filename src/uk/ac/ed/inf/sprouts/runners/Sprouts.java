package uk.ac.ed.inf.sprouts.runners;

import java.util.Arrays;

import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.players.UserPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

public class Sprouts {

  private static final String GAME_TYPE = "4+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new RandomPlayer();
  private static final Player PLAYER2 = new RandomPlayer();

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    // gameRunner.makeMoves(Arrays.asList("2(7)2[1,3,4,5,6]", "3(8)4", "5(9)5", "4(10)3![1]"));
    gameRunner.run();
  }
}
