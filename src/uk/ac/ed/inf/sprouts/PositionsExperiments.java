package uk.ac.ed.inf.sprouts;

import java.util.Scanner;

import uk.ac.ed.inf.sprouts.internal.InternalPosition;

public class PositionsExperiments {

  public static void main(String[] args) {
    printPosition("0.0.0.}!");
    printPosition("0.1d1d.}!");
    printPosition("BE.}0.1EB.}!");
    printPosition("B.}CF.}0.CFB.}!");
    printPosition("B.}0.B.}G.}G.}!");
    printPosition("B.}G.}G.}AH.}B.AH.}!");
    printPosition("B.}G.}G.}B..}I.}I.}!");
//    printPosition("a.b.c.}!");
//    printPosition("a.bdcD.}!");

    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter position:\n");
    String positionString = scanner.nextLine();
    InternalPosition position = InternalPosition.fromString(positionString);
    System.out.println("Position is: \n" + position);
    position.optimize();
    System.out.println("New position is: " + position);
    System.out.println("----");
  }

  public static void printPosition(String string) {
    System.out.println("Position string: " + string);
    InternalPosition position = InternalPosition.fromString(string);
    System.out.println("Position is:     " + position);
    position.optimize();
    System.out.println("New position is: " + position);
    System.out.println("----");
  }
}
