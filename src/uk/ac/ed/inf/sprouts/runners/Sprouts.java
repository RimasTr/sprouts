package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.players.BruteforcePlayer;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

public class Sprouts {

  private static final String GAME_TYPE = "6+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new BruteforcePlayer(false, false);
  private static final Player PLAYER2 = new BruteforcePlayer(false, false);

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    //gameRunner.makeMoves(Arrays.asList());
    gameRunner.run();
  }
}
