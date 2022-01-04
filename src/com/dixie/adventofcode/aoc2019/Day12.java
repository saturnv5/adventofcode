package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Point3D;
import com.google.common.base.Predicates;
import com.google.common.math.LongMath;

import java.util.*;
import java.util.stream.IntStream;

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
    Moon[] moons = lines.stream().map(Moon::new).toArray(Moon[]::new);
    Moon[] initialState = clone(moons);
    Moon[] empty = new Moon[moons.length];
    ArrayList<Long> periods = new ArrayList<>();

    // Record individual periods for x-axis only.
    Moon[] moonsX = moons;
    List<Moon> moonsXList = Arrays.asList(Arrays.copyOf(moonsX, moonsX.length));
    long steps = 0;
    while(!Arrays.equals(moonsX, empty)) {
      Arrays.stream(moonsX)
          .filter(Predicates.notNull())
          .forEach(m -> m.updateVelocityX(moonsXList));
      Arrays.stream(moonsX).filter(Predicates.notNull()).forEach(Moon::updateLocation);
      steps++;
      maybeRecordPeriod(initialState, moonsX, periods, steps);
    }

    // Record individual periods for y-axis only.
    Moon[] moonsY = clone(initialState);
    List<Moon> moonsYList = Arrays.asList(Arrays.copyOf(moonsY, moonsY.length));
    steps = 0;
    while(!Arrays.equals(moonsY, empty)) {
      Arrays.stream(moonsY)
          .filter(Predicates.notNull())
          .forEach(m -> m.updateVelocityY(moonsYList));
      Arrays.stream(moonsY).filter(Predicates.notNull()).forEach(Moon::updateLocation);
      steps++;
      maybeRecordPeriod(initialState, moonsY, periods, steps);
    }

    // Record individual periods for z-axis only.
    Moon[] moonsZ = clone(initialState);
    List<Moon> moonsZList = Arrays.asList(Arrays.copyOf(moonsZ, moonsZ.length));
    steps = 0;
    while(!Arrays.equals(moonsZ, empty)) {
      Arrays.stream(moonsZ).filter(Predicates.notNull())
          .forEach(m -> m.updateVelocityZ(moonsZList));
      Arrays.stream(moonsZ).filter(Predicates.notNull()).forEach(Moon::updateLocation);
      steps++;
      maybeRecordPeriod(initialState, moonsZ, periods, steps);
    }

    return lcm(periods);
  }

  private static void maybeRecordPeriod(
      Moon[] initialState, Moon[] currentState, List<Long> periods, long steps) {
    for (int i = 0; i < initialState.length; i++) {
      if (initialState[i].equals(currentState[i])) {
        currentState[i] = null;
        periods.add(steps);
      }
    }
  }

  private static Moon[] clone(Moon[] moons) {
    return Arrays.stream(moons).map(Moon::new).toArray(Moon[]::new);
  }

  private static long lcm(List<Long> numbers) {
    return -1;
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

    Moon(Moon moon) {
      this.velocity.set(moon.velocity);
      this.location = new Point3D(moon.location);
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

    void updateVelocityX(List<Moon> moons) {
      for (Moon moon : moons) {
        if (moon == this) {
          continue;
        }
        velocity.x += Integer.signum(moon.location.x - location.x);
      }
    }

    void updateVelocityY(List<Moon> moons) {
      for (Moon moon : moons) {
        if (moon == this) {
          continue;
        }
        velocity.y += Integer.signum(moon.location.y - location.y);
      }
    }

    void updateVelocityZ(List<Moon> moons) {
      for (Moon moon : moons) {
        if (moon == this) {
          continue;
        }
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

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Moon moon = (Moon) o;
      return Objects.equals(velocity, moon.velocity) &&
          Objects.equals(location, moon.location);
    }
  }
}
