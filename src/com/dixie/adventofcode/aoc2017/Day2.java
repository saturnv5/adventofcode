package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Pair;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.Arrays;
import java.util.List;

public class Day2 extends Day {
  public static void main(String[] args) {
    new Day2().solve();
  }

  private List<int[]> spreadsheet;

  @Override
  protected void prepare(List<String> lines) {
    spreadsheet = lines.stream().map(l -> StreamUtils.streamInts(l, "\\s").toArray()).toList();
  }

  @Override
  protected Object part1(List<String> lines) {
    return spreadsheet.stream().mapToInt(Day2::minMaxDiff).sum();
  }

  @Override
  protected Object part2(List<String> lines) {
    return spreadsheet.stream().mapToInt(Day2::evenQuotient).sum();
  }

  private static int minMaxDiff(int[] nums) {
    return Arrays.stream(nums).max().getAsInt() - Arrays.stream(nums).min().getAsInt();
  }

  private static int evenQuotient(int[] nums) {
    for (Pair<Integer, Integer> pair : StreamUtils.streamPairwise(
        Arrays.stream(nums).boxed().toList()).toList()) {
      if (pair.first % pair.second == 0) {
        return pair.first / pair.second;
      }
    }
    return 0;
  }
}
