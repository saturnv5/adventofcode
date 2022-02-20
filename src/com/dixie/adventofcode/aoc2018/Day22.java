package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;

import java.util.List;
import java.util.function.BiFunction;

public class Day22 extends Day {
  public static void main(String[] args) {
    new Day22().solve();
  }

  private static final int EROSION_MOD = 20183;
  private static final int X_MULTIPLIER = 16807, Y_MULTIPLIER = 48271;

  private int depth;
  private int targetX, targetY;

  @Override
  protected void prepare(List<String> lines) {
    depth = Integer.parseInt(lines.get(0).substring(7));
    String[] coord = lines.get(1).substring(8).split(",");
    targetX = Integer.parseInt(coord[0]);
    targetY = Integer.parseInt(coord[1]);
  }

  @Override
  protected Object part1(List<String> lines) {
    int riskLevel = 0;
    for (int y = 0; y <= targetY; y++) {
      for (int x = 0; x <= targetX; x++) {
        riskLevel += memoizedErosionLevel.apply(x, y) % 3;
      }
    }
    return riskLevel;
  }

  private final BiFunction<Integer, Integer, Integer> memoizedErosionLevel =
      Memoizer.memoize(this::erosionLevel);

  private int erosionLevel(int x, int y) {
    if ((x == 0 && y == 0) || (x == targetX && y == targetY)) {
      return depth % EROSION_MOD;
    }
    if (x == 0) {
      return (y * Y_MULTIPLIER + depth) % EROSION_MOD;
    } else if (y == 0) {
      return (x * X_MULTIPLIER + depth) % EROSION_MOD;
    }
    return (memoizedErosionLevel.apply(x - 1, y) * memoizedErosionLevel.apply(x, y - 1) + depth) %
        EROSION_MOD;
  }
}
