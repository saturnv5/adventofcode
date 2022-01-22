package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.collect.ImmutableList;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Day24 extends Day {
  public static void main(String[] args) {
    new Day24().solve();
  }

  private static final Point CENTER = new Point(2, 2);

  private final Map<Integer, Space2D<Boolean>> layers = new HashMap<>();
  private final Map<Integer, Integer> layerBugs = new HashMap<>();
  private Space2D<Boolean> space0;

  @Override
  protected void prepare(List<String> lines) {
    space0 = Space2D.parseFromStrings(lines, c -> c == '#');
    layers.put(0, space0);
  }

  @Override
  protected Object part1(List<String> lines) {
    HashSet<Long> ratings = new HashSet<>();
    ratings.add(rating(space0));
    while (true) {
      long rating = advanceTime();
      if (!ratings.add(rating)) {
        System.out.println(space0.toPrintableImage(b -> b ? "#" : "."));
        return rating;
      }
    }
  }

  @Override
  protected Object part2(List<String> lines) {
    IntStream.range(0, 200).forEach(i -> advanceTimeFolded());
    return layers.keySet().stream().mapToInt(this::numBugsInLayer).sum();
  }

  private long rating(Space2D<Boolean> space) {
    return space.streamOccurrencesOf(true)
        .mapToLong(p -> 1L << (p.x + p.y * space.getBounds().width))
        .sum();
  }

  private long advanceTime() {
    List<Point> toAdd = bugsToAdd(space0, 0, false);
    List<Point> toRemove = bugsToRemove(space0, 0, false);
    toAdd.forEach(p -> space0.setValueAt(p, true));
    toRemove.forEach(p -> space0.setValueAt(p, false));
    return rating(space0);
  }

  private void advanceTimeFolded() {
    HashMap<Integer, List<Point>> toAdd = new HashMap<>();
    HashMap<Integer, List<Point>> toRemove = new HashMap<>();
    for (int level : ImmutableList.copyOf(layers.keySet())) {
      toRemove.put(level, bugsToRemove(layer(level), level, true));
    }
    for (int level : layers.keySet()) {
      toAdd.put(level, bugsToAdd(layer(level), level, true));
    }
    toAdd.forEach((lvl, ps) -> {
      Space2D<Boolean> space = layer(lvl);
      ps.forEach(p -> space.setValueAt(p, true));
      layerBugs.remove(lvl);
    });
    toRemove.forEach((lvl, ps) -> {
      Space2D<Boolean> space = layer(lvl);
      ps.forEach(p -> space.setValueAt(p, false));
      layerBugs.remove(lvl);
    });
  }

  private List<Point> bugsToAdd(Space2D<Boolean> space, int level, boolean folded) {
    return space.streamOccurrencesOf(false)
        .filter(p -> !folded || !CENTER.equals(p))
        .filter(p -> (numAdjBugs(space, level, p, folded, false) + 1) / 2 == 1)
        .toList();
  }

  private List<Point> bugsToRemove(Space2D<Boolean> space, int level, boolean folded) {
    return space.streamOccurrencesOf(true)
        .filter(p -> numAdjBugs(space, level, p, folded, true) != 1)
        .toList();
  }

  private int numAdjBugs(
      Space2D<Boolean> space, int level, Point p, boolean folded, boolean expandIfAtEdge) {
    int adjBugs = 0;
    for (Direction dir : Direction.CARDINALS) {
      Point adj = dir.apply(p);
      if (folded) {
        if (adj.equals(CENTER)) {
          adjBugs += numBugsInLayerEdge(level + 1, dir.turnBack(), expandIfAtEdge);
          continue;
        } else if (!space.getBounds().contains(adj)) {
          if (hasBugAtInnerEdge(level - 1, dir, expandIfAtEdge)) {
            adjBugs++;
          }
          continue;
        }
      }
      if (space.getValueAt(adj, false)) {
        adjBugs++;
      }
    }
    return adjBugs;
  }

  private Space2D<Boolean> layer(int level) {
    return layers.computeIfAbsent(level,
        l -> new Space2D<>(space0.getBounds().width, space0.getBounds().height, false));
  }

  private int numBugsInLayer(int level) {
    if (layers.containsKey(level)) {
      return layerBugs.computeIfAbsent(level,
          l -> (int) layer(l).streamOccurrencesOf(true).count());
    }
    return 0;
  }

  private int numBugsInLayerEdge(int level, Direction edge, boolean expandIfMissing) {
    if (numBugsInLayer(level) == 0) {
      if (expandIfMissing) {
        layer(level);
      }
      return 0;
    }
    Space2D<Boolean> space = layer(level);
    IntStream range = IntStream.range(0, 5);
    return (int) switch (edge) {
      case NORTH -> range.filter(x -> space.getValueAt(x, 0, false)).count();
      case SOUTH -> range.filter(x -> space.getValueAt(x, 4, false)).count();
      case WEST -> range.filter(y -> space.getValueAt(0, y, false)).count();
      case EAST -> range.filter(y -> space.getValueAt(4, y, false)).count();
      default -> 0;
    };
  }

  private boolean hasBugAtInnerEdge(int level, Direction edge, boolean expandIfMissing) {
    if (numBugsInLayer(level) == 0) {
      if (expandIfMissing) {
        layer(level);
      }
      return false;
    }
    Space2D<Boolean> space = layer(level);
    return switch (edge) {
      case NORTH -> space.getValueAt(2, 1, false);
      case SOUTH -> space.getValueAt(2, 3, false);
      case WEST -> space.getValueAt(1, 2, false);
      case EAST -> space.getValueAt(3, 2, false);
      default -> false;
    };
  }
}
