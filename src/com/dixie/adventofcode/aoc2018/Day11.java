package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Day11 extends Day {
  public static void main(String[] args) {
    new Day11().solve();
  }

  private int serial;
  private Space2D<Integer> fuelCells;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    serial = Integer.parseInt(lines.get(0));
    fuelCells = new Space2D<>(new Rectangle(1, 1, 300, 300), null);
    fuelCells.streamAllPointsInBounds().forEach(p -> fuelCells.setValueAt(p, powerLevel(p)));
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    return fuelCells.streamAllPointsInBounds()
        .max(Comparator.comparingInt(this::totalPowerAt))
        .get();
  }

  private int powerLevel(Point p) {
    int rackId = p.x + 10;
    int m = (rackId * p.y + serial) * rackId;
    int d100 = (m % 1000) / 100;
    return d100 - 5;
  }

  private int totalPowerAt(Point p) {
    return Space2D.streamAllPointsInBounds(new Rectangle(p.x, p.y, 3, 3))
        .mapToInt(f -> fuelCells.getValueAt(f, 0))
        .sum();
  }
}
