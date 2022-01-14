package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;

public class Day5 extends Day {
  public static void main(String[] args) {
    new Day5().solve();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    Intcode ic = new Intcode(StreamUtils.streamInts(lines.get(0), ",").toArray());
    return ic.executeUntilEnd(part1 ? 1 : 5);
  }
}
