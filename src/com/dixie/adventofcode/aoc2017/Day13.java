package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class Day13 extends Day {
  public static void main(String[] args) {
    new Day13().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    LongAdder severity = new LongAdder();
    lines.stream()
        .map(l -> StreamUtils.streamInts(l, ": ").toArray())
        .filter(layer -> layer[0] % (layer[1] * 2 - 2) == 0)
        .forEach(layer -> severity.add(layer[0] * layer[1]));
    return severity.longValue();
  }
}
