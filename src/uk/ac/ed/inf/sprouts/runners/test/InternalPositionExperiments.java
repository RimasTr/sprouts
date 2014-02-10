package uk.ac.ed.inf.sprouts.runners.test;

import uk.ac.ed.inf.sprouts.internal.InternalPosition;
import uk.ac.ed.inf.sprouts.internal.InternalPositionWithLands;
import uk.ac.ed.inf.sprouts.utils.Output;

public class InternalPositionExperiments {

  public static void main(String[] args) {
//    printPosition("0.0.0.}!");
//    printPosition("0.1d1d.}!");
//    printPosition("BE.}0.1EB.}!");
//    printPosition("B.}CF.}0.CFB.}!");
//    printPosition("B.}0.B.}G.}G.}!");
//    printPosition("B.}G.}G.}AH.}B.AH.}!");
//    printPosition("B.}G.}G.}B..}I.}I.}!");
//    printPosition("a.b.c.}!");
//    printPosition("a.bdcD.}!");
    printPosition("0.AB.}0.C.}1C.}AB.}!");
//    Scanner scanner = new Scanner(System.in);
//    System.out.print("Enter position:\n");
//    String positionString = scanner.nextLine();
//    InternalPosition position = InternalPosition.fromStringUnoptimized(positionString);
//    Output.debug("Position is: \n" + position);
//    position.optimize();
//    Output.debug("New position is: " + position);
//    position.optimize();
//    Output.debug("New position is: " + position);
//    Output.debug("----");
  }

  public static void printPosition(String string) {
    Output.debug("Position string: " + string);
    InternalPosition position = InternalPosition.fromString(string);
    Output.debug("Position is:     " + position);
    position.optimize();
    Output.debug("New position is: " + position);
    Output.debug("Split position: " + new InternalPositionWithLands(position));
    Output.debug("----");
  }
}
