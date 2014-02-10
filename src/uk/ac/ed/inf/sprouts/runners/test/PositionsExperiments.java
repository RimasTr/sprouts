package uk.ac.ed.inf.sprouts.runners.test;

import java.util.Set;

import uk.ac.ed.inf.sprouts.internal.ChildrenGenerator;
import uk.ac.ed.inf.sprouts.internal.InternalPosition;
import uk.ac.ed.inf.sprouts.utils.Output;

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
    printPosition("ABC.}BC.}DE.FG.}DE.}FAG.}!");
  }

  public static void printPosition(String string) {
    Output.debug("Position string: " + string);
    InternalPosition position = InternalPosition.fromString(string);
    Output.debug("Position is:     " + position);
    ChildrenGenerator childrenGenerator = new ChildrenGenerator(position);
    long time = System.currentTimeMillis();
    Set<InternalPosition> children = childrenGenerator.generateAllChildren();
    Output.debug("" + (System.currentTimeMillis() - time) / 1000.0);
    Output.debug("" + children);
    Output.debug("" + children.size());
    Output.debug("----");
  }
}
