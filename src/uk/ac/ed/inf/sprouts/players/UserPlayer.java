package uk.ac.ed.inf.sprouts.players;

import java.util.Scanner;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.utils.Output;

/**
 * Reads the moves from the standard input.
 *
 * @author Rimas
 */
public class UserPlayer implements Player {

  @Override
  public Move getMove(Game game) {
    Scanner scanner = new Scanner(System.in);
    Output.debug("GameInfo", "Enter your move:");
    String moveString = scanner.nextLine();
    return Move.fromString(moveString);
  }
}
