package uk.ac.ed.inf.sprouts.runners.utils;

import java.util.List;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.players.BruteforcePlayer;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.utils.Output;

/**
 * Computes the next move for the given game from the given player.
 *
 * @author Rimas
 */
public class OneMoveRunner {

  private final Game game;
  private final Player player;

  public OneMoveRunner(String gameType, Player player) {
    this.game = Game.fromString(gameType);
    this.player = player;
  }

  public void makeMoves(List<String> moves) {
    for (String move : moves) {
      game.makeMove(Move.fromString(move));
    }
  }

  public Move getMove() {
    if (game.isOver()) {
//      Output.debug("GameInfo", "Other player won...");
      return null;
    }

    Move move = player.getMove(game);
    game.makeMove(move);
    Output.debug("NewPosition", "Position: " + game.getPosition());
//    if (game.isOver()) {
//      Output.debug("GameInfo", "Computer won!");
//    }
    return move;
  }

  public static void main(String[] args) {
    
    OneMoveRunner runner = new OneMoveRunner("4+", new BruteforcePlayer(false, false));
    //runner.makeMoves(Arrays.asList("1(5)2", "2(6@3)!5", "2!(7@3)!6![4]"));
    runner.getMove();
  }
}
