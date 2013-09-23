package uk.ac.ed.inf.sprouts;

import java.util.Scanner;

public class Sprouts {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter game type:\n");
    //String gameTypeString = scanner.nextLine();
    String gameTypeString = "6+";
    Game game = Game.fromString(gameTypeString);
    System.out.println("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
    System.out.println("Position is: \n" + game.getPosition());
    System.out.println("Enter your move:");
    //String moveString = scanner.nextLine();

    // 1
//    makeMove("2(7)3", game);
//    makeMove("4(8)7", game);
//    makeMove("1(9)5", game);
//    makeMove("5(10)6", game);
//    makeMove("8!(11)!9", game);

    makeMove("2(7)2[3,4]", game);
    makeMove("1(8)5", game);
    makeMove("6(9)8", game);
    makeMove("5(10)6![2]", game);
  }

  private static void makeMove(String moveString, Game game) {
    Move move = Move.fromString(moveString);
    System.out.println("Your move is: " + move);
    game.getPosition().makeMove(move);
    System.out.println("New position is: \n" + game.getPosition());
  }
}
