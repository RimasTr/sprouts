package uk.ac.ed.inf.sprouts.runners;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.players.SmartPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;
import uk.ac.ed.inf.sprouts.utils.Output;

import com.google.common.base.Joiner;

/**
 * Runs a number of games between two players and outputs statistics (including the number of
 * survivors for each game).
 * 
 * @author Rimas
 */
public class PlayersEvaluator {

  private static final String GAME_TYPE = "4+";
  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player PLAYER1 = new RandomPlayer();
  private static final Player PLAYER2 = new SmartPlayer();
  private static final int NUM_OF_GAMES = 100;

  public static void main(String[] args) {

    List<Integer> survivorsList = new ArrayList<Integer>();
    int firstPlayerWon = 0;
    int survivorsSum = 0;
    int survivorsMin = 999;
    int survivorsMax = 0;

    for (int i = 0; i < NUM_OF_GAMES; i++) {
      GameRunner gameRunner = new GameRunner(GAME_TYPE, PLAYER1, PLAYER2);
      if (gameRunner.run() == 0) {
        firstPlayerWon++;
      }
      int survivors = gameRunner.getSurvivors();
      Output.debug("--------------------------- Survivors: " + survivors);
      Output.debug("--------------------------- " + firstPlayerWon + "/" + (i + 1));
      survivorsList.add(survivors);
      survivorsSum += survivors;
      survivorsMin = Math.min(survivorsMin, survivors);
      survivorsMax = Math.max(survivorsMax, survivors);
    }
    Output.debug("First player won: " + firstPlayerWon + "/" + NUM_OF_GAMES);
    Output.debug("Survivors:\n" + Joiner.on(',').join(survivorsList));
    Output.debug("Survivors min: " + survivorsMin);
    Output.debug("Survivors average: " + 1.0 * survivorsSum / NUM_OF_GAMES);
    Output.debug("Survivors max: " + survivorsMax);
  }
}
