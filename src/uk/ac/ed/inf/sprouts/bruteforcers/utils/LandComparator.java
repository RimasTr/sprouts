package uk.ac.ed.inf.sprouts.bruteforcers.utils;

import java.util.Comparator;

public class LandComparator implements Comparator<String> {

  @Override
  public int compare(String land1, String land2) {
    return livesOfLand(land1) > livesOfLand(land2) ? -1 : 1;
  }

  private int livesOfLand(String land) {
    int sum = 0;
    for (char c : land.toCharArray()) {
      sum += livesOfChar(c);
    }
    return sum;
  }

  private int livesOfChar(char c) {
    switch (c) {
      case '0':
        return 3;
      case '1':
        return 2;
      case '2':
        return 1;
    }
    if (Character.isLetter(c)) {
      return 1;
    }
    return 0;
  }
}
