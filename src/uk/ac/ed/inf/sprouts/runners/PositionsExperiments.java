package uk.ac.ed.inf.sprouts.runners;

import java.util.Set;

import uk.ac.ed.inf.sprouts.internal.ChildrenGenerator;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;

public class PositionsExperiments {

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
//    printPosition("bd.ae.}0.0.0.}cgf.}1cfdbdfg.}aeh.}ahe.}!");
    //printPosition("al.}al.bnmcnm.}d.cofpgqfocm.}e.hrisjsiuktkuir.fqgp.}kt.}!");
    //printPosition("0.0.0.0.0.0.0.0.0.}!");
    printPosition("1.}!");
  }

  public static void printPosition(String string) {
    System.out.println("Position string: " + string);
    InternalPosition position = InternalPosition.fromString(string);
    System.out.println("Position is:     " + position);
    ChildrenGenerator childrenGenerator = new ChildrenGenerator(position);
    long time = System.currentTimeMillis();
    Set<InternalPosition> children = childrenGenerator.generateAllChildren();
    System.out.println((System.currentTimeMillis() - time) / 1000.0);
    System.out.println(children);
    System.out.println(children.size());
    System.out.println("----");
  }
}
