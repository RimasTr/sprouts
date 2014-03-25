package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.players.BruteforcePlayer;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

public class Sprouts {

  private static final String GAME_TYPE = "4+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new BruteforcePlayer(true, true);
  private static final Player PLAYER2 = new RandomPlayer();

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    // gameRunner.makeMoves(Arrays.asList("1(7)3", "1(8@2)3![4,5]", "3!(9@2)7![1,8]"));
    gameRunner.run();
  }
}
