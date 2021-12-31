package com.google.adventofcode.aoc2019;

import com.google.adventofcode.lib.Day;

import java.util.List;
import java.util.stream.IntStream;

public class Day4 extends Day {
  public static void main(String[] args) {
    new Day4().solve();
  }

  @Override
  protected long solve(List<String> lines, boolean part1) {
    int dashIndex = lines.get(0).indexOf('-');
    int from = Integer.parseInt(lines.get(0).substring(0, dashIndex));
    int to = Integer.parseInt(lines.get(0).substring(dashIndex + 1));
    return IntStream.rangeClosed(from, to).filter(p -> isValid(p, part1)).count();
  }

  private static boolean isValid(int password, boolean part1) {
    char[] digits = String.valueOf(password).toCharArray();
    boolean hasConsecutive = false;
    int consecutive = 0;
    for (int i = 1; i < digits.length; i++) {
      char a = digits[i - 1];
      char b = digits[i];
      if (a > b) {
        return false;
      }
      if (a == b) {
        if (part1) {
          hasConsecutive = true;
        }
        consecutive++;
      }
      if (a != b || i == digits.length - 1){
        hasConsecutive |= consecutive == 1;
        consecutive = 0;
      }
    }
    return hasConsecutive;
  }
}
