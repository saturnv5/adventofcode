package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Day15 extends Day {
  public static void main(String[] args) {
    new Day15().solve();
  }

  private static final int DEFAULT_ATTACK_POWER = 3;
  private static final int MAX_HP = 200;
  private static final List<Direction> SUCCESSOR_DIRECTIONS =
      List.of(Direction.NORTH, Direction.WEST, Direction.EAST, Direction.SOUTH);

  private Space2D<Unit> space;
  private int numElves;
  private int numGoblins;
  private int elfAttackPower = DEFAULT_ATTACK_POWER;

  @Override
  protected Object part1(List<String> lines) {
    space = Space2D.parseFromStrings(lines, this::parseCell);
    numElves = (int) space.streamOccurrencesOf(u -> u.type == Unit.Type.ELF).count();
    numGoblins = (int) space.streamOccurrencesOf(u -> u.type == Unit.Type.GOBLIN).count();
    return simulateCombat();
  }

  @Override
  protected Object part2(List<String> lines) {
    while (true) {
      elfAttackPower++;
      initialise(lines);
      int numStartingElves = numElves;
      int outcome = simulateCombat();
      if (numElves == numStartingElves) {
        System.out.println("Min. elf attack power: " + elfAttackPower);
        return outcome;
      }
    }
  }

  private void initialise(List<String> lines) {
    space = Space2D.parseFromStrings(lines, this::parseCell);
    numElves = (int) space.streamOccurrencesOf(u -> u.type == Unit.Type.ELF).count();
    numGoblins = (int) space.streamOccurrencesOf(u -> u.type == Unit.Type.GOBLIN).count();
  }

  private int simulateCombat() {
    int rounds = 0;
    while (simulateRound()) {
      // printSpace();
      rounds++;
    }
    int totalHp = space.streamOccurrencesOf(u -> u.type != Unit.Type.WALL)
        .mapToInt(e -> e.getValue().hitPoints)
        .sum();
    return rounds * totalHp;
  }

  private Unit parseCell(int cell) {
    return switch (cell) {
      case '#' -> new Unit(Unit.Type.WALL, 0);
      case 'E' -> new Unit(Unit.Type.ELF, elfAttackPower);
      case 'G' -> new Unit(Unit.Type.GOBLIN, DEFAULT_ATTACK_POWER);
      default -> null;
    };
  }

  private boolean simulateRound() {
    HashSet<Unit> hasMoved = new HashSet<>(numElves + numGoblins);
    AtomicBoolean combatContinues = new AtomicBoolean(true);
    space.streamAllPointsInBounds().forEach(p -> {
      if (!combatContinues.get()) return;
      Unit unit = space.getValueAt(p);
      if (unit == null || unit.type == Unit.Type.WALL) {
        return;
      }
      if (numGoblins == 0 || numElves == 0) {
        combatContinues.set(false);
        return; // Combat has ended.
      }
      if (!hasMoved.add(unit)) {
        return;
      }
      combatContinues.set(simulateUnit(p, unit));
    });
    return combatContinues.get();
  }

  private boolean simulateUnit(Point location, Unit unit) {
    Pair<Point, Unit> adjEnemy = getAdjacentEnemy(location, unit);
    if (adjEnemy == null) {
      // Move first.
      Path<Point> path = findPathToEnemy(location, unit);
      if (path != null) {
        Point newLocation = path.getNodes().get(1);
        space.removeValueAt(location);
        space.setValueAt(newLocation, unit);
        adjEnemy = getAdjacentEnemy(newLocation, unit);
      }
    }
    if (adjEnemy != null) {
      Unit enemy = adjEnemy.second;
      enemy.hitPoints -= unit.attackPower;
      if (enemy.hitPoints <= 0) {
        space.removeValueAt(adjEnemy.first);
        if (enemy.type == Unit.Type.ELF) numElves--;
        else if (enemy.type == Unit.Type.GOBLIN) numGoblins--;
      }
    }
    return true;
  }

  private Path<Point> findPathToEnemy(Point location, Unit unit) {
    List<SearchNode<Point>> potentialPaths = new ArrayList<>();
    GraphUtils.breadthFirstTraversal(
        this::successors, location, new GraphUtils.DefaultVisitor<>(), n -> {}, n -> {
          Pair<Point, Unit> adjEnemy = getAdjacentEnemy(n.getNode(), unit);
          if (adjEnemy == null) return false;
          if (potentialPaths.isEmpty() || potentialPaths.get(0).getCost() == n.getCost()) {
            potentialPaths.add(n);
            return false;
          }
          return true;
    });
    if (potentialPaths.isEmpty()) {
      return null;
    }
    return potentialPaths.stream()
        .min(Comparator.<SearchNode<Point>>comparingInt(n -> n.getNode().y)
            .thenComparingInt(n -> n.getNode().x))
        .get()
        .constructPath();
  }

  private Pair<Point, Unit> getAdjacentEnemy(Point location, Unit unit) {
    return SUCCESSOR_DIRECTIONS.stream()
        .map(d -> d.apply(location))
        .map(p -> Pair.of(p, space.getValueAt(p)))
        .filter(
            e -> e.second != null && e.second.type != Unit.Type.WALL && e.second.type != unit.type)
        .min(Comparator.<Pair<Point, Unit>>comparingInt(e -> e.second.hitPoints)
            .thenComparingInt(e -> e.first.y)
            .thenComparingInt(e -> e.first.x)).orElse(null);
  }

  private Stream<Point> successors(Point from) {
    return SUCCESSOR_DIRECTIONS.stream()
        .map(d -> d.apply(from))
        .filter(p -> space.getValueAt(p) == null);
  }

  private void printSpace() {
    System.out.println(space.toPrintableImage(Day15::toString));
  }

  private static String toString(Unit unit) {
    if (unit == null) {
      return " ";
    }
    return switch (unit.type) {
      case WALL -> "█";
      case ELF -> "E";
      case GOBLIN -> "G";
    };
  }

  private static class Unit {
    enum Type { WALL, ELF, GOBLIN }

    final Type type;
    int hitPoints = MAX_HP;
    int attackPower;

    Unit(Type type, int attackPower) {
      this.type = type;
      this.attackPower = attackPower;
    }
  }
}
