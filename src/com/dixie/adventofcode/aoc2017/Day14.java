package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.aoc2017.common.KnotHash;
import com.dixie.adventofcode.lib.Day;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class Day14 extends Day {
  public static void main(String[] args) {
    new Day14().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    String input = lines.get(0);
    return IntStream.range(0, 128)
        .mapToObj(i -> String.format("%s-%d", input, i))
        .flatMapToInt(in -> Arrays.stream(KnotHash.computeHash(in)))
        .map(Integer::bitCount)
        .sum();
  }
}
