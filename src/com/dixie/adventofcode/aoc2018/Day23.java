package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Point3D;
import com.google.common.base.Splitter;

import java.util.Comparator;
import java.util.List;

public class Day23 extends Day {
  public static void main(String[] args) {
    new Day23().solve();
  }

  private static final Splitter INPUT_SPLITTER =
      Splitter.onPattern("pos|r|=|<|>|,| ").trimResults().omitEmptyStrings();

  private List<Nanobot> nanobots;

  @Override
  protected void prepare(List<String> lines) {
    nanobots = lines.stream()
        .map(Nanobot::parse)
        .toList();
  }

  @Override
  protected Object part1(List<String> lines) {
    Nanobot strongest = nanobots.stream().max(Comparator.comparingInt(Nanobot::radius)).get();
    return nanobots.stream().filter(n -> isInRange(strongest, n)).count();
  }

  private boolean isInRange(Nanobot from, Nanobot to) {
    return dist(from.position(), to.position()) <= from.radius();
  }

  private static int dist(Point3D p1, Point3D p2) {
    return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z);
  }

  private record Nanobot(Point3D position, int radius) {
    private static Nanobot parse(String line) {
      int[] vals = INPUT_SPLITTER.splitToStream(line).mapToInt(Integer::parseInt).toArray();
      Point3D pos = new Point3D(vals[0], vals[1], vals[2]);
      return new Nanobot(pos, vals[3]);
    }
  }
}
