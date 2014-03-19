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
  private Move optimalMove;
  private String optimalPosition;
  private double bestOptimalMoveRatio = 2.0; // more than 1
  private HashMap<String, Move> possiblePositions;
  private HashMap<String, Boolean> alreadyComputedPositions;
  private HashMap<String, Boolean> savedPositions;
  private int save = 0;
  private boolean computeOptimalMove = false;

  private final int DEBUG_DEPTH = 0;

  public ImprovedLandsMoveBruteforcer(Game game) {
    this.game = game;
    this.alreadyComputedPositions = new HashMap<String, Boolean>();
    this.savedPositions = new HashMap<String, Boolean>();
  }

  public ImprovedLandsMoveBruteforcer(Game game, HashMap<String, Boolean> savedPositions,
      boolean computeOptimalMove) {
    this(game);
    this.alreadyComputedPositions = savedPositions;
    this.computeOptimalMove = computeOptimalMove;
  }

  public ImprovedLandsMoveBruteforcer(Game game, HashMap<String, Boolean> savedPositions) {
    this(game, savedPositions, false);
  }

  public ImprovedLandsMoveBruteforcer(Game game, boolean computeOptimalMove) {
    this(game);
    this.computeOptimalMove = computeOptimalMove;
  }

  public ImprovedLandsMoveBruteforcer(Game game, int save) {
    this(game);
    this.save = save;
    this.computeOptimalMove = true;
  }

  public ImprovedLandsMoveBruteforcer(Game game, HashMap<String, Boolean> savedPositions, int save) {
    this(game, savedPositions, true);
    this.save = save;
  }

  public void quickCompute() {
    if (possiblePositions == null) {
      possiblePositions = getPossiblePositions(game);
    }
    for (String position : possiblePositions.keySet()) {
      if (alreadyComputedPositions.containsKey(position + 0)
          && !alreadyComputedPositions.get(position + 0)) {
        winningMove = possiblePositions.get(position);
        return;
      }
    }
  }

  public void compute() {
    if (possiblePositions == null) {
      possiblePositions = getPossiblePositions(game);
    }
    Output.debug("Possible moves: " + possiblePositions.size());
    int total = possiblePositions.size();
    int i = 0;
    // Output.debug("Possible moves: " + possiblePositions);
    for (String position : possiblePositions.keySet()) {
      Output.debug("Checking " + possiblePositions.get(position).toNotation() + " " + (++i) + "/"
          + total);
      if (!isWin(position, 0, 0)) {
        winningMove = possiblePositions.get(position);
        finish();
        return;
      }
    }
    if (!hasWinningMove()) {
      computeOptimalMove();
    }
    finish();
  }

  private void computeOptimalMove() {
    if (optimalPosition != null) {
      Output.debug("Optimal ratio: " + bestOptimalMoveRatio);
      optimalMove = possiblePositions.get(optimalPosition);
      // Output.debug("Optimal position: " + optimalPosition);
      // Output.debug("Optimal move: " + optimalMove);
    }
  }

  private void finish() {
    Output.debug("Different positions: " + alreadyComputedPositions.size());
    if (save > 0) {
      Output.debug("Saved positions: " + savedPositions.size());
      SavedPositionsHandler.savePositionsToFile(savedPositions);
    }
    if (hasWinningMove()) {
      Output.debug("Winning position!");
    }
  }

  public boolean hasWinningMove() {
    return winningMove != null;
  }

  public boolean hasOptimalMove() {
    return optimalMove != null;
  }

  public Move getWinningMove() {
    return winningMove;
  }

  public Move getOptimalMove() {
    return optimalMove;
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
    String d = "";
    if (depth < DEBUG_DEPTH) {
      for (int i = 0; i < depth; i++) {
        d += "--";
      }
      Output.debug(d + "Calculating " + currentPosition + " " + nimber + " " + depth);
    }
    if (currentPosition.length() <= 1) {
      return nimber != 0;
    }
    // if (!needsToComputeAll(depth)) {
    if (alreadyComputedPositions.containsKey(currentPosition + nimber)) {
      return alreadyComputedPositions.get(currentPosition + nimber);
    }
    // }
    InternalPositionWithLands lands = InternalPositionWithLands.fromString(currentPosition);
    if (lands.size() > 1) {
      // TODO: maybe store combined result as well?
      // Output.debug("Splitting " + currentPosition + " --------- ");
      List<String> landStrings =
          Lists.newArrayList(Iterables.transform(lands, InternalPosition.toString));
      Collections.sort(landStrings, new LandComparator());
      int n = computeNimberOfSeveralLands(landStrings.subList(1, lands.size()), depth + 1);
      return isWin(landStrings.get(0), nimber ^ n, depth + 1);
    }
    // Else only one land
    boolean result = false;
    for (int i = 0; i < nimber; i++) {
      if (!isWin(currentPosition, i, depth + 1)) {
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
    if (depth < DEBUG_DEPTH) {
      Output.debug(d + "Possible: " + possiblePositions.size() + " " + possiblePositions);
    }
    int total = possiblePositions.size();
    int i = 0;
    // Output.debug("Children: " + possiblePositions);
    // First check already computed positions maybe?
    if (!needsToComputeAll(depth)) {
      for (String position : possiblePositions) {
        if (alreadyComputedPositions.containsKey(position + nimber)
            && !alreadyComputedPositions.get(position + nimber)) {
          // Output.debug(currentPosition + "\ttrue 2");
          savePosition(currentPosition, nimber, true, depth);
          return true;
        }
      }
    }
    // Check all positions:
    int totalPossiblePositions = possiblePositions.size();
    int winningPositions = 0;
    result = false;
    for (String position : possiblePositions) {
      if (depth < DEBUG_DEPTH - 1) {
        Output.debug(d + "--" + (++i) + "/" + total);
      }
      if (!isWin(position, nimber, depth + 1)) {
        // Output.debug(currentPosition + "\ttrue 3");
        savePosition(currentPosition, nimber, true, depth);
        result = true;
        if (!needsToComputeAll(depth)) {
          return result;
        }
        winningPositions++;
      }
    }
    if (depth == 0 && computeOptimalMove) {
      double currentRatio = 1.0 * winningPositions / totalPossiblePositions;
      // Output.debug("Current ration: " + currentRatio);
      if (currentRatio < bestOptimalMoveRatio) {
        optimalPosition = currentPosition;
        bestOptimalMoveRatio = currentRatio;
      }
    }
    savePosition(currentPosition, nimber, result, depth);
    // Output.debug(currentPosition + "\tfalse");
    return result;
  }

  private boolean needsToComputeAll(int depth) {
    return depth < save || (computeOptimalMove && depth == 0);
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
