package uk.ac.ed.inf.sprouts.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

public class SavedPositionsHandler {

  private static String FILENAME = "savedPositions.txt";
  private static HashMap<String, Boolean> positions;

  public static void savePositionsToFile(HashMap<String, Boolean> savedPositions) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(FILENAME));
      for (Entry<String, Boolean> entry : savedPositions.entrySet()) {
        out.write(entry.getKey() + '\t' + (entry.getValue() ? '1' : '0') + '\n');
      }
      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static HashMap<String, Boolean> getSavedPositions() {
    if (positions != null) {
      return positions;
    }
    positions = new HashMap<String, Boolean>();
    BufferedReader in;
    try {
      in = new BufferedReader(new FileReader(FILENAME));
      String line = in.readLine();
      while (line != null) {
        String s[] = line.split("\t");
        //Output.debug(s[0] + " - " + s[1]);
        //Output.debug("" + s[1].equals("1"));
        positions.put(s[0], (s[1].equals("1")));
        line = in.readLine();
      }
      in.close();
      return positions;
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
