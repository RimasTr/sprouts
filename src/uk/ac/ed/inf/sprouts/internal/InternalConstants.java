package uk.ac.ed.inf.sprouts.internal;

import com.google.common.collect.ImmutableList;

public class InternalConstants {

  public static final char END_OF_BOUNDARY_CHAR = '.';
  public static final char END_OF_REGION_CHAR = '}';
  public static final char END_OF_POSITION_CHAR = '!';

  public static final String END_OF_BOUNDARY_REGEX = "\\.";
  public static final String END_OF_REGION_REGEX = "}";
  public static final String END_OF_POSITION_REGEX = "!";

  public static final char ZERO_CHAR = '0';
  public static final char ONE_CHAR = '1';
  public static final char TWO_CHAR = '2';

  public static final char FIRST_LOWERCASE_LETTER = 'a';
  public static final char FIRST_UPPERCASE_LETTER = 'A';

  public static final ImmutableList<Character> ABSTRACT_CHARS = ImmutableList.of(ZERO_CHAR,
      ONE_CHAR, TWO_CHAR);
}
