package uk.ac.ed.inf.sprouts.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import uk.ac.ed.inf.sprouts.runners.test.PositionsTester;

public class SavedPositionsHandler {

  private static String FILENAME_LOAD = "savedPositions.txt.gz";
  private static String FILENAME_SAVE = "savedPositionsNew.txt";
  private static HashMap<String, Boolean> positions;

  public static void savePositionsToFile(HashMap<String, Boolean> savedPositions) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(FILENAME_SAVE));
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
      InputStream is = PositionsTester.class.getClassLoader().getResourceAsStream(FILENAME_LOAD);
      in = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
      String line = in.readLine();
      while (line != null) {
        String s[] = line.split("\t");
        // Output.debug(s[0] + " - " + s[1]);
        // Output.debug("" + s[1].equals("1"));
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
