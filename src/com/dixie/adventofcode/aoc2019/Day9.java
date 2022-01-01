package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;

public class Day9 extends Day {
  public static void main(String[] args) {
    new Day9().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    return runProgram(lines.get(0), 1);
  }

  @Override
  protected long part2(List<String> lines) {
    return runProgram(lines.get(0), 2);
  }

  private static long runProgram(String program, int input) {
    return new Intcode(StreamUtils.streamLongs(program, ",").toArray()).executeUntilEnd(input);
  }
}
