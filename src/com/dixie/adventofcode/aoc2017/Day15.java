package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day15 extends Day {
  public static void main(String[] args) {
    new Day15().solve();
  }

  private static final long GEN_A_FACTOR = 16807, GEN_B_FACTOR = 48271, REPS_1 = 40_000_000,
      REPS_2 = 5_000_000;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    long genA = Integer.parseInt(lines.get(0).substring(24));
    long genB = Integer.parseInt(lines.get(1).substring(24));
    int matches = 0;
    for (int i = 0; i < (part1 ? REPS_1 : REPS_2); i++) {
      while((genA = generateNextA(genA)) % (part1 ? 1 : 4) != 0);
      while((genB = generateNextB(genB)) % (part1 ? 1 : 8) != 0);
      if (Long.valueOf(genA).shortValue() == Long.valueOf(genB).shortValue()) {
        matches++;
      }
    }
    return matches;
  }

  private static long generateNextA(long current) {
    return (current * GEN_A_FACTOR) % Integer.MAX_VALUE;
  }

  private static long generateNextB(long current) {
    return (current * GEN_B_FACTOR) % Integer.MAX_VALUE;
  }
}
