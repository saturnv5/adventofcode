package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day16 extends Day {
  public static void main(String[] args) {
    new Day16().solve();
  }

  private static final int[] PATTERN = {0, 1, 0, -1};

  @Override
  protected long part1(List<String> lines) {
    int[] input =
        IntStream.range(0, lines.get(0).length()).map(i -> lines.get(0).charAt(i) - '0').toArray();
    for (int i = 0; i < 100; i++) {
      input = executePhase(input);
    }
    StringBuilder sb = new StringBuilder();
    while (sb.length() < 8) {
      sb.append((char) (input[sb.length()] + '0'));
    }
    return Long.parseLong(sb.toString());
  }

  private static int[] executePhase(int[] input) {
    int[] output = new int[input.length];
    for (int outIndex = 0; outIndex < output.length; outIndex++) {
      long val = 0;
      for (int inIndex = 0; inIndex < input.length; inIndex++) {
        val += (long) getMultiplier(inIndex, outIndex) * input[inIndex];
      }
      val %= 10;
      output[outIndex] = (int) Math.abs(val);
    }
    return output;
  }

  private static int getMultiplier(int inputIndex, int outputIndex) {
    int patternIndex = (inputIndex + 1) / (outputIndex + 1);
    return PATTERN[patternIndex % PATTERN.length];
  }
}
