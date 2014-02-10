package uk.ac.ed.inf.sprouts.players;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;

public interface Player {

  public Move getMove(Game game);
}
