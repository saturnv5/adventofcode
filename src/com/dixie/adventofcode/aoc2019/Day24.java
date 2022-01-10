package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class Day24 extends Day {
  public static void main(String[] args) {
    new Day24().solve();
  }

  private Space2D<Boolean> space;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    space = Space2D.parseFromStrings(lines, c -> c == '#');
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    HashSet<Long> ratings = new HashSet<>();
    ratings.add(rating());
    while (true) {
      long rating = advanceTime();
      if (!ratings.add(rating)) {
        System.out.println(space.toPrintableImage(b -> b ? "#" : "."));
        return rating;
      }
    }
  }

  private long rating() {
    return space.streamOccurrencesOf(true)
        .mapToLong(p -> 1 << (p.x + p.y * space.getBounds().width))
        .sum();
  }

  private long advanceTime() {
    List<Point> toAdd =
        space.streamOccurrencesOf(false).filter(p -> (numAdjBugs(p) + 1) / 2 == 1).toList();
    List<Point> toRemove = space.streamOccurrencesOf(true).filter(p -> numAdjBugs(p) != 1).toList();
    toAdd.forEach(p -> space.setValueAt(p, true));
    toRemove.forEach(p -> space.setValueAt(p, false));
    return rating();
  }

  private long numAdjBugs(Point p) {
    return Direction.CARDINALS.stream()
        .filter(dir -> space.getValueAt(dir.apply(p), false))
        .count();
  }
}
