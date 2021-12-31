package com.google.adventofcode.aoc2019;

import com.google.adventofcode.lib.Day;

import java.util.List;

public class Day1 extends Day {

  public static void main(String[] args) {
    new Day1().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    return lines.stream().mapToLong(Long::parseLong).map(l -> l / 3 - 2).sum();
  }

  @Override
  protected long part2(List<String> lines) {
    return lines.stream().mapToLong(Long::parseLong)
            .map(l -> {
              l = l / 3 - 2;
              long totalFuel = 0;
              while (l > 0) {
                totalFuel += l;
                l = l / 3 - 2;
              }
              return totalFuel;
            }).sum();
  }
}
