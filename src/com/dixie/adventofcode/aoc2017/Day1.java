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
  protected Object part1(List<String> lines) {
    int sum = 0;
    for (int i = 0; i < digits.length; i++) {
      if (digits[i] == digits[(i + 1) % digits.length]) {
        sum += digits[i];
      }
    }
    return sum;
  }

  @Override
  protected Object part2(List<String> lines) {
    int sum = 0;
    int halfLength = digits.length / 2;
    for (int i = 0; i < digits.length; i++) {
      if (digits[i] == digits[(i + halfLength) % digits.length]) {
        sum += digits[i];
      }
    }
    return sum;
  }
}
