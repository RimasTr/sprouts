package uk.ac.ed.inf.sprouts.utils;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Joiner;

public class ServerClient {

  private String username;

  private static final String SERVER_URL = "http://sprouts.laisva.lt/api.php";

  public ServerClient() {
  }

  public ServerClient(String username) {
    this.username = username;
  }

  public void changeUsername(String newUsername) {
    this.username = newUsername;
  }

  public String newGame(String gameType) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "newGame");
    parameters.put("gameType", gameType);
    Map<String, Object> response = sendPost(parameters);
    changeUsername((String) response.get("playerId"));
    return (String) response.get("gameId");
  }

  public String joinGame(String gameId) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "joinGame");
    parameters.put("gameId", gameId);
    Map<String, Object> response = sendPost(parameters);
    changeUsername((String) response.get("playerId"));
    return (String) response.get("gameType");
  }

  public Map<String, Object> makeMove(String gameId, String move) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "makeMove");
    parameters.put("gameId", gameId);
    parameters.put("move", move);
    return sendPost(parameters);
  }

  public Map<String, Object> getMoves(String gameId) {
    return getMoves(gameId, 0);
  }

  public Map<String, Object> getMoves(String gameId, int lastMove) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "getMoves");
    parameters.put("gameId", gameId);
    parameters.put("lastMove", "" + lastMove);
    return sendPost(parameters);
  }

  public Map<String, Object> claimGame(String gameId, String targetUser) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "claimGame");
    parameters.put("gameId", gameId);
    parameters.put("targetUser", targetUser);
    return sendPost(parameters);
  }

  public Map<String, Object> won(String gameId, boolean me, boolean resign) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "won");
    parameters.put("gameId", gameId);
    parameters.put("me", me ? "y" : "n");
    parameters.put("resign", resign ? "y" : "n");
    return sendPost(parameters);
  }

  public List<String> getGames(String targetUser) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("action", "getGames");
    parameters.put("targetUser", targetUser);
    Map<String, Object> response = sendPost(parameters);
    @SuppressWarnings("unchecked")
    List<String> list = (List<String>) response.get("games");
    return list;
  }

  private Map<String, Object> sendPost(Map<String, String> parameters) {

    try {

      if (username != null) {
        parameters.put("username", username);
      }

      encodeParameters(parameters);
      String parametersString = Joiner.on("&").withKeyValueSeparator("=").join(parameters);

      URL serverUrl = new URL(SERVER_URL);
      HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();

      // Send POST request
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      DataOutputStream outStream = new DataOutputStream(con.getOutputStream());
      outStream.writeBytes(parametersString);
      outStream.flush();
      outStream.close();

      // Get response
      int responseCode = con.getResponseCode(); // TODO: handle other response codes?
      Output.debug("ServerClient", "Sending 'POST' request to URL : " + serverUrl);
      Output.debug("ServerClient", "Post parameters : " + parametersString);
      Output.debug("ServerClient", "Response Code : " + responseCode);

      JSONParser parser = new JSONParser();

      try {
        @SuppressWarnings("unchecked")
        Map<String, Object> map =
            (JSONObject) parser.parse(new InputStreamReader(con.getInputStream()));
        Output.debug("ServerClient", "Response : " + map.toString());
        return map;
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ParseException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      // TODO: better exceptions handling
      e.printStackTrace();
    }

    return null;
  }

  private void encodeParameters(Map<?, String> map) throws UnsupportedEncodingException {
    for (Map.Entry<?, String> e : map.entrySet()) {
      String val = e.getValue();
      if (val != null) e.setValue(URLEncoder.encode(val, "UTF-8"));
    }
  }

  private String getUsername() {
    // For testing
    return username;
  }

  public static void main(String[] args) throws Exception {
    ServerClient p1 = new ServerClient("test");
    ServerClient p2 = new ServerClient();
    String gameId = p1.newGame("4+S");
    p2.joinGame(gameId);
    p2.makeMove(gameId, "2(3)4");
    String tempUsername = p2.getUsername();
    p2.changeUsername("test3");
    p2.claimGame(gameId, tempUsername);
    p1.makeMove(gameId, "3(4)5");
    p2.getMoves(gameId);
    p2.makeMove(gameId, "4(5)6");
    p1.getMoves(gameId);
    p2.won(gameId, false, true);
    p2.getMoves(gameId);
  }

}
