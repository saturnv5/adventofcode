package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day9 extends Day {
  public static void main(String[] args) {
    new Day9().solve();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    int totalScore = 0;
    int currentScore = 0;
    AtomicInteger totalGarbage = new AtomicInteger();
    String group = lines.get(0);
    for (int i = 0; i < group.length(); i++) {
      char ch = group.charAt(i);
      switch (ch) {
        case '{' -> currentScore++;
        case '}' -> totalScore += currentScore--;
        case '<' -> i = findGarbageEndIndex(group, i + 1, totalGarbage);
      }
    }
    return part1 ? totalScore : totalGarbage.get();
  }

  private static int findGarbageEndIndex(String group, int fromIndex, AtomicInteger totalGarbage) {
    for (int i = fromIndex; i < group.length(); i++) {
      char ch = group.charAt(i);
      if (ch == '!') i++;
      else if (ch == '>') return i;
      else totalGarbage.incrementAndGet();
    }
    return group.length();
  }
}
