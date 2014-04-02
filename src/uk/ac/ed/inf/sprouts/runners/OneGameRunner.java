package uk.ac.ed.inf.sprouts.runners;

import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.players.SmartPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;

/**
 * Runs one game between two players.
 * 
 * @author Rimas
 */
public class OneGameRunner {

  private static final String GAME_TYPE = "7+";
  // Possible Players: SmartPlayer, RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new SmartPlayer();
  private static final Player PLAYER2 = new RandomPlayer();

  public static void main(String[] args) {
    GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
    // gameRunner.makeMoves(Arrays.asList());
    gameRunner.run();
  }
}
