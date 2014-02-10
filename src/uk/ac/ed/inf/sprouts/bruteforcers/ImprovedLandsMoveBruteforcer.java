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
import uk.ac.ed.inf.sprouts.utils.Output;
import uk.ac.ed.inf.sprouts.utils.SavedPositionsHandler;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ImprovedLandsMoveBruteforcer implements MoveBruteforcer {

  public static String DEBUG_POSITION = "ABCD.}CBAD.}!";

  private Game game;
  private Move winningMove;
  private HashMap<String, Move> possiblePositions;
  private HashMap<String, Boolean> alreadyComputedPositions;
  private HashMap<String, Boolean> savedPositions;
  private int save = 0;

  public ImprovedLandsMoveBruteforcer(Game game) {
    this.game = game;
    this.alreadyComputedPositions = new HashMap<String, Boolean>();
    this.savedPositions = new HashMap<String, Boolean>();
  }

  public ImprovedLandsMoveBruteforcer(Game game, HashMap<String, Boolean> savedPositions) {
    this(game);
    this.alreadyComputedPositions = savedPositions;
  }

  public ImprovedLandsMoveBruteforcer(Game game, int save) {
    this(game);
    this.save = save;
  }

  public void compute() {
    possiblePositions = getPossiblePositions(game);
    Output.debug("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      Output.debug("Checking " + possiblePositions.get(position).toNotation());
      if (!isWin(position, 0, 0)) {
        winningMove = possiblePositions.get(position);
        finish();
        return;
      }
    }
    finish();
  }

  private void finish() {
    Output.debug("Different positions: " + alreadyComputedPositions.size());
    Output.debug("Saved positions: " + savedPositions.size());
    if (save > 0) {
      SavedPositionsHandler.savePositionsToFile(savedPositions);
    }
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
    if (possiblePositions == null) {
      possiblePositions = getPossiblePositions(game);
    }
    Random random = new Random();
    List<String> keys = new ArrayList<String>(possiblePositions.keySet());
    String randomKey = keys.get(random.nextInt(keys.size()));
    return possiblePositions.get(randomKey);
  }

  private boolean isWin(String currentPosition, int nimber, int depth) {
    if (depth < 3) {
      Output.debug("Calculating " + currentPosition + " " + nimber + " " + depth);
    }
    if (currentPosition.length() <= 1) {
      return nimber != 0;
    }
    if (alreadyComputedPositions.containsKey(currentPosition + nimber)) {
      return alreadyComputedPositions.get(currentPosition + nimber);
    }
    InternalPositionWithLands lands = InternalPositionWithLands.fromString(currentPosition);
    if (lands.size() > 1) {
      // TODO: maybe store combined result as well?
      // Output.debug("Splitting " + currentPosition + " --------- ");
      List<String> landStrings =
          Lists.newArrayList(Iterables.transform(lands, InternalPosition.toString));
      Collections.sort(landStrings, new LandComparator());
      int n = computeNimberOfSeveralLands(landStrings.subList(1, lands.size()), depth);
      return isWin(landStrings.get(0), nimber ^ n, depth);
    }
    // Else only one land
    boolean result = false;
    for (int i = 0; i < nimber; i++) {
      if (!isWin(currentPosition, i, depth)) {
        savePosition(currentPosition, nimber, true, depth);
        // Output.debug(currentPosition + "\ttrue 1");
        result = true;
        if (depth >= save) {
          return result;
        }
      }
    }
    if (result) {
      return result;
    }
    Set<String> possiblePositions = getPossibleInternalPositions(lands.get(0));
    // Output.debug("Children: " + possiblePositions);
    for (String position : possiblePositions) {
      // First check already computed possitions maybe?
      if (alreadyComputedPositions.containsKey(position + nimber)
          && !alreadyComputedPositions.get(position + nimber)) {
        // Output.debug(currentPosition + "\ttrue 2");
        savePosition(currentPosition, nimber, true, depth);
        return true;
      }
    }
    result = false;
    for (String position : possiblePositions) {
      if (!isWin(position, nimber, depth + 1)) {
        // Output.debug(currentPosition + "\ttrue 3");
        savePosition(currentPosition, nimber, true, depth);
        result = true;
        if (depth >= save) {
          return result;
        }
      }
    }
    savePosition(currentPosition, nimber, result, depth);
    // Output.debug(currentPosition + "\tfalse");
    return result;
  }

  private int computeNimberOfSeveralLands(List<String> list, int depth) {
    int result = 0;
    for (String land : list) {
      // Output.debug("computing " + land.toString());
      int nimber = computeNimber(land, depth);
      // Output.debug("of " + land.toString() + " is " + nimber);
      result = result ^ nimber;
    }
    // Output.debug("Total: " + result);
    return result;
  }

  private int computeNimber(String position, int depth) {
    int n = 0;
    while (true) {
      if (!isWin(position, n++, depth)) {
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
    return alreadyComputedPositions;
  }

  public HashMap<String, Boolean> getSavedPositions() {
    return savedPositions;
  }

  private void savePosition(String currentPosition, int nimber, boolean value, int depth) {
    alreadyComputedPositions.put(currentPosition + nimber, value);
    if (depth < save) {
      savedPositions.put(currentPosition + nimber, value);
    }
  }
}
