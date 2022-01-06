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
    return outputWithOffset(output, 0);
  }

  @Override
  protected long part2(List<String> lines) {
    String line = lines.get(0);
    int targetPos = Integer.parseInt(line.substring(0, 7));
    int[] input =
        IntStream.range(0, lines.get(0).length()).map(i -> lines.get(0).charAt(i) - '0').toArray();
    int[] repeatedInput =
        Stream.generate(() -> input).limit(10000).flatMapToInt(Arrays::stream).toArray();
    int[] output = calculateOutput(repeatedInput);
    return outputWithOffset(output, targetPos);
  }

  private static int outputWithOffset(int[] output, int offset) {
    StringBuilder sb = new StringBuilder();
    while (sb.length() < 8) {
      sb.append((char) (output[offset + sb.length()] + '0'));
    }
    return Integer.parseInt(sb.toString());
  }

  private static int[] calculateOutput(int[] input) {
    int[] output = new int[input.length];
    for (int i = 0; i < 100; i++) {
      executePhase(input, output);
      // Swap input <-> output arrays to save on memory allocation.
      int[] tmp = input;
      input = output;
      output = tmp;
    }
    return input;
  }

  private static void executePhase(int[] input, int[] output) {
    convertToSums(input);
    for (int outIndex = 0; outIndex < output.length; outIndex++) {
      output[outIndex] = calculateOutput(input, outIndex);
    }
  }

  private static void convertToSums(int[] input) {
    for (int i = 1; i < input.length; i++) {
      input[i] += input[i - 1];
    }
  }

  private static int calculateOutput(int[] sums, int outIndex) {
    int val = 0;
    for (int inIndex = outIndex + 1; inIndex <= sums.length; inIndex += outIndex + 1) {
      int patternIndex = inIndex / (outIndex + 1);
      int multiplier = PATTERN[patternIndex % PATTERN.length];
      if (multiplier != 0) {
        int lowIndex = inIndex - 2;
        int highIndex = inIndex + outIndex - 1;
        int low = lowIndex >= 0 ? sums[lowIndex] : 0;
        int high = highIndex < sums.length ? sums[highIndex] : sums[sums.length - 1];
        val += (high - low) * multiplier;
      }
    }
    val %= 10;
    return Math.abs(val);
  }
}
