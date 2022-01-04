package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Point3D;
import com.google.common.base.Predicates;
import com.google.common.math.LongMath;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day12 extends Day {
  public static void main(String[] args) {
    new Day12().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    List<Moon> moons = lines.stream().map(Moon::new).toList();
    IntStream.range(0, 1000).forEach(i -> updateMoons(moons));
    return moons.stream().mapToInt(Moon::getEnergy).sum();
  }

  @Override
  protected long part2(List<String> lines) {
    List<Moon> moons = lines.stream().map(Moon::new).toList();
    List<Integer> baseX = stateX(moons), baseY = stateY(moons), baseZ = stateZ(moons);
    long xPeriod = 0, yPeriod = 0, zPeriod = 0;

    for (int step = 1; true; step++) {
      if (xPeriod > 0 && yPeriod > 0 && zPeriod > 0) {
        break;
      }
      updateMoons(moons);
      if (xPeriod == 0 && stateX(moons).equals(baseX)) {
        xPeriod = step;
      }
      if (yPeriod == 0 && stateY(moons).equals(baseY)) {
        yPeriod = step;
      }
      if (zPeriod == 0 && stateZ(moons).equals(baseZ)) {
        zPeriod = step;
      }
    }

    return lcm(xPeriod, yPeriod, zPeriod);
  }

  private static List<Integer> stateX(List<Moon> moons) {
    return moons.stream().flatMap(m -> Stream.of(m.location.x, m.velocity.x)).toList();
  }

  private static List<Integer> stateY(List<Moon> moons) {
    return moons.stream().flatMap(m -> Stream.of(m.location.y, m.velocity.y)).toList();
  }

  private static List<Integer> stateZ(List<Moon> moons) {
    return moons.stream().flatMap(m -> Stream.of(m.location.z, m.velocity.z)).toList();
  }

  private static long lcm(long a, long b, long c) {
    long lcm = (a * b) / LongMath.gcd(a, b);
    return (lcm * c) / LongMath.gcd(lcm, c);
  }

  private static void updateMoons(List<Moon> moons) {
    moons.forEach(m -> m.updateVelocity(moons));
    moons.forEach(Moon::updateLocation);
  }

  private static class Moon {
    private final Point3D velocity = new Point3D();
    private final Point3D location;

    Moon(String coords) {
      String[] coord = coords.substring(1, coords.length() - 1).split(", ");
      location = new Point3D(
          Integer.parseInt(coord[0].substring(2)),
          Integer.parseInt(coord[1].substring(2)),
          Integer.parseInt(coord[2].substring(2)));
    }

    void updateVelocity(List<Moon> moons) {
      for (Moon moon : moons) {
        if (moon == this) {
          continue;
        }
        velocity.x += Integer.signum(moon.location.x - location.x);
        velocity.y += Integer.signum(moon.location.y - location.y);
        velocity.z += Integer.signum(moon.location.z - location.z);
      }
    }

    void updateLocation() {
      location.translate(velocity);
    }

    int getEnergy() {
      return (Math.abs(location.x) + Math.abs(location.y) + Math.abs(location.z))
          * (Math.abs(velocity.x) + Math.abs(velocity.y) + Math.abs(velocity.z));
    }
  }
}
