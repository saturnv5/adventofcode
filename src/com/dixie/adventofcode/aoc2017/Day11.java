package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAccumulator;

public class Day11 extends Day {
  public static void main(String[] args) {
    new Day11().solve();
  }

  private static final Map<String, Point> HEX_DIR = Map.of(
      "n", new Point(0, -2),
      "ne", new Point(1, -1),
      "se", new Point(1, 1),
      "s", new Point(0, 2),
      "sw", new Point(-1, 1),
      "nw", new Point(-1, -1));

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    String[] path = lines.get(0).split(",");
    LongAccumulator acc = new LongAccumulator(Math::max, 0);
    Point pos = new Point(0, 0);
    Arrays.stream(path).map(HEX_DIR::get).forEach(d -> {
      pos.translate(d.x, d.y);
      acc.accumulate(dist(pos));
    });
    return part1 ? dist(pos) : acc.longValue();
  }

  private static int dist(Point pos) {
    return (Math.abs(pos.x) + Math.abs(pos.y)) / 2;
  }
}
