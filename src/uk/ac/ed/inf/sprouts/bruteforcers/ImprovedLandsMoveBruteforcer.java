package uk.ac.ed.inf.sprouts.bruteforcers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.bruteforcers.utils.LandComparator;
import uk.ac.ed.inf.sprouts.external.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.ChildrenGenerator;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;
import uk.ac.ed.inf.sprouts.internal.InternalPositionWithLands;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ImprovedLandsMoveBruteforcer implements MoveBruteforcer {

  public static String DEBUG_POSITION = "ABCD.}CBAD.}!";

  private Game game;
  private Move winningMove;
  private HashMap<String, Move> possiblePositions;
  private HashMap<String, Boolean> alreadyComputedPositions;

  public ImprovedLandsMoveBruteforcer(Game game) {
    this.game = game;
  }

  public void compute() {
    alreadyComputedPositions = new HashMap<String, Boolean>();
    possiblePositions = getPossiblePositions(game);
    System.out.println("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      System.out.println("Checking " + possiblePositions.get(position).toNotation());
      if (!isWin(position, 0)) {
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

  private boolean isWin(String currentPosition, int nimber) {
    //System.out.println("Calculating " + currentPosition + " " + nimber);
    if (currentPosition.length() <= 1) {
      return nimber != 0;
    }
    if (alreadyComputedPositions.containsKey(currentPosition + nimber)) {
      return alreadyComputedPositions.get(currentPosition + nimber);
    }
    InternalPositionWithLands lands = InternalPositionWithLands.fromString(currentPosition);
    if (lands.size() > 1) {
      // TODO: maybe store combined result as well?
      // System.out.println("Splitting " + currentPosition + " --------- ");
      List<String> landStrings =
          Lists.newArrayList(Iterables.transform(lands, InternalPosition.toString));
      Collections.sort(landStrings, new LandComparator());
      int n = computeNimberOfSeveralLands(landStrings.subList(1, lands.size()));
      return isWin(landStrings.get(0), nimber ^ n);
    }
    // Else only one land
    for (int i = 0; i < nimber; i++) {
      if (!isWin(currentPosition, i)) {
        alreadyComputedPositions.put(currentPosition + nimber, true);
        // System.out.println(currentPosition + "\ttrue 1");
        return true;
      }
    }
    Set<String> possiblePositions = getPossibleInternalPositions(lands.get(0));
    // System.out.println("Children: " + possiblePositions);
    for (String position : possiblePositions) {
      // First check already computed possitions maybe?
      if (alreadyComputedPositions.containsKey(position + nimber)
          && !alreadyComputedPositions.get(position + nimber)) {
        // System.out.println(currentPosition + "\ttrue 2");
        alreadyComputedPositions.put(currentPosition + nimber, true);
        return true;
      }
    }
    for (String position : possiblePositions) {
      if (!isWin(position, nimber)) {
        // System.out.println(currentPosition + "\ttrue 3");
        alreadyComputedPositions.put(currentPosition + nimber, true);
        return true;
      }
    }
    alreadyComputedPositions.put(currentPosition + nimber, false);
    // System.out.println(currentPosition + "\tfalse");
    return false;
  }

  private int computeNimberOfSeveralLands(List<String> list) {
    int result = 0;
    for (String land : list) {
      // System.out.println("computing " + land.toString());
      int nimber = computeNimber(land);
      // System.out.println("of " + land.toString() + " is " + nimber);
      result = result ^ nimber;
    }
    // System.out.println("Total: " + result);
    return result;
  }

  private int computeNimber(String position) {
    int n = 0;
    while (true) {
      if (!isWin(position, n++)) {
        return n - 1;
      }
    }
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
