package uk.ac.ed.inf.sprouts.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;

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
    double finalSurvivorsEstimate = game.getInitialSprouts() * 2.0 / 3.0;
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
    System.out.println("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      System.out.println("Checking " + possiblePositions.get(position).getMove().toNotation());
      if (!isOptimal(possiblePositions.get(position).getGame())) {
        optimalMove = possiblePositions.get(position).getMove();
        return;
      }
    }
  }

  private boolean isOptimal(Game gameAfterMove) {
    // TODO: make sure it works
    return (needsMoreSurvivors == (countSurvivors(gameAfterMove) > countSurvivors(game)));
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
