package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;

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

  // TODO: remove
  private String pos = "2AB.}AB.}!";

  public SimpleMoveBruteforcer(Game game) {
    this.game = game;
  }

  public void compute() {
    alreadyComputedPositions = new HashMap<String, Boolean>();
    possiblePositions = getPossiblePositions(game);
    System.out.println("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      System.out.println("Checking " + possiblePositions.get(position).getMove().toNotation());
      if (!isWin(possiblePositions.get(position).getGame(), position)) {
        winningMove = possiblePositions.get(position).getMove();
        System.out.println("Different positions: " + alreadyComputedPositions.size());
        return;
      }
    }
    System.out.println("Different positions: " + alreadyComputedPositions.size());
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
    if (currentPosition.length() <= 1) {
      return false;
    }
    if (alreadyComputedPositions.containsKey(currentPosition)) {
      return alreadyComputedPositions.get(currentPosition);
    }

    HashMap<String, MoveResult> possiblePositions = getPossiblePositions(game);

    // TODO: remove, debug
//    if (currentPosition.equals("1abc2cba.}!")) {
//      System.out.println(game.getMovesHistory());
//      ArrayList list = new ArrayList(possiblePositions.keySet());
//      Collections.sort(list);
//      System.out.println("Blah " + list);
//      // getPossiblePositions(game, true);
//    }
    for (String position : possiblePositions.keySet()) {
      if (!isWin(possiblePositions.get(position).getGame(), position)) {
        // if (!checkIfMatches(currentPosition, game, true)) {
        // System.out.println("Wins by " + possiblePositions.get(position).getMove().toNotation());
        // }
        alreadyComputedPositions.put(currentPosition, true);
        return true;
      }
    }
    // if (!checkIfMatches(currentPosition, game, false)) {
    // System.out.println("All distinct moves: ");
    // for (String position : possiblePositions.keySet()) {
    // System.out.println(possiblePositions.get(position).getMove().toNotation());
    // }
    // System.out.println("All moves: ");
    // getPossiblePositions(game, true);
    // }
    alreadyComputedPositions.put(currentPosition, false);
    return false;
  }

  private boolean checkIfMatches(String position, Game game, boolean expectedValue) {
    if (!position.equals(pos)) {
      return true;
    }
    if (alreadyComputedPositions.containsKey(position)) {
      // System.out.println("-- Get: " + alreadyComputedPositions.get(position));
      if (alreadyComputedPositions.get(position) != expectedValue) {
        System.out.println("----------------- Doesn't match: " + position);
        System.out.println("Should have been: " + expectedValue);

        System.out.println("Position: ");
        System.out.println(game.getPosition());
        System.out.println("Moves history:");
        for (Move intMove : game.getMovesHistory()) {
          System.out.println(intMove.toNotation());
        }

        return false;
      }
      return true;
    } else {
      System.out.println("Adding " + expectedValue);

      System.out.println("Position: ");
      System.out.println(game.getPosition());
      System.out.println("Moves history:");
      for (Move intMove : game.getMovesHistory()) {
        System.out.println(intMove.toNotation());
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
      System.out.println("All moves (" + moves.size() + ")");
    }
    for (Move move : moves) {
      Game clone = game.deepClone();
      clone.makeMove(move);
      InternalPosition internalPosition = InternalPosition.fromExternal(clone.getPosition());
      String internalPositionString = internalPosition.toString();
      if (debug) {
        System.out.println(move.toNotation() + " -> " + internalPositionString);
        System.out.println(clone.getPosition());
      }
      possiblePositions.put(internalPositionString, new MoveResult(move, clone));
    }
    // System.out.println("All possible positions (" + possiblePositions.keySet().size() + ")");
    // for (String positionString : possiblePositions.keySet()) {
    // System.out.println(positionString + "\t\t"
    // + possiblePositions.get(positionString).getMove().toNotation());
    // }
    return possiblePositions;
  }

  public HashMap<String, Boolean> getComputedPositions() {
    return alreadyComputedPositions;
  }
}
