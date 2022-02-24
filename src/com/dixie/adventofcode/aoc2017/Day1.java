package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day1 extends Day {
  public static void main(String[] args) {
    new Day1().solve();
  }

  private int[] digits;

  @Override
  protected void prepare(List<String> lines) {
    digits = lines.get(0).chars().map(c -> c - '0').toArray();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    int sum = 0;
    int inc = part1 ? 1 : digits.length / 2;
    for (int i = 0; i < digits.length; i++) {
      if (digits[i] == digits[(i + inc) % digits.length]) {
        sum += digits[i];
      }
    }
    return sum;
  }
}
