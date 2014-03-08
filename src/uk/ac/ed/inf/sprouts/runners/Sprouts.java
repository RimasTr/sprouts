package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.players.BruteforcePlayer;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.UserPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

public class Sprouts {

  private static final String GAME_TYPE = "6+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new BruteforcePlayer(false, false);
  private static final Player PLAYER2 = new UserPlayer();

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    //gameRunner.makeMoves(Arrays.asList("1(5@2)1[2,3]", "5(6@3)1!", "2(7@3)2[1,3,5,6]", "2(8@3)7", "8!(9)3", "3(10@1)9[1,5,6]", "3(11)!6", "11(12@1)10!", "4(13@1)4", "13(14)4!"));
    gameRunner.run();
  }
}
