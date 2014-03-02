package uk.ac.ed.inf.sprouts.runners.utils;

import java.util.List;

import uk.ac.ed.inf.sprouts.external.Game;
import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.utils.Output;

public class GameRunner {

  private final Game game;
  private final Player players[];
  private final boolean output[] = {false, false};

  public GameRunner(String gameType, Player player1, Player player2) {
    this.game = Game.fromString(gameType);
    this.players = new Player[] {player1, player2};
    Output.debug("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
  }

  public GameRunner(String gameType, Player player1, Player player2, boolean outputP1,
      boolean outputP2) {
    this(gameType, player1, player2);
    output[0] = outputP1;
    output[1] = outputP2;
  }

  public void makeMoves(List<String> moves) {
    for (String move : moves) {
      makeMove(Move.fromString(move), game);
      //game.makeMove(move);
    }
  }

  public int getSurvivors() {
    return 3 * game.getInitialSprouts() - game.getNumberOfMoves();
  }

  public int run() {
    // Output.debug("Game type is: " + game.getInitialSprouts() + " " + game.getGameType());
    // Output.debug("Position is: \n" + game.getPosition());

    int current = 0;
    while (!game.isOver()) {
      Move move = players[current].getMove(game);
      if (output[current]) {
        Output.move(move);
      }
      makeMove(move, game);
      current = (current + 1) % 2;
    }
    int won = (current + 1) % 2; // 0 or 1
    Output.debug("Player " + (won + 1) + " won");
    Output.won(won + 1);
    return won;
  }

  private void makeMove(Move move, Game game) {
    Output.debug("The move is: " + move.toNotation());
    game.makeMove(move);
    Output.debug("New position is: \n" + game.getPosition());
    // Output.debug("Internal position is: \n"
    // + InternalPosition.fromExternal(game.getPosition()));
    Output.debug();
  }
}
