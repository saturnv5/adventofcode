package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Point3D;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    List<Moon> moons = lines.stream().map(Moon::new).toList();
    State initialState = new State(moons);
    int steps = 0;
    while (true) {
      updateMoons(moons);
      steps++;
      State state = new State(moons);
      if (state.equals(initialState)) {
        return steps;
      }
    }
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
        velocity.y += Integer.signum(moon.location.y- location.y);
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

    int[] flatRepresentation() {
      return new int[]{location.x, location.y, location.z, velocity.x, velocity.y, velocity.z};
    }
  }

  private static class State {
    private final int[][] state;

    State(List<Moon> moons) {
      state = moons.stream().map(Moon::flatRepresentation).toArray(int[][]::new);
    }

    @Override
    public boolean equals(Object o) {
      State s = (State) o;
      return Arrays.deepEquals(state, s.state);
    }
  }
}
