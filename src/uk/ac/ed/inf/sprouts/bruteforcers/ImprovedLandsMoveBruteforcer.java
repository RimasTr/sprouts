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

/**
 * The main brute-force search algorithm used in the application. Takes advantage of splitting
 * positions into lands and uses nimbers to compute the outcomes.
 * 
 * @author Rimas
 */
public class ImprovedLandsMoveBruteforcer implements MoveBruteforcer {

  // The position that is being analysed:
  private Game game;
  // The move that leads to winning position:
  private Move winningMove;
  // The move that leads to optimal position (in case winning move doesn't exist):
  private Move optimalMove;
  private String optimalPosition; // To be able to recover optimal move
  private double bestOptimalMoveRatio = 2.0; // Initial value has to be more than 1
  private HashMap<String, Move> possiblePositions; // Children of the position
  private HashMap<String, Boolean> alreadyComputedPositions; // Hashed positions
  private HashMap<String, Boolean> savedPositions; // If we're going to save anything to a text file
  private int save = 0; // How many layers of the search-tree will be saved in the text file
  private boolean computeOptimalMove = false;

  // Layers of search tree for which more output should be generated:
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

  /**
   * Check if we have the result in already computed positions list.
   */
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
    // Get the possible children:
    if (possiblePositions == null) {
      possiblePositions = getPossiblePositions(game);
    }
    Output.debug("Debug", "Possible moves: " + possiblePositions.size());

    // Check all children until a winning move is found:
    for (String position : possiblePositions.keySet()) {
      Output.debug("Debug", "Checking " + possiblePositions.get(position).toNotation());
      if (!isWin(position, 0, 0)) {
        winningMove = possiblePositions.get(position);
        finish();
        return;
      }
    }

    // If no winning move was found, compute an optimal move:
    if (!hasWinningMove()) {
      computeOptimalMove();
    }
    finish();
  }

  private void computeOptimalMove() {
    if (optimalPosition != null) {
      Output.debug("Debug", "Optimal ratio: " + bestOptimalMoveRatio);
      optimalMove = possiblePositions.get(optimalPosition);
    }
  }

  private void finish() {
    Output.debug("NewPosition", "Different positions: " + alreadyComputedPositions.size());
    if (save > 0) {
      Output.debug("Debug", "Saved positions: " + savedPositions.size());
      SavedPositionsHandler.savePositionsToFile(savedPositions);
    }
    if (hasWinningMove()) {
      Output.debug("Debug", "Winning position!");
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
      Output.debug("Debug", d + "Calculating " + currentPosition + " " + nimber + " " + depth);
    }
    if (currentPosition.length() <= 1) {
      return nimber != 0;
    }
    if (!needsToComputeAll(depth)) {
      if (alreadyComputedPositions.containsKey(currentPosition + nimber)) {
        return alreadyComputedPositions.get(currentPosition + nimber);
      }
    }
    InternalPositionWithLands lands = InternalPositionWithLands.fromString(currentPosition);
    if (lands.size() > 1) {
      // TODO: maybe store combined result as well?
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
      Output.debug("Debug", d + "Possible: " + possiblePositions.size() + " " + possiblePositions);
    }
    int total = possiblePositions.size();
    int i = 0;
    // First check already computed positions maybe?
    if (!needsToComputeAll(depth)) {
      for (String position : possiblePositions) {
        if (alreadyComputedPositions.containsKey(position + nimber)
            && !alreadyComputedPositions.get(position + nimber)) {
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
        Output.debug("Debug", d + "--" + (++i) + "/" + total);
      }
      if (!isWin(position, nimber, depth + 1)) {
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
      if (currentRatio < bestOptimalMoveRatio) {
        optimalPosition = currentPosition;
        bestOptimalMoveRatio = currentRatio;
      }
    }
    savePosition(currentPosition, nimber, result, depth);
    return result;
  }

  private boolean needsToComputeAll(int depth) {
    return depth < save || (computeOptimalMove && depth == 0);
  }

  private int computeNimberOfSeveralLands(List<String> list, int depth) {
    int result = 0;
    for (String land : list) {
      int nimber = computeNimber(land, depth);
      result = result ^ nimber;
    }
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
