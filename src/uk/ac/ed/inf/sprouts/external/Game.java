package uk.ac.ed.inf.sprouts.external;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.ac.ed.inf.sprouts.utils.Output;

public class Game implements Serializable {

  private static final long serialVersionUID = 8360779598976895710L;

  private final int initialSprouts;
  private final GameType gameType;
  private Position position;
  private List<Move> movesHistory;

  Game(int initialSprouts, GameType gameType) {
    this.initialSprouts = initialSprouts;
    this.gameType = gameType;
    this.position = new Position(initialSprouts);
    this.movesHistory = new ArrayList<Move>();
  }

  public static Game fromString(String gameTypeString) {
    Pattern pattern = Pattern.compile("(\\d+)([+-])");
    Matcher matcher = pattern.matcher(gameTypeString);

    if (matcher.matches()) {
      // Output.debug("Matches: ");
      // for (int i = 1; i <= matcher.groupCount(); i++)
      // Output.debug(i + ": " + matcher.group(i));
      Integer initialSprouts = Integer.parseInt(matcher.group(1));
      GameType gameType = GameType.fromSymbol(matcher.group(2));
      return new Game(initialSprouts, gameType);
    } else {
      Output.debug("Fail");
      return null;
    }
  }

  public int getInitialSprouts() {
    return initialSprouts;
  }

  public GameType getGameType() {
    return gameType;
  }

  public Position getPosition() {
    return position;
  }

  public List<Move> getMovesHistory() {
    return movesHistory;
  }

  public int getNumberOfMoves() {
    return movesHistory.size();
  }

  public boolean isOver() {
    return position.isLost();
  }

  public void makeMove(Move move) {
    movesHistory.add(move);
    position.makeMove(move);
  }

  public Game deepClone() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(this);

      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      return (Game) ois.readObject();
    } catch (IOException e) {
      Output.debug("" + e);
    } catch (ClassNotFoundException e) {
      Output.debug("" + e);
    }
    return null;
  }

  public void makeMove(String string) {
    makeMove(Move.fromString(string));
  }
}
