package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Day3 extends Day {
  public static void main(String[] args) {
    new Day3().solve();
  }

  private static final Map<Character, Direction> DIRECTIONS = Map.of(
      'U', Direction.NORTH,
      'D', Direction.SOUTH,
      'L', Direction.WEST,
      'R', Direction.EAST);

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    HashMap<Point, List<Wire>> occupied = new HashMap<>();
    lines.forEach(line -> layWire(line.split(","), occupied));
    if (part1) {
      return intersections(occupied)
          .map(Map.Entry::getKey)
          .mapToInt(p -> Math.abs(p.x) + Math.abs(p.y))
          .min()
          .getAsInt();
    } else {
      return intersections(occupied)
          .mapToInt(e -> sumSteps(e.getKey(), e.getValue()))
          .min()
          .getAsInt();
    }
  }

  private static Stream<Map.Entry<Point, List<Wire>>> intersections(
      HashMap<Point, List<Wire>> occupied) {
    return occupied.entrySet()
            .stream()
            .filter(e -> e.getValue().size() > 1);
  }

  private void layWire(String[] moves, HashMap<Point, List<Wire>> occupied) {
    Wire wire = new Wire();
    Point p = new Point(0, 0);
    int dist = 0;
    for (String move : moves) {
      Direction dir = DIRECTIONS.get(move.charAt(0));
      int amount = Integer.parseInt(move.substring(1));
      for (int i = 0; i < amount; i++) {
        p = dir.apply(p);
        dist++;
        if (!wire.wirePoints.containsKey(p)) {
          wire.wirePoints.put(p, dist);
          occupied.computeIfAbsent(p, unused -> new ArrayList<>()).add(wire);
        }
      }
    }
  }

  private static class Wire {
    final HashMap<Point, Integer> wirePoints = new HashMap<>();
  }

  private static int sumSteps(Point p, List<Wire> wires) {
    return wires.stream().mapToInt(w -> w.wirePoints.get(p)).sum();
  }
}
