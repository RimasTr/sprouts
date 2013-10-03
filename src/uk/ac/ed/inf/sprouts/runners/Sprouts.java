package uk.ac.ed.inf.sprouts.runners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import uk.ac.ed.inf.sprouts.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.RandomMoveGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;

public class Sprouts {

  enum GamePlayingType {
    HUMAN_VS_HUMAN, HUMAN_VS_PC, PC_VS_PC, ALL_MOVES_RANDOM, ALL_MOVES
  }

  public static final boolean LETTERS_MODE = true;
  private static final GamePlayingType GAME_TYPE = GamePlayingType.ALL_MOVES;
  private static final String AUTO_GAME_TYPE = "4+";
  private static final Long SEED = null;
  private static final int NUMBER_OF_MOVES_TO_GENERATE = 1000;

  private static RandomMoveGenerator moveGenerator;

  public static void main(String[] args) {
    init();
    switch (GAME_TYPE) {
      case PC_VS_PC:
        runAutoGame();
        break;
      case ALL_MOVES_RANDOM:
        runAllMovesByRandom();
        break;
      case ALL_MOVES:
        runAllMoves();
        break;
      default:
        runGameVsPc(GAME_TYPE);
        break;
    }
  }

  private static void runAllMovesByRandom() {
    String gameTypeString = AUTO_GAME_TYPE;
    Game game = Game.fromString(gameTypeString);
    System.out.println("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
    System.out.println("Position is: \n" + game.getPosition());

    // makeMove("1(6)2", game);
    // makeMove("3(7)4", game);

    HashSet<Move> movesSet = new HashSet<Move>();
    HashMap<String, Move> possiblePositions = new HashMap<String, Move>();
    for (int i = 0; i < NUMBER_OF_MOVES_TO_GENERATE; i++) {
      Game clone = game.deepClone();
      Move move = moveGenerator.generateRandomMove(clone.getPosition());
      clone.getPosition().makeMove(move);
      movesSet.add(move);
      possiblePositions.put(InternalPosition.fromExternal(clone.getPosition()).toString(), move);
    }
    System.out.println("All moves (" + movesSet.size() + ")");
    System.out.println("All possible positions (" + possiblePositions.keySet().size() + ")");
    for (String positionString : possiblePositions.keySet()) {
      System.out.println(possiblePositions.get(positionString).toNotation() + " : "
          + positionString);
    }
  }

  private static void runAllMoves() {
    String gameTypeString = AUTO_GAME_TYPE;
    Game game = Game.fromString(gameTypeString);
    System.out.println("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
    System.out.println("Position is: \n" + game.getPosition());

    //makeMove("1(9)2", game);

    AllMovesGenerator allMovesGenerator = new AllMovesGenerator();
    Set<Move> moves = allMovesGenerator.generateAllMoves(game.getPosition());
    HashMap<String, Move> possiblePositions = new HashMap<String, Move>();
    printMovesInOrder(moves);
    System.out.println("All moves (" + moves.size() + ")");

    for (Move move : moves) {
      Game clone = game.deepClone();
      clone.getPosition().makeMove(move);
      possiblePositions.put(InternalPosition.fromExternal(clone.getPosition()).toString(), move);
    }
    System.out.println("All possible positions (" + possiblePositions.keySet().size() + ")");
    for (String positionString : possiblePositions.keySet()) {
      System.out.println(positionString + "\t\t"
          + possiblePositions.get(positionString).toNotation());
    }
  }

  private static void runGameVsPc(GamePlayingType gamePlayingType) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter game type:\n");
    String gameTypeString = scanner.nextLine();
    Game game = Game.fromString(gameTypeString);
    System.out.println("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
    System.out.println("Position is: \n" + game.getPosition());

    // String seedString = scanner.nextLine();
    // Long.parseLong(seedString)

    if (gamePlayingType == GamePlayingType.HUMAN_VS_PC) {
      System.out.println("Do you want to start first (Y/N):");
      String startString = scanner.nextLine();
      boolean computerStarts = startString.toLowerCase().equals("n");

      if (computerStarts) {
        makeMove(moveGenerator.generateRandomMove(game.getPosition()), game);
      }
    }

    while (!game.isOver()) {
      System.out.println("Enter your move:");
      String moveString = scanner.nextLine();
      makeMove(moveString, game);
      if (game.isOver()) {
        System.out.println("Human wins");
        break;
      }
      if (gamePlayingType == GamePlayingType.HUMAN_VS_PC) {
        makeMove(moveGenerator.generateRandomMove(game.getPosition()), game);
        if (game.isOver()) {
          System.out.println("Computer wins");
        }
      }
    }
  }

  private static void runAutoGame() {
    String gameTypeString = AUTO_GAME_TYPE;
    Game game = Game.fromString(gameTypeString);
    System.out.println("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
    System.out.println("Position is: \n" + game.getPosition());

    while (!game.isOver()) {
      makeMove(moveGenerator.generateRandomMove(game.getPosition()), game);
    }
  }

  public static void init() {
    if (SEED != null) {
      moveGenerator = new RandomMoveGenerator(SEED);
    } else {
      moveGenerator = new RandomMoveGenerator();
    }
  }

  private static void makeMove(String moveString, Game game) {
    Move move = Move.fromString(moveString);
    makeMove(move, game);
  }

  private static void makeMove(Move move, Game game) {
    System.out.println("The move is: " + move.toNotation());
    game.getPosition().makeMove(move);
    // System.out.println("New position is: \n" + game.getPosition());
    System.out.println("Internal position is: \n"
        + InternalPosition.fromExternal(game.getPosition()));
    System.out.println();
  }

  private static void printMovesInOrder(Set<Move> movesSet) {
    List<Move> moves = new ArrayList<Move>(movesSet);
    Collections.sort(moves);
    for (Move move : moves) {
      System.out.println(move.toNotation());
    }
  }
}
