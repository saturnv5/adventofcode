package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class Day2 extends Day {
  public static void main(String[] args) {
    new Day2().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    LongAdder twos = new LongAdder();
    LongAdder threes = new LongAdder();
    lines.forEach(id -> {
      if (hasReps(id, 2)) twos.increment();
      if (hasReps(id, 3)) threes.increment();
    });
    return twos.longValue() * threes.longValue();
  }

  private static boolean hasReps(String id, int reps) {
    return id.chars()
        .filter(c1 -> id.chars().filter(c2 -> c1 == c2).count() == reps)
        .findFirst()
        .isPresent();
  }
}
