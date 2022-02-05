package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.base.Splitter;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.IntStream;

public class Day17 extends Day {
  public static void main(String[] args) {
    new Day17().solve();
  }

  private enum Cell { SPRING, CLAY, WET_SAND, WATER, DEBUG }

  private static final Splitter INPUT_SPLITTER =
      Splitter.onPattern(".=|, |\\.\\.").omitEmptyStrings();
  private static final Point SPRING = new Point(500, 0);

  private final Space2D<Cell> ground = new Space2D<>();

  @Override
  protected void prepare(List<String> lines) {
    ground.setValueAt(SPRING, Cell.SPRING);
    lines.forEach(this::parseLine);
    simulateWaterFlow();
    System.out.println(ground.toPrintableImage(Day17::cellToString));
  }

  @Override
  protected Object part1(List<String> lines) {
    return ground.streamOccurrencesOf(c -> c == Cell.WET_SAND || c == Cell.WATER).count();
  }

  private void simulateWaterFlow() {
    ArrayDeque<Point> waterFringe = new ArrayDeque<>();
    waterFringe.offer(new Point(SPRING));
    while (!waterFringe.isEmpty()) {
      Point lead = waterFringe.poll();
      if (ground.getValueAt(lead) == Cell.WATER) {
        // Already flooded up to here, retreat.
        lead = retreatFringe(lead);
        if (lead != null) waterFringe.offer(lead);
        continue;
      }
      if (!ground.getBounds().contains(Direction.DOWN.apply(lead))) {
        // Reached bottom boundary.
        continue;
      }
      if (tryFlowDown(lead)) {
        // Successfully followed or filled downwards.
        waterFringe.offer(lead);
      } else {
        // Try to flood left and right instead.
        Point left = Direction.LEFT.apply(lead);
        if (ground.getValueAt(left) == null) {
          ground.setValueAt(left, Cell.WET_SAND);
          waterFringe.offer(left);
        }
        Point right = Direction.RIGHT.apply(lead);
        if (ground.getValueAt(right) == null) {
          ground.setValueAt(right, Cell.WET_SAND);
          waterFringe.offer(right);
        }
      }
    }
  }

  private Point retreatFringe(Point lead) {
    Point up = Direction.UP.apply(lead);
    while (ground.getBounds().contains(up)) {
      Cell cell = ground.getValueAt(up);
      if (cell == Cell.WET_SAND) {
        return up;
      } else if (cell != Cell.WATER) {
        return null;
      }
      up = Direction.UP.apply(up);
    }
    return null;
  }

  private boolean tryFlowDown(Point lead) {
    Point down = Direction.DOWN.apply(lead);
    Cell cell = ground.getValueAt(down);
    if (cell == null || cell == Cell.WET_SAND) {
      ground.setValueAt(down, Cell.WET_SAND);
      lead.setLocation(down);
      return true;
    } else if (tryFillLevel(lead)) {
      lead.y--;
      return true;
    }
    return false;
  }

  private boolean tryFillLevel(Point from) {
    Point left = canFlowToClayInDirection(from, Direction.LEFT);
    if (left == null) return false;
    Point right = canFlowToClayInDirection(from, Direction.RIGHT);
    if (right == null) return false;
    IntStream.range(left.x + 1, right.x).forEach(x -> ground.setValueAt(x, from.y, Cell.WATER));
    return true;
  }

  private Point canFlowToClayInDirection(Point from, Direction direction) {
    Point search = direction.apply(from);
    while (ground.getBounds().contains(search)) {
      if (ground.getValueAt(search) == Cell.CLAY) {
        return search;
      }
      Cell below = ground.getValueAt(Direction.DOWN.apply(search));
      if (below != Cell.CLAY && below != Cell.WATER) {
        return null;
      }
      search = direction.apply(search);
    }
    return null;
  }

  private void parseLine(String line) {
    int[] tokens = INPUT_SPLITTER.splitToStream(line).mapToInt(Integer::parseInt).toArray();
    if (line.charAt(0) == 'x') {
      int x = tokens[0];
      IntStream.rangeClosed(tokens[1], tokens[2]).forEach(y -> ground.setValueAt(x, y, Cell.CLAY));
    } else {
      int y = tokens[0];
      IntStream.rangeClosed(tokens[1], tokens[2]).forEach(x -> ground.setValueAt(x, y, Cell.CLAY));
    }
  }

  private static String cellToString(Cell cell) {
    if (cell == null) return " ";
    return switch (cell) {
      case SPRING -> "+";
      case CLAY -> "▓";
      case WET_SAND -> ".";
      case WATER -> "~";
      case DEBUG -> "$";
    };
  }
}
