package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Day11 extends Day {
  public static void main(String[] args) {
    new Day11().solve();
  }

  private int serial;
  private Space2D<Integer> fuelCells;

  @Override
  protected void prepare(List<String> lines) {
    serial = Integer.parseInt(lines.get(0));
    fuelCells = new Space2D<>(new Rectangle(1, 1, 300, 300), null);
    fuelCells.streamAllPointsInBounds().forEach(p -> fuelCells.setValueAt(p, powerLevel(p)));
  }

  @Override
  protected Object part1(List<String> lines) {
    Rectangle max = fuelCells.streamAllPointsInBounds()
        .map(p -> new Rectangle(p.x, p.y, 3, 3))
        .max(Comparator.comparingInt(this::totalPowerWithinSquare))
        .get();
    return max.x + "," + max.y;
  }

  @Override
  protected Object part2(List<String> lines) {
    Rectangle max = fuelCells.streamAllPointsInBounds()
        .flatMap(p -> IntStream.range(1, 301 - Math.max(p.x, p.y))
            .mapToObj(size -> new Rectangle(p.x, p.y, size, size)))
        .max(Comparator.comparingInt(this::totalPowerWithinSquare))
        .get();
    return max.x + "," + max.y + "," + max.width;
  }

  private int powerLevel(Point p) {
    int rackId = p.x + 10;
    int m = (rackId * p.y + serial) * rackId;
    int d100 = (m % 1000) / 100;
    return d100 - 5;
  }

  private int totalPowerWithinSquare(Rectangle square) {
    return memoizedTotalPower.apply(new Point(square.x, square.y))
        - memoizedTotalPower.apply(new Point(square.x, square.y + square.height))
        - memoizedTotalPower.apply(new Point(square.x + square.width, square.y))
        + memoizedTotalPower.apply(new Point(square.x + square.width, square.y + square.height));
  }

  private final Function<Point, Integer> memoizedTotalPower = Memoizer.memoize(this::totalPowerFromPoint);

  private int totalPowerFromPoint(Point p) {
    if (!fuelCells.getBounds().contains(p)) {
      return 0;
    }
    if (p.x == 300 && p.y == 300) {
      return fuelCells.getValueAt(p);
    } else if (p.x == 300) {
      return fuelCells.getValueAt(p) + memoizedTotalPower.apply(new Point(p.x, p.y + 1));
    } else if (p.y == 300) {
      return fuelCells.getValueAt(p) + memoizedTotalPower.apply(new Point(p.x + 1, p.y));
    } else {
      return fuelCells.getValueAt(p)
          + memoizedTotalPower.apply(new Point(p.x + 1, p.y))
          + memoizedTotalPower.apply(new Point(p.x, p.y + 1))
          - memoizedTotalPower.apply(new Point(p.x + 1, p.y + 1));
    }
  }
}
