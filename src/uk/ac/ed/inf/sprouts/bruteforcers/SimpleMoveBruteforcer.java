package uk.ac.ed.inf.sprouts.bruteforcers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;
import uk.ac.ed.inf.sprouts.utils.Output;

public class SimpleMoveBruteforcer implements MoveBruteforcer {

  private class MoveResult {

    private final Move move;
    private final Game game;

    public MoveResult(Move move, Game game) {
      this.move = move;
      this.game = game;
    }

    public Move getMove() {
      return move;
    }

    public Game getGame() {
      return game;
    }
  }

  private Game game;
  private Move winningMove;
  private HashMap<String, MoveResult> possiblePositions;
  private HashMap<String, Boolean> alreadyComputedPositions;
  private int positions = 0;

  // TODO: remove
  private String pos = "2AB.}AB.}!";

  public SimpleMoveBruteforcer(Game game) {
    this.game = game;
  }

  public void compute() {
    alreadyComputedPositions = new HashMap<String, Boolean>();
    possiblePositions = getPossiblePositions(game);
    Output.debug("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      Output.debug("Checking " + possiblePositions.get(position).getMove().toNotation());
      if (!isWin(possiblePositions.get(position).getGame(), position)) {
        winningMove = possiblePositions.get(position).getMove();
        Output.debug("Different positions: " + alreadyComputedPositions.size());
        Output.debug("Checked positions: " + positions);
        return;
      }
    }
    Output.debug("Different positions: " + alreadyComputedPositions.size());
    Output.debug("Checked positions: " + positions);
  }

  public boolean hasWinningMove() {
    return winningMove != null;
  }

  public Move getWinningMove() {
    return winningMove;
  }

  public Move getRandomMove() {
    if (game.isOver()) {
      return null;
    }
    Random random = new Random();
    List<String> keys = new ArrayList<String>(possiblePositions.keySet());
    String randomKey = keys.get(random.nextInt(keys.size()));
    return possiblePositions.get(randomKey).getMove();
  }

  private boolean isWin(Game game, String currentPosition) {
    positions++;
    if (currentPosition.length() <= 1) {
      return false;
    }
    if (alreadyComputedPositions.containsKey(currentPosition)) {
      return alreadyComputedPositions.get(currentPosition);
    }

    HashMap<String, MoveResult> possiblePositions = getPossiblePositions(game);

    // TODO: remove, debug
    if (currentPosition.equals(ImprovedMoveBruteforcer.DEBUG_POSITION)) {
      Output.debug("" + game.getMovesHistory());
      ArrayList<String> list = new ArrayList<String>(possiblePositions.keySet());
      Collections.sort(list);
      Output.debug("Debug pos: " + list);
      Output.debug("History: " + game.getMovesHistory());
      getPossiblePositions(game, true);
    }
    for (String position : possiblePositions.keySet()) {
      if (!isWin(possiblePositions.get(position).getGame(), position)) {
        // if (!checkIfMatches(currentPosition, game, true)) {
        // Output.debug("Wins by " + possiblePositions.get(position).getMove().toNotation());
        // }
        alreadyComputedPositions.put(currentPosition, true);
        return true;
      }
    }
    // if (!checkIfMatches(currentPosition, game, false)) {
    // Output.debug("All distinct moves: ");
    // for (String position : possiblePositions.keySet()) {
    // Output.debug(possiblePositions.get(position).getMove().toNotation());
    // }
    // Output.debug("All moves: ");
    // getPossiblePositions(game, true);
    // }
    alreadyComputedPositions.put(currentPosition, false);
    return false;
  }

  @SuppressWarnings("unused")
  // debug code
  private boolean checkIfMatches(String position, Game game, boolean expectedValue) {
    if (!position.equals(pos)) {
      return true;
    }
    if (alreadyComputedPositions.containsKey(position)) {
      // Output.debug("-- Get: " + alreadyComputedPositions.get(position));
      if (alreadyComputedPositions.get(position) != expectedValue) {
        Output.debug("----------------- Doesn't match: " + position);
        Output.debug("Should have been: " + expectedValue);

        Output.debug("Position: ");
        Output.debug("" + game.getPosition());
        Output.debug("Moves history:");
        for (Move intMove : game.getMovesHistory()) {
          Output.debug(intMove.toNotation());
        }

        return false;
      }
      return true;
    } else {
      Output.debug("Adding " + expectedValue);

      Output.debug("Position: ");
      Output.debug("" + game.getPosition());
      Output.debug("Moves history:");
      for (Move intMove : game.getMovesHistory()) {
        Output.debug(intMove.toNotation());
      }
      return false;
    }
  }

  private HashMap<String, MoveResult> getPossiblePositions(Game game) {
    return getPossiblePositions(game, false);
  }

  private HashMap<String, MoveResult> getPossiblePositions(Game game, boolean debug) {
    AllMovesGenerator allMovesGenerator = new AllMovesGenerator();
    Set<Move> moves = allMovesGenerator.generateAllMoves(game.getPosition());
    HashMap<String, MoveResult> possiblePositions = new HashMap<String, MoveResult>();
    if (debug) {
      Output.debug("All moves (" + moves.size() + ")");
    }
    for (Move move : moves) {
      Game clone = game.deepClone();
      clone.makeMove(move);
      InternalPosition internalPosition = InternalPosition.fromExternal(clone.getPosition());
      String internalPositionString = internalPosition.toString();
      if (debug) {
        Output.debug(move.toNotation() + " -> " + internalPositionString);
        Output.debug("" + clone.getPosition());
      }
      possiblePositions.put(internalPositionString, new MoveResult(move, clone));
    }
    // Output.debug("All possible positions (" + possiblePositions.keySet().size() + ")");
    // for (String positionString : possiblePositions.keySet()) {
    // Output.debug(positionString + "\t\t"
    // + possiblePositions.get(positionString).getMove().toNotation());
    // }
    return possiblePositions;
  }

  public HashMap<String, Boolean> getComputedPositions() {
    return alreadyComputedPositions;
  }
}
