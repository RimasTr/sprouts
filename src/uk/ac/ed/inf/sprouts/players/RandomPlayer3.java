package uk.ac.ed.inf.sprouts.players;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.external.RandomMoveGenerator;

public class RandomPlayer3 implements Player {

  @Override
  public Move getMove(Game game) {
    return new RandomMoveGenerator().generateRandomMove(game.getPosition());
  }
}
