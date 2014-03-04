package uk.ac.ed.inf.sprouts.players;

import java.util.Random;
import java.util.Set;

import uk.ac.ed.inf.sprouts.external.AllMovesGenerator;
import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;

public class RandomPlayer implements Player {

  @Override
  public Move getMove(Game game) {
    Set<Move> moves = new AllMovesGenerator().generateAllMoves(game.getPosition());
    int r = new Random().nextInt(moves.size());
    return (Move) moves.toArray()[r];
  }
}
