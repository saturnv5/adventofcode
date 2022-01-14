package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.MathUtils;
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

  private List<Moon> moons;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    moons = lines.stream().map(Moon::new).toList();
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    IntStream.range(0, 1000).forEach(i -> updateMoons());
    return moons.stream().mapToInt(Moon::getEnergy).sum();
  }

  @Override
  protected Object part2(List<String> lines) {
    List<Integer> baseX = stateX(), baseY = stateY(), baseZ = stateZ();
    long xPeriod = 0, yPeriod = 0, zPeriod = 0;

    for (int step = 1; true; step++) {
      if (xPeriod > 0 && yPeriod > 0 && zPeriod > 0) {
        break;
      }
      updateMoons();
      if (xPeriod == 0 && stateX().equals(baseX)) {
        xPeriod = step;
      }
      if (yPeriod == 0 && stateY().equals(baseY)) {
        yPeriod = step;
      }
      if (zPeriod == 0 && stateZ().equals(baseZ)) {
        zPeriod = step;
      }
    }

    return MathUtils.lcm(xPeriod, yPeriod, zPeriod);
  }

  private List<Integer> stateX() {
    return moons.stream().flatMap(m -> Stream.of(m.location.x, m.velocity.x)).toList();
  }

  private List<Integer> stateY() {
    return moons.stream().flatMap(m -> Stream.of(m.location.y, m.velocity.y)).toList();
  }

  private List<Integer> stateZ() {
    return moons.stream().flatMap(m -> Stream.of(m.location.z, m.velocity.z)).toList();
  }

  private void updateMoons() {
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
