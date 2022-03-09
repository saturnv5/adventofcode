package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.aoc2017.common.KnotHash;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day10 extends Day {
  public static void main(String[] args) {
    new Day10().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    int[] list = IntStream.range(0, 256).toArray();
    int[] hashLengths = StreamUtils.streamInts(lines.get(0), ",").toArray();
    return KnotHash.performHash(list, hashLengths);
  }

  @Override
  protected Object part2(List<String> lines) {
    int[] denseHash = KnotHash.computeHash(lines.get(0));
    StringBuilder sb = new StringBuilder();
    Arrays.stream(denseHash).mapToObj(i -> String.format("%02x", i)).forEach(sb::append);
    return sb.toString();
  }
}
