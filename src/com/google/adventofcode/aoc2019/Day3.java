package com.google.adventofcode.aoc2019;

import com.google.adventofcode.lib.Day;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Day3 extends Day {
  public static void main(String[] args) {
    new Day3().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    HashMap<Point, List<Wire>> occupied = new HashMap<>();
    lines.forEach(line -> layWire(line.split(","), occupied));
    return intersections(occupied)
            .map(Map.Entry::getKey)
            .mapToInt(p -> Math.abs(p.x) + Math.abs(p.y))
            .min()
            .getAsInt();
  }

  @Override
  protected long part2(List<String> lines) {
    HashMap<Point, List<Wire>> occupied = new HashMap<>();
    lines.stream().forEach(line -> layWire(line.split(","), occupied));
    return intersections(occupied)
            .mapToInt(e -> sumSteps(e.getKey(), e.getValue()))
            .min()
            .getAsInt();
  }

  private static Stream<Map.Entry<Point, List<Wire>>> intersections(HashMap<Point, List<Wire>> occupied) {
    return occupied.entrySet()
            .stream()
            .filter(e -> e.getValue().size() > 1);
  }

  private void layWire(String[] moves, HashMap<Point, List<Wire>> occupied) {
    Wire wire = new Wire();
    Point p = new Point(0, 0);
    int dist = 0;
    for (String move : moves) {
      char dir = move.charAt(0);
      int amount = Integer.parseInt(move.substring(1));
      for (int i = 0; i < amount; i++) {
        switch (dir) {
          case 'U' -> p = new Point(p.x, p.y - 1);
          case 'D' -> p = new Point(p.x, p.y + 1);
          case 'L' -> p = new Point(p.x - 1, p.y);
          case 'R' -> p = new Point(p.x + 1, p.y);
        }
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
