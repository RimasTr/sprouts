package uk.ac.ed.inf.sprouts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.InternalConstants;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;

public class MoveBruteforcer {

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
//  private HashMap<String, Boolean> alreadyComputedPositions;

  public MoveBruteforcer(Game game) {
    this.game = game;
  }

  public void compute() {
//    alreadyComputedPositions = new HashMap<String, Boolean>();
    possiblePositions = getPossiblePositions(game);
    System.out.println("Possible moves: " + possiblePositions.size());
    for (String position : possiblePositions.keySet()) {
      System.out.println("Checking " + possiblePositions.get(position).getMove().toNotation());
      if (!isWin(possiblePositions.get(position).getGame(), position)) {
        winningMove = possiblePositions.get(position).getMove();
        return;
      }
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
    Random random = new Random();
    List<String> keys = new ArrayList<String>(possiblePositions.keySet());
    String randomKey = keys.get(random.nextInt(keys.size()));
    return possiblePositions.get(randomKey).getMove();
  }

  private boolean isWin(Game game, String currentPosition) {
    if (currentPosition.equals(InternalConstants.END_OF_POSITION_CHAR)) {
      return false;
    }
//     if (alreadyComputedPositions.containsKey(currentPosition)) {
//     return alreadyComputedPositions.get(currentPosition);
//     }

    HashMap<String, MoveResult> possiblePositions = getPossiblePositions(game);
    for (String position : possiblePositions.keySet()) {
      if (!isWin(possiblePositions.get(position).getGame(), position)) {
        return true;
      }
    }
    return false;
  }

  private HashMap<String, MoveResult> getPossiblePositions(Game game) {
    AllMovesGenerator allMovesGenerator = new AllMovesGenerator();
    Set<Move> moves = allMovesGenerator.generateAllMoves(game.getPosition());
    HashMap<String, MoveResult> possiblePositions = new HashMap<String, MoveResult>();
    // System.out.println("All moves (" + moves.size() + ")");
    for (Move move : moves) {
      Game clone = game.deepClone();
      clone.getPosition().makeMove(move);
      possiblePositions.put(InternalPosition.fromExternal(clone.getPosition()).toString(),
          new MoveResult(move, clone));
    }
    // System.out.println("All possible positions (" + possiblePositions.keySet().size() + ")");
    // for (String positionString : possiblePositions.keySet()) {
    // System.out.println(positionString + "\t\t"
    // + possiblePositions.get(positionString).getMove().toNotation());
    // }
    return possiblePositions;
  }
}
