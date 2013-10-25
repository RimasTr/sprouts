package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.ChildrenGenerator;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;

public class ImprovedMoveBruteforcer implements MoveBruteforcer {

  private Game game;
  private Move winningMove;
  private HashMap<String, Move> possiblePositions;
  private HashMap<String, Boolean> alreadyComputedPositions;

  public ImprovedMoveBruteforcer(Game game) {
    this.game = game;
  }

  public void compute() {
    alreadyComputedPositions = new HashMap<String, Boolean>();
    possiblePositions = getPossiblePositions(game);
    System.out.println("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      System.out.println("Checking " + possiblePositions.get(position).toNotation());
      if (!isWin(position)) {
        winningMove = possiblePositions.get(position);
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
    return possiblePositions.get(randomKey);
  }

  private boolean isWin(String currentPosition) {
    if (currentPosition.length() <= 1) {
      return false;
    }
    if (alreadyComputedPositions.containsKey(currentPosition)) {
      return alreadyComputedPositions.get(currentPosition);
    }

    Set<String> possiblePositions = getPossibleInternalPositions(currentPosition);
    // TODO: remove, debug
//    if (currentPosition.equals("1abc2cba.}!")) {
//      ArrayList list = new ArrayList(possiblePositions);
//      Collections.sort(list);
//      System.out.println("Blah " + list);
//    }
    for (String position : possiblePositions) {
      if (!isWin(position)) {
        if (!checkIfMatches(currentPosition, true)) {
          System.out.println("Wins by going to " + position);
        }
        alreadyComputedPositions.put(currentPosition, true);
        return true;
      }
    }
    if (!checkIfMatches(currentPosition, false)) {
      // System.out.println("All distinct moves: ");
      // for (String position : possiblePositions.keySet()) {
      // System.out.println(possiblePositions.get(position).getMove().toNotation());
    }
    // System.out.println("All moves: ");
    // getPossiblePositions(game, true);
    // }
    alreadyComputedPositions.put(currentPosition, false);
    return false;
  }

  private boolean checkIfMatches(String position, boolean expectedValue) {
    // if (!position.equals(pos)) {
    // return true;
    // }
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
      // System.out.println("Adding " + expectedValue);
      //
      // System.out.println("Position: ");
      // System.out.println(game.getPosition());
      // System.out.println("Moves history:");
      // for (Move intMove : game.getMovesHistory()) {
      // System.out.println(intMove.toNotation());
      // }
      return true;
    }
  }

  private Set<String> getPossibleInternalPositions(String position) {
    return new ChildrenGenerator(InternalPosition.fromString(position))
        .generateAllChildrenStrings();
  }

  private HashMap<String, Move> getPossiblePositions(Game game) {
    AllMovesGenerator allMovesGenerator = new AllMovesGenerator();
    Set<Move> moves = allMovesGenerator.generateAllMoves(game.getPosition());
    HashMap<String, Move> possiblePositions = new HashMap<String, Move>();
    for (Move move : moves) {
      Game clone = game.deepClone();
      clone.makeMove(move);
      InternalPosition internalPosition = InternalPosition.fromExternal(clone.getPosition());
      String internalPositionString = internalPosition.toString();
      possiblePositions.put(internalPositionString, move);
    }
    return possiblePositions;
  }

  public HashMap<String, Boolean> getComputedPositions() {
    return alreadyComputedPositions;
  }
}
