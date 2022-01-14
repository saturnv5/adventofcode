package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Pair;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class Day2 extends Day {
  public static void main(String[] args) {
    new Day2().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    LongAdder twos = new LongAdder();
    LongAdder threes = new LongAdder();
    lines.forEach(id -> {
      if (hasReps(id, 2)) twos.increment();
      if (hasReps(id, 3)) threes.increment();
    });
    return twos.longValue() * threes.longValue();
  }

  @Override
  protected Object part2(List<String> lines) {
    Pair<String, String> match =
        StreamUtils.streamPairwise(lines).filter(Day2::differsByOne).findFirst().get();
    StringBuilder sb = new StringBuilder(match.first.length() - 1);
    IntStream.range(0, match.first.length())
        .filter(i -> match.first.charAt(i) == match.second.charAt(i))
        .mapToObj(match.first::charAt)
        .forEach(sb::append);
    return sb;
  }

  private static boolean hasReps(String id, int reps) {
    return id.chars()
        .filter(c1 -> id.chars().filter(c2 -> c1 == c2).count() == reps)
        .findFirst()
        .isPresent();
  }

  private static boolean differsByOne(Pair<String, String> pair) {
    int diffs = 0;
    for (int i = 0; i < pair.first.length(); i++) {
      if (pair.first.charAt(i) != pair.second.charAt(i)) {
        diffs++;
      }
    }
    return diffs == 1;
  }
}
