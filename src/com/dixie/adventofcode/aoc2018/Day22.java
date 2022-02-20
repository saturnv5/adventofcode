package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Day22 extends Day {
  public static void main(String[] args) {
    new Day22().solve();
  }

  private static final int ROCKY = 0, WET = 1, NARROW = 2;
  private static final int EROSION_MOD = 20183;
  private static final int X_MULTIPLIER = 16807, Y_MULTIPLIER = 48271;
  private static final int NO_TOOL = 0, TORCH = 1, CLIMBING_GEAR = 2;
  private static final long TOOL_SWITCH_COST = 7;

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
        riskLevel += memoizedType.apply(x, y);
      }
    }
    return riskLevel;
  }

  @Override
  protected Object part2(List<String> lines) {
    return GraphUtils.shortestPath(this::successors, new State(0, 0, TORCH),
        new State(targetX, targetY, TORCH)).getCost();
  }

  private final BiFunction<Integer, Integer, Integer> memoizedType = Memoizer.memoize(this::type);

  private int type(int x, int y) {
    return memoizedErosionLevel.apply(x, y) % 3;
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

  private Iterable<Pair<State, Long>> successors(State state) {
    List<Pair<State, Long>> successors = new ArrayList<>();
    Point p = new Point(state.x(), state.y());
    Direction.CARDINALS.stream()
        .map(dir -> dir.apply(p))
        .filter(n -> canEnter(n.x, n.y, state.tool()))
        .forEach(n -> successors.add(Pair.of(new State(n.x, n.y, state.tool()), 1L)));
    // Switch tools.
    IntStream.of(NO_TOOL, TORCH, CLIMBING_GEAR)
        .filter(t -> t != state.tool())
        .forEach(
            t -> successors.add(Pair.of(new State(state.x(), state.y(), t), TOOL_SWITCH_COST)));
    return successors;
  }

  private boolean canEnter(int x, int y, int tool) {
    if (x < 0 || y < 0) {
      return false;
    }
    return switch (memoizedType.apply(x, y)) {
      case ROCKY -> tool != NO_TOOL;
      case WET -> tool != TORCH;
      case NARROW -> tool != CLIMBING_GEAR;
      default -> false;
    };
  }

  private record State(int x, int y, int tool) {}
}
