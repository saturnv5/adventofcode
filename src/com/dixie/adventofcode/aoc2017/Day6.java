package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.primitives.Ints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day6 extends Day {
  public static void main(String[] args) {
    new Day6().solve();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    int[] banks = StreamUtils.streamInts(lines.get(0), "\\s").toArray();
    HashMap<List<Integer>, Integer> configs = new HashMap<>();
    int steps = 0;
    while (true) {
      Integer index = configs.putIfAbsent(Ints.asList(banks), steps);
      if (index != null) {
        return part1 ? steps : steps - index;
      }
      redistribute(banks);
      steps++;
    }
  }

  private static void redistribute(int[] banks) {
    int largestIndex = 0;
    for (int i = 1; i < banks.length; i++) {
      if (banks[i] > banks[largestIndex]) largestIndex = i;
    }
    int val = banks[largestIndex];
    banks[largestIndex] = 0;
    for (int i = (largestIndex + 1) % banks.length; val > 0; i = (i + 1) % banks.length, val--) {
      banks[i]++;
    }
  }
}
