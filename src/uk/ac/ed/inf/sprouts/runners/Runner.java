package uk.ac.ed.inf.sprouts.runners;

import java.util.Arrays;
import java.util.Scanner;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.players.BruteforcePlayer;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.players.UserPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.GameRunner;
import uk.ac.ed.inf.sprouts.utils.Output;

public class Runner {

  // Possible Players: RandomPlayer, BruteforcePlayer, StrategyPlayer, UserPlayer
  private static final Player USER_PLAYER = new UserPlayer();
  private static final Player COMPUTER_PLAYER = new RandomPlayer();

  public static void main(String[] args) {
    Player Player1;
    Player Player2;
    boolean output1 = false;
    boolean output2 = false;

    Output.debug("Enter game type:");
    Scanner scanner = new Scanner(System.in);
    String gameTypeString = scanner.nextLine();

    Output.debug("Do you want to go first (F) or second (S)?");
    String firstOrSecond = scanner.nextLine();
    if (firstOrSecond.toLowerCase().startsWith("f")) {
      Player1 = USER_PLAYER;
      Player2 = COMPUTER_PLAYER;
      output2 = true;
    } else {
      Player1 = COMPUTER_PLAYER;
      Player2 = USER_PLAYER;
      output1 = true;
    }

    GameRunner gameRunner = new GameRunner(gameTypeString, Player1, Player2, output1, output2);
    // gameRunner.makeMoves(Arrays.asList("2(7)2[1,3,4,5,6]", "3(8)4", "5(9)5", "4(10)3![1]"));
    gameRunner.run();
  }
}
