package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.primitives.Ints;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class Day6 extends Day {
  public static void main(String[] args) {
    new Day6().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    int[] banks = StreamUtils.streamInts(lines.get(0), "\\s").toArray();
    HashSet<List<Integer>> configs = new HashSet<>();
    configs.add(Ints.asList(banks));
    return Stream.generate(() -> redistribute(banks))
        .map(Ints::asList)
        .takeWhile(configs::add)
        .count() + 1;
  }

  private static int[] redistribute(int[] banks) {
    int largestIndex = 0;
    for (int i = 1; i < banks.length; i++) {
      if (banks[i] > banks[largestIndex]) largestIndex = i;
    }
    int val = banks[largestIndex];
    banks[largestIndex] = 0;
    for (int i = (largestIndex + 1) % banks.length; val > 0; i = (i + 1) % banks.length, val--) {
      banks[i]++;
    }
    return banks;
  }
}
