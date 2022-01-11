package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Day24 extends Day {
  public static void main(String[] args) {
    new Day24().solve();
  }

  private Map<Integer, Space2D<Boolean>> layers = new HashMap<>();
  private Map<Integer, Integer> layerBugs = new HashMap<>();
  private Space2D<Boolean> space0;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    space0 = Space2D.parseFromStrings(lines, c -> c == '#');
    layers.put(0, space0);
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    HashSet<Long> ratings = new HashSet<>();
    ratings.add(rating(space0));
    while (true) {
      long rating = advanceTime(0, false);
      if (!ratings.add(rating)) {
        System.out.println(space0.toPrintableImage(b -> b ? "#" : "."));
        return rating;
      }
    }
  }

  @Override
  protected long part2(List<String> lines) {
    IntStream.range(0, 200).forEach(i -> advanceTime());
    return layerBugs.values().stream().mapToInt(Integer::intValue).sum();
  }

  private long rating(Space2D<Boolean> space) {
    return space.streamOccurrencesOf(true)
        .mapToLong(p -> 1L << (p.x + p.y * space.getBounds().width))
        .sum();
  }

  private void advanceTime() {

  }

  private long advanceTime(int level, boolean folded) {
    Space2D<Boolean> space = layer(level);
    List<Point> toAdd =
        space.streamOccurrencesOf(false)
            .filter(p -> (numAdjBugs(space, level, p, folded) + 1) / 2 == 1)
            .toList();
    List<Point> toRemove =
        space.streamOccurrencesOf(true)
            .filter(p -> numAdjBugs(space, level, p, folded) != 1)
            .toList();
    toAdd.forEach(p -> space.setValueAt(p, true));
    toRemove.forEach(p -> space.setValueAt(p, false));
    return rating(space);
  }

  private long numAdjBugs(Space2D<Boolean> space, int level, Point p, boolean folded) {
    return Direction.CARDINALS.stream()
        .filter(dir -> space.getValueAt(dir.apply(p), false))
        .count();
  }

  private Space2D<Boolean> layer(int level) {
    return layers.computeIfAbsent(level,
        l -> new Space2D<>(space0.getBounds().width, space0.getBounds().height, false));
  }

  private int numBugs(int level) {
    return layerBugs.computeIfAbsent(level, l -> (int) layer(l).streamOccurrencesOf(true).count());
  }
}
