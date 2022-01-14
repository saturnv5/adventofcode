package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.*;

import java.awt.*;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongUnaryOperator;

public class Day15 extends Day {
  public static void main(String[] args) {
    new Day15().solve();
  }

  private static final int WALL = 0, EMPTY = 1, OXYGEN = 2;
  private static final Map<Direction, Integer> DIRECTION_INPUT = Map.of(
      Direction.NORTH, 1,
      Direction.SOUTH, 2,
      Direction.WEST, 3,
      Direction.EAST, 4);

  private Space2D<Integer> space;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    space = constructSpace(lines);
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    Point start = new Point();
    Point oxygen =
        space.streamAllPoints().filter(p -> space.getValueAt(p) == OXYGEN).findFirst().get();
    Path<Point> shortestPath = GraphUtils.shortestNonWeightedPath(
        p -> Direction.CARDINALS.stream()
            .map(d -> d.apply(p))
            .filter(l -> space.getValueAt(l, WALL) != WALL),
        start, oxygen);
    System.out.println(space.toPrintableImage(t -> t == null ? "  " : t == WALL ? "██" : ".."));
    return shortestPath.getCost();
  }

  @Override
  protected Object part2(List<String> lines) {
    Point oxygen =
        space.streamAllPoints().filter(p -> space.getValueAt(p) == OXYGEN).findFirst().get();
    Path<Point> longestPath = GraphUtils.longestBfsPath(
        p -> Direction.CARDINALS.stream()
            .map(d -> d.apply(p))
            .filter(l -> space.getValueAt(l, WALL) != WALL),
        oxygen);
    return longestPath.getCost();
  }

  private static Space2D<Integer> constructSpace(List<String> lines) {
    Intcode ic = new Intcode(lines.get(0));
    Space2D<Integer> space = new Space2D<>();
    Point start = new Point();
    space.setValueAt(start, EMPTY);
    explore(new Point(), null, space, ic::executeUntilOutput);
    return space;
  }

  private static void explore(Point location, Direction fromDirection, Space2D<Integer> space,
      LongUnaryOperator moveFunction) {
    // Move forward.
    boolean movedForward = false;
    if (fromDirection != null) {
      int newTile = (int) moveFunction.applyAsLong(DIRECTION_INPUT.get(fromDirection));
      space.setValueAt(location, newTile);
      movedForward = newTile != WALL;
    }

    // Keep exploring.
    if (movedForward || fromDirection == null) {
      for (Direction nextDirection : Direction.CARDINALS) {
        Point newLocation = nextDirection.apply(location);
        if (space.getValueAt(newLocation) == null) {
          explore(newLocation, nextDirection, space, moveFunction);
        }
      }
    }

    // Go back.
    if (movedForward) {
      Direction back = fromDirection.turnBack();
      moveFunction.applyAsLong(DIRECTION_INPUT.get(back));
    }
  }
}
