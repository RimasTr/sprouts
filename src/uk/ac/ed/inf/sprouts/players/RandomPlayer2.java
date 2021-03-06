package uk.ac.ed.inf.sprouts.players;

import uk.ac.ed.inf.sprouts.bruteforcers.ImprovedLandsMoveBruteforcer;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;

public class RandomPlayer2 implements Player {

  @Override
  public Move getMove(Game game) {
    return new ImprovedLandsMoveBruteforcer(game).getRandomMove();
  }
}
