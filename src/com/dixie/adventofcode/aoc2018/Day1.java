package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;

import java.util.HashSet;
import java.util.List;

public class Day1 extends Day {
  public static void main(String[] args) {
    new Day1().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    return lines.stream().mapToInt(Integer::parseInt).sum();
  }

  @Override
  protected Object part2(List<String> lines) {
    HashSet<Integer> frequencies = new HashSet<>();
    int[] deltas = lines.stream().mapToInt(Integer::parseInt).toArray();
    int frequency = 0;
    for (int i = 0; true; i = (i + 1) % deltas.length) {
      frequency += deltas[i];
      if (!frequencies.add(frequency)) {
        return frequency;
      }
    }
  }
}
