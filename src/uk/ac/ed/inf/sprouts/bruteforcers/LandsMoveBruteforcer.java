package uk.ac.ed.inf.sprouts.bruteforcers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.ChildrenGenerator;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;
import uk.ac.ed.inf.sprouts.internal.InternalPositionWithLands;

public class LandsMoveBruteforcer implements MoveBruteforcer {

  public static String DEBUG_POSITION = "ABC.}BC.}DE.FG.}DE.}FAG.}!--";

  private Game game;
  private Move winningMove;
  private HashMap<String, Move> possiblePositions;
  private HashMap<String, Integer> alreadyComputedPositions;

  public LandsMoveBruteforcer(Game game) {
    this.game = game;
  }

  public void compute() {
    alreadyComputedPositions = new HashMap<String, Integer>();
    isWin(InternalPosition.fromExternal(game.getPosition()).toString());
//    possiblePositions = getPossiblePositions(game);
//    System.out.println("Possible moves: " + possiblePositions.size());
//    for (String position : possiblePositions.keySet()) {
//      System.out.println("Checking " + possiblePositions.get(position).toNotation());
//      if (!isWin(position)) {
//        winningMove = possiblePositions.get(position);
//        System.out.println("Different positions: " + alreadyComputedPositions.size());
//        return;
//      }
//    }
//    System.out.println("Different positions: " + alreadyComputedPositions.size());
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
    int result = computeNimber(currentPosition);
    System.out.println("Result of " + currentPosition + " is " + result);
    return result > 0;
  }

  private int computeNimber(String currentPosition) {
    //System.out.println("Calculating " + currentPosition);
    if (currentPosition.length() <= 1) {
      return 0;
    }
    if (alreadyComputedPositions.containsKey(currentPosition)) {
      return alreadyComputedPositions.get(currentPosition);
    }
    InternalPositionWithLands lands = InternalPositionWithLands.fromString(currentPosition);
    if (lands.size() > 1) {
      // TODO: maybe store combined result as well?
      //System.out.println("Splitting " + currentPosition + " --------- ");
      return computeNimberOfSeveralLands(lands);
    }
    // Else only one land
    Set<String> possiblePositions = getPossibleInternalPositions(lands.get(0));
    Set<Integer> nimbers = new HashSet<Integer>();
    for (String position : possiblePositions) {
      nimbers.add(computeNimber(position));
    }
    int result = 0;
    while (nimbers.contains(result)) {
      result++;
    }
    //System.out.println("Nimber of " + currentPosition + " is " + result);
    alreadyComputedPositions.put(currentPosition, result);
    return result;
  }

  private int computeNimberOfSeveralLands(InternalPositionWithLands lands) {
    int result = 0;
    for (InternalPosition land : lands) {
      //System.out.println("computing " + land.toString());
      int nimber = computeNimber(land.toString());
      System.out.println("of " + land.toString() + " is " + nimber);
      result = result ^ nimber;
    }
    //System.out.println("Total: " + result);
    return result;
  }

  private Set<String> getPossibleInternalPositions(InternalPosition position) {
    return new ChildrenGenerator(position).generateAllChildrenStrings();
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
    // TODO: maybe implement something for testing
    return null;
  }
}
