package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day16 extends Day {
  public static void main(String[] args) {
    new Day16().solve();
  }

  private static final int[] PATTERN = {0, 1, 0, -1};

  @Override
  protected long part1(List<String> lines) {
    int[] input =
        IntStream.range(0, lines.get(0).length()).map(i -> lines.get(0).charAt(i) - '0').toArray();
    int[] output = calculateOutput(input);
    StringBuilder sb = new StringBuilder();
    while (sb.length() < 8) {
      sb.append((char) (output[sb.length()] + '0'));
    }
    return Long.parseLong(sb.toString());
  }

  @Override
  protected long part2(List<String> lines) {
    String line = lines.get(0);
    int targetPos = Integer.parseInt(line.substring(0, 8));
    int[] input =
        IntStream.range(0, lines.get(0).length()).map(i -> lines.get(0).charAt(i) - '0').toArray();
    int[] repeatedInput =
        Stream.generate(() -> input).limit(10000).flatMapToInt(Arrays::stream).toArray();
    int[] output = calculateOutput(repeatedInput);
    StringBuilder sb = new StringBuilder();
    while (sb.length() < 8) {
      sb.append((char) (output[targetPos + sb.length()] + '0'));
    }
    return Long.parseLong(sb.toString());
  }

  private static int[] calculateOutput(int[] input) {
    for (int i = 0; i < 100; i++) {
      input = executePhase(input);
    }
    return input;
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
