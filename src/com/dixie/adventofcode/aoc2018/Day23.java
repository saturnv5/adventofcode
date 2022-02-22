package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Cuboid;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Point3D;
import com.google.common.base.Splitter;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

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

  @Override
  protected Object part2(List<String> lines) {
    Point3D origin = new Point3D();
    PriorityQueue<Region> candidates = new PriorityQueue<>(
        Comparator.<Region>comparingInt(r -> r.reachableBots.size()).reversed()
            .thenComparingInt(r -> dist(origin, r.cuboid().max())));
    candidates.offer(Region.fromBots(nanobots));
    while (!candidates.isEmpty()) {
      Region candidate = candidates.poll();
      if (candidate.cuboid().size() == 0) {
        return dist(origin, candidate.cuboid().min());
      }
      candidate.subdivide().forEach(candidates::offer);
    }
    return 0;
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

  private record Region(Cuboid cuboid, List<Nanobot> reachableBots) {
    static Region fromBots(List<Nanobot> nanobots) {
      Cuboid cuboid = new Cuboid(new Point3D(), new Point3D());
      nanobots.stream().map(Nanobot::position).forEach(cuboid::add);
      return new Region(cuboid, nanobots);
    }

    List<Region> subdivide() {
      return cuboid.subdivide()
          .stream()
          .map(this::withSmallerCuboid)
          .filter(r -> !r.reachableBots.isEmpty())
          .toList();
    }

    Region withSmallerCuboid(Cuboid cuboid) {
      List<Nanobot> filteredBots =
          reachableBots.stream().filter(n -> cuboid.dist(n.position()) <= n.radius()).toList();
      return new Region(cuboid, filteredBots);
    }
  }
}
