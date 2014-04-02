package uk.ac.ed.inf.sprouts.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.external.Region;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;
import uk.ac.ed.inf.sprouts.utils.Output;

public class SurvivorsStrategy {

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
  private Move optimalMove;
  private HashMap<String, MoveResult> possiblePositions;
  private boolean needsMoreSurvivors;

  public SurvivorsStrategy(Game game) {
    this.game = game;
    this.needsMoreSurvivors = computeSurvivorsGoal();
  }

  private boolean computeSurvivorsGoal() {
    int wantedNumberOfSurvivors = getWantedNumberOfSurvivors();
    if (countSurvivors(game) > wantedNumberOfSurvivors) {
      return false;
    }
    return true;
  }

  private int getWantedNumberOfSurvivors() {
    // double finalSurvivorsEstimate = game.getInitialSprouts() * 2.0 / 3.0;
    double finalSurvivorsEstimate = getFinalSurvivorsEstimate();
    double numberOfMovesEstimate = game.getInitialSprouts() * 3.0 - finalSurvivorsEstimate;
    double wantedNumberOfSurvivors;
    if (needsEvenNumberOfSurvivors(numberOfMovesEstimate, finalSurvivorsEstimate)) {
      wantedNumberOfSurvivors = roundToNearestEven(finalSurvivorsEstimate);
    } else {
      wantedNumberOfSurvivors = roundToNearestOdd(finalSurvivorsEstimate);
    }
    double wantedSurvivorsEstimate =
        wantedNumberOfSurvivors * (game.getNumberOfMoves() / numberOfMovesEstimate);
    return (int) Math.round(wantedSurvivorsEstimate);
  }

  private double getFinalSurvivorsEstimate() {
    double estimate = 0;
    HashMap<Integer, Integer> lives = game.getPosition().getLives();
    for (Region region : game.getPosition().getRegions()) {
      double regionEstimate = (2.0 / 9.0) * region.getLives(lives);
      estimate += (regionEstimate > 1.0 ? regionEstimate : 1.0);
    }
    return estimate;
  }

  private boolean needsEvenNumberOfSurvivors(double numberOfMovesEstimate,
      double finalSurvivorsEstimate) {
    if (game.getNumberOfMoves() % 2 == Math.round(numberOfMovesEstimate) % 2) {
      // estimated outcome is fine for the player
      return Math.round(finalSurvivorsEstimate) % 2 == 0;
    } else {
      return !(Math.round(finalSurvivorsEstimate) % 2 == 0);
    }
  }

  private int roundToNearestEven(double number) {
    return (int) (Math.round(number / 2.0) * 2.0);
  }

  private double roundToNearestOdd(double number) {
    return roundToNearestEven(number + 1) - 1;
  }

  private int countSurvivors(Game game) {
    String position = InternalPosition.fromExternal(game.getPosition()).toString();
    // TODO: make sure it's right
    return 3 * game.getInitialSprouts() - game.getNumberOfMoves() - getLives(position);
  }

  public void compute() {
    possiblePositions = getPossiblePositions(game);
    double maxAverage = -99999999;
    Output.debug("Debug", "Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      Output.debug("Debug", "Checking " + possiblePositions.get(position).getMove().toNotation());
      HashMap<String, MoveResult> possibleChildPositions =
          getPossiblePositions(possiblePositions.get(position).getGame());
      double average;
      if (!possibleChildPositions.isEmpty()) {
         average = (needsMoreSurvivors ? 1.0 : -1.0) * computeAverage(possibleChildPositions);
      } else {
        average = 2; // TODO: some other value? shouldn't happen anyway
      }
      if (average > maxAverage) {
        optimalMove = possiblePositions.get(position).getMove();
      }
      // return;
      // if (!isOptimal(possiblePositions.get(position).getGame())) {
      // optimalMove = possiblePositions.get(position).getMove();
      // return;
      // }
    }
  }

  private double computeAverage(HashMap<String, MoveResult> possibleChildPositions) {
    int sum = 0;
    for (MoveResult result : possibleChildPositions.values()) {
      sum += countSurvivors(result.getGame());
    }
    return sum * 1.0 / possibleChildPositions.values().size();
  }

  public boolean hasOptimalMove() {
    return optimalMove != null;
  }

  public Move getOptimalMove() {
    return optimalMove;
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

  private HashMap<String, MoveResult> getPossiblePositions(Game game) {
    return getPossiblePositions(game, false);
  }

  private HashMap<String, MoveResult> getPossiblePositions(Game game, boolean debug) {
    AllMovesGenerator allMovesGenerator = new AllMovesGenerator();
    Set<Move> moves = allMovesGenerator.generateAllMoves(game.getPosition());
    HashMap<String, MoveResult> possiblePositions = new HashMap<String, MoveResult>();
    if (debug) {
      Output.debug("Debug", "All moves (" + moves.size() + ")");
    }
    for (Move move : moves) {
      Game clone = game.deepClone();
      clone.makeMove(move);
      InternalPosition internalPosition = InternalPosition.fromExternal(clone.getPosition());
      String internalPositionString = internalPosition.toString();
      if (debug) {
        Output.debug("Debug", move.toNotation() + " -> " + internalPositionString);
        Output.debug("Debug", "" + clone.getPosition());
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

  public int getLives(String position) {
    int lives = 0;
    for (char c : position.toCharArray()) {
      lives += getDoubleLives(c);
    }
    return lives / 2;
  }

  public int getDoubleLives(char c) {
    switch (c) {
      case '0':
        return 3 * 2;
      case '1':
        return 2 * 2;
      case '2':
        return 1 * 2;
      case '3':
        return 0 * 2;
      default:
        // All letters
        return 1; // half
    }
  }
}
