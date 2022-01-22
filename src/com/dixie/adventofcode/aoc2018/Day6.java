package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day6 extends Day {
  public static void main(String[] args) {
    new Day6().solve();
  }

  private Space2D<Point> space;
  private List<Point> places;

  @Override
  protected void prepare(List<String> lines) {
    space = new Space2D<>();
    places = lines.stream().map(Day6::toPoint).toList();
    places.forEach(p -> space.setValueAt(p, p));
  }

  @Override
  protected Object part1(List<String> lines) {
    space.streamAllPointsInBounds().forEach(p -> space.setValueAt(p, closest(p)));
    Set<Point> infiniteAreas = edges();
    return places.stream()
        .filter(Predicate.not(infiniteAreas::contains))
        .mapToLong(p -> space.streamOccurrencesOf(p).count())
        .max()
        .getAsLong();
  }

  @Override
  protected Object part2(List<String> lines) {
    return space.streamAllPointsInBounds().mapToInt(this::totalDist).filter(d -> d < 10000).count();
  }

  private Point closest(Point from) {
    Point closest = places.stream()
        .min(Comparator.comparingInt(p -> dist(from, p)))
        .get();
    Point secondClosest = places.stream()
        .filter(p -> p != closest)
        .min(Comparator.comparingInt(p -> dist(from, p)))
        .get();
    if (dist(from, closest) == dist(from, secondClosest)) {
      return null;
    }
    return closest;
  }

  private Set<Point> edges() {
    Rectangle bounds = space.getBounds();
    return space.streamAllPoints()
        .filter(p -> p.x == bounds.x || p.y == bounds.y || p.x == bounds.x + bounds.width - 1 ||
            p.y == bounds.y + bounds.height - 1)
        .map(p -> space.getValueAt(p))
        .collect(Collectors.toSet());
  }

  private int totalDist(Point from) {
    return places.stream().mapToInt(p -> dist(from, p)).sum();
  }

  private static int dist(Point p1, Point p2) {
    return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
  }

  private static Point toPoint(String line) {
    String[] coord = line.split(", ");
    return new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
  }
}
