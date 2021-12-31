package com.google.adventofcode.aoc2019;

import com.google.adventofcode.lib.Day;
import com.google.adventofcode.lib.Memoizer;
import com.google.common.math.IntMath;

import java.awt.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class Day10 extends Day {
  public static void main(String[] args) {
    new Day10().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    int width = lines.get(0).length();
    int height = lines.size();
    HashSet<Point> asteroids = parseAsteroids(lines);
    return asteroids.stream()
            .mapToInt(asteroid -> computeVisible(asteroids, asteroid, width, height).size())
            .max()
            .getAsInt();
  }

  @Override
  protected long part2(List<String> lines) {
    int width = lines.get(0).length();
    int height = lines.size();
    HashSet<Point> asteroids = parseAsteroids(lines);
    Point station = asteroids.stream()
            .max(Comparator.comparing(
                    Memoizer.memoize(a -> computeVisible(asteroids, a, width, height).size())))
            .get();
    asteroids.remove(station);
    int asteroidsDestroyed = 0;
    while (asteroidsDestroyed < 200) {
      List<Point> toHit = computeVisible(asteroids, station, width, height).stream()
              .sorted(Comparator.comparing(Memoizer.memoize(a -> angle(station, a))))
              .toList();
      if (asteroidsDestroyed + toHit.size() < 200) {
        toHit.forEach(asteroids::remove);
        asteroidsDestroyed += toHit.size();
      } else {
        Point hit200 = toHit.get(200 - asteroidsDestroyed - 1);
        return hit200.x * 100 + hit200.y;
      }
    }
    return -1;
  }

  private static HashSet<Point> parseAsteroids(List<String> lines) {
    HashSet<Point> asteroids = new HashSet<>();
    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        if (line.charAt(x) == '#') {
          asteroids.add(new Point(x, y));
        }
      }
    }
    return asteroids;
  }

  private static HashSet<Point> computeVisible(HashSet<Point> asteroids, Point station, int width, int height) {
    HashSet<Point> visibleAsteroids = new HashSet<>(asteroids);
    visibleAsteroids.remove(station);
    asteroids.stream()
            .filter(visibleAsteroids::contains)
            .forEach(asteroid -> {
              int dx = asteroid.x - station.x;
              int dy = asteroid.y - station.y;
              int gcd = IntMath.gcd(Math.abs(dx), Math.abs(dy));
              dx /= gcd;
              dy /= gcd;
              Point shadow = new Point(asteroid.x + dx, asteroid.y + dy);
              while (shadow.x >= 0 && shadow.x < width && shadow.y >= 0 && shadow.y < height) {
                visibleAsteroids.remove(shadow);
                shadow.translate(dx, dy);
              }
            });
    return visibleAsteroids;
  }

  private static double angle(Point a, Point b) {
    double angle = Math.atan2(b.x - a.x, a.y - b.y);
    if (angle < 0) {
      angle += Math.PI * 2;
    }
    return angle;
  }
}
