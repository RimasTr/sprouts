package uk.ac.ed.inf.sprouts.runners;

import java.util.List;
import java.util.Map;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import uk.ac.ed.inf.sprouts.external.Move;
import uk.ac.ed.inf.sprouts.players.Player;
import uk.ac.ed.inf.sprouts.players.RandomPlayer;
import uk.ac.ed.inf.sprouts.players.SmartPlayer;
import uk.ac.ed.inf.sprouts.runners.utils.OneMoveRunner;
import uk.ac.ed.inf.sprouts.utils.Output;
import uk.ac.ed.inf.sprouts.utils.ServerClient;

/**
 * The main runner of the application. Supports three game types: new game (-n), join game (-j) and
 * tracking of the player (-t).
 * 
 * @author Rimas
 */
public class NextMovePlayer {

  @Option(name = "-p", usage = "Set player type.\nE.g. '-p random', '-p smart'.", metaVar = "<playerType>")
  private String playerString = "random";

  @Option(name = "-u", usage = "Set username for the player.\nE.g. '-u MyAwesomeBot'.", metaVar = "<username>")
  private String username;

  @Option(name = "-j", usage = "Join the given game.\nE.g. '-j 5Pr0u75r0K'.", metaVar = "<gameId>")
  private String gameId;

  @Option(name = "-n", usage = "Start new game of the given type.\nE.g. '-n 4+F' (F means you go First, S means Second).", metaVar = "<gameType>")
  private String gameType;

  @Option(name = "-t", usage = "Track user with the given username.\nE.g. '-t BestSproutsPlayerEver'.", metaVar = "<username>")
  private String trackedUser;

  @Option(name = "-r", usage = "Set refresh interval in seconds.\nE.g. '-r 1'.", metaVar = "<seconds>")
  private long refreshInterval = 1;

  @Option(name = "-k", usage = "Keep playing after the game is over.\nE.g. '-k'.")
  private boolean keepPlaying = false;

  @Option(name = "-v", usage = "Enable verbose mode, i.e. more output.\nE.g. '-v'.")
  private boolean verbose = false;

  private Player player;
  private CmdLineParser parser;

  public void doMain(String[] args) {

    try {
      parseArguments(args);
    } catch (Exception e) {
      return;
    }

    ServerClient client = new ServerClient(username);

    do {
      if (trackedUser != null) {
        // Tracking a user
        List<String> games = client.getGames(trackedUser);
        if (games.size() > 0) {
          gameId = games.get(0);
          gameType = client.joinGame(gameId);
          Output.out();
          Output.debug("GameInfo", "Found a game. Game id: " + gameId);
        } else {
          sleep();
          continue;
        }
      } else if (gameType != null) {
        // Start a new game
        gameId = client.newGame(gameType);
        Output.out();
        Output.debug("GameInfo", "Game started. Game id: " + gameId);
      } else if (gameId != null) {
        // Join the game
        gameType = client.joinGame(gameId);
        Output.out();
        Output.debug("GameInfo", "Game joined. Game type: " + gameType);
      }
      do {
        // Get moves
        Map<String, Object> movesResponse = client.getMoves(gameId);
        if ((Boolean) movesResponse.get("isOver")) {
          Output.out();
          Output.debug("GameInfo", "The game is over. We won! :)");
          break;
        }
        if ((Boolean) movesResponse.get("yourTurn")) {
          @SuppressWarnings("unchecked")
          List<String> moves = (List<String>) movesResponse.get("moves");
          if (!moves.isEmpty()) {
            Output.out();
            Output.debug("GameInfo", "Got move: " + moves.get(moves.size() - 1));
          }
          OneMoveRunner runner = new OneMoveRunner(gameType, player);
          runner.makeMoves(moves);
          // Compute move
          Move move = runner.getMove();
          if (move != null) {
            // Send move
            client.makeMove(gameId, move.toNotation());
            Output.out();
            Output.debug("GameInfo", "Making move: " + move);
          } else {
            // No more moves, game is over
            Output.out();
            Output.debug("GameInfo", "The game is over. We lost...");
            client.won(gameId, false, false);
            break;
          }
        }
        sleep();
      } while (trackedUser == null); // Only if we're not tracking a user.
    } while (keepPlaying);
  }

  private void parseArguments(String[] args) throws Exception {
    parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
      checkPlayModes();
      checkRefresh();
      player = getPlayer();
      if (verbose) {
        Output.enableDebug();
      }
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      System.err.println("java " + this.getClass().getSimpleName() + " [options...]");
      parser.printUsage(System.err);
      System.err.println();
      throw new Exception(e);
    }
  }

  private void checkPlayModes() throws CmdLineException {
    int modes = 0;
    if (gameType != null) modes++;
    if (gameId != null) modes++;
    if (trackedUser != null) modes++;
    if (modes < 1) {
      throw new CmdLineException(parser, "One of -j, -n or -t have to be set.");
    }
    if (modes > 1) {
      throw new CmdLineException(parser, "Only one of -j, -n and -t can be set.");
    }
    if (keepPlaying && gameId != null) {
      throw new CmdLineException(parser, "Cannot use -k with -j.");
    }
  }

  private void checkRefresh() throws CmdLineException {
    if (refreshInterval < 1) {
      throw new CmdLineException(parser, "Refresh interval (-r) has to be bigger than 0.");
    }
  }

  private Player getPlayer() throws CmdLineException {
    if (playerString.equals("random")) {
      return new RandomPlayer();
    }
    if (playerString.equals("smart")) {
      return new SmartPlayer();
    }
    throw new CmdLineException(parser, "Wrong player type (-p). Try 'random' or 'smart'.");
  }

  private void sleep() {
    try {
      Thread.sleep(refreshInterval * 1000);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
    Output.outSingle(".");
  }

  public static void main(String[] args) {
    new NextMovePlayer().doMain(args);
  }
}
