package uk.ac.ed.inf.sprouts;

import java.util.Scanner;

public class Sprouts {

  enum GamePlayingType {
    HUMAN_VS_HUMAN, HUMAN_VS_PC, PC_VS_PC
  }

  private static final GamePlayingType GAME_TYPE = GamePlayingType.PC_VS_PC;
  private static final String AUTO_GAME_TYPE = "4+";
  private static final Long SEED = null;

  private static RandomMoveGenerator moveGenerator;

  public static void main(String[] args) {
    init();
    switch (GAME_TYPE) {
      case PC_VS_PC:
        runAutoGame();
        break;
      default:
        runGameVsPc(GAME_TYPE);
        break;
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
    System.out.println("New position is: \n" + game.getPosition());
  }
}
