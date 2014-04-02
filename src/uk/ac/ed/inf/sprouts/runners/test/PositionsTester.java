package uk.ac.ed.inf.sprouts.runners.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class PositionsTester {

  private static final String POSITIONS_FILE = "savedPositions.txt.gz";

  public static void main(String[] args) throws IOException {
    InputStream is = PositionsTester.class.getClassLoader().getResourceAsStream(POSITIONS_FILE);
    System.out.println("Is: " + is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
    String line;
    if ((line = reader.readLine()) != null) System.out.println("Read: " + line);
  }
}
