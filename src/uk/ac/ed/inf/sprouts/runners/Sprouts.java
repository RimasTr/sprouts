package uk.ac.ed.inf.sprouts.runners;

import java.util.Arrays;

import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.players.UserPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

public class Sprouts {

  private static final String GAME_TYPE = "4+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new UserPlayer();
  private static final Player PLAYER2 = new UserPlayer();

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    gameRunner.makeMoves(Arrays.asList("2(5@1)2[1,4]", "1(6)4", "3(7@2)3[2,5]", "6!(8@2)1", "3(9@2)7![2,5]", "8(10)1", "2(11@6)5", "11!(12)!9"));
    gameRunner.run();
  }
}
