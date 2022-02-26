package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day5 extends Day {
  public static void main(String[] args) {
    new Day5().solve();
  }

  private int[] jumps;

  @Override
  protected void prepare(List<String> lines) {
    jumps = lines.stream().mapToInt(Integer::parseInt).toArray();
  }

  @Override
  protected Object part1(List<String> lines) {
    int[] jumpsInstance = jumps.clone();
    int steps = 0;
    for (int i = 0; i >= 0 && i < jumpsInstance.length; steps++, i += jumpsInstance[i]++);
    return steps;
  }

  @Override
  protected Object part2(List<String> lines) {
    int[] jumpsInstance = jumps.clone();
    int steps = 0;
    for (int i = 0; i >= 0 && i < jumpsInstance.length; steps++) {
      int jump = jumpsInstance[i];
      if (jump >= 3) jumpsInstance[i]--;
      else jumpsInstance[i]++;
      i += jump;
    }
    return steps;
  }
}
