package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Day15 extends Day {
  public static void main(String[] args) {
    new Day15().solve();
  }

  private static final List<Direction> SUCCESSOR_DIRECTIONS =
      List.of(Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH);

  private Space2D<Unit> space;
  private int numElves;
  private int numGoblins;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    space = Space2D.parseFromStrings(lines, Day15::parseCell);
    numElves = (int) space.streamAllPoints()
        .map(space::getValueAt)
        .filter(u -> u != null && u.type == Unit.Type.ELF)
        .count();
    numGoblins = (int) space.streamAllPoints()
        .map(space::getValueAt)
        .filter(u -> u != null && u.type == Unit.Type.GOBLIN)
        .count();
    return super.solve(lines, part1);
  }

  private static Unit parseCell(int cell) {
    return switch (cell) {
      case '#' -> new Unit(Unit.Type.WALL);
      case 'E' -> new Unit(Unit.Type.ELF);
      case 'G' -> new Unit(Unit.Type.GOBLIN);
      default -> null;
    };
  }

  private boolean simulateRound() {
    HashSet<Unit> hasMoved = new HashSet<>(numElves + numGoblins);
    AtomicBoolean hasCombatEnded = new AtomicBoolean(false);
    space.streamAllPointsInBounds().forEach(p -> {
      if (hasCombatEnded.get()) return;
      Unit unit = space.getValueAt(p);
      if (unit == null || unit.type == Unit.Type.WALL) {
        return;
      }
      if (!hasMoved.add(unit)) {
        return;
      }
      hasCombatEnded.set(simulateUnit(p, unit));
    });
    return hasCombatEnded.get();
  }

  private boolean simulateUnit(Point location, Unit unit) {
    return false;
  }

  private Stream<Point> successors(Point from) {
    return SUCCESSOR_DIRECTIONS.stream()
        .map(d -> d.apply(from))
        .filter(p -> space.getValueAt(p) == null);
  }

  private static class Unit {
    enum Type { WALL, ELF, GOBLIN }

    final Type type;
    int hitPoints = 200;

    Unit(Type type) {
      this.type = type;
    }
  }
}
