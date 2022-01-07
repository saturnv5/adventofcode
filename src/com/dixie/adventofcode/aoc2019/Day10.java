package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.math.IntMath;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Day10 extends Day {
  public static void main(String[] args) {
    new Day10().solve();
  }

  private Space2D<Boolean> space;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    space = Space2D.parseFromStrings(lines, c -> c == '#' ? true : null);
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    return space.streamAllPoints()
            .mapToLong(asteroid -> computeVisible(asteroid).streamAllPoints().count())
            .max()
            .getAsLong();
  }

  @Override
  protected long part2(List<String> lines) {
    Point station = space.streamAllPoints()
            .max(Comparator.comparing(
                    Memoizer.memoize(a -> computeVisible(a).streamAllPoints().count())))
            .get();
    space.removeValueAt(station);
    int asteroidsDestroyed = 0;
    while (asteroidsDestroyed < 200) {
      List<Point> toHit = computeVisible(station).streamAllPoints()
              .sorted(Comparator.comparing(Memoizer.memoize(a -> angle(station, a))))
              .toList();
      if (asteroidsDestroyed + toHit.size() < 200) {
        toHit.forEach(space::removeValueAt);
        asteroidsDestroyed += toHit.size();
      } else {
        Point hit200 = toHit.get(200 - asteroidsDestroyed - 1);
        return hit200.x * 100 + hit200.y;
      }
    }
    return -1;
  }

  private Space2D<Boolean> computeVisible(Point station) {
    Space2D<Boolean> visibleAsteroids = Space2D.copyOf(space);
    visibleAsteroids.removeValueAt(station);
    space.streamAllPoints()
            .filter(visibleAsteroids::hasValueAt)
            .forEach(asteroid -> {
              int dx = asteroid.x - station.x;
              int dy = asteroid.y - station.y;
              int gcd = IntMath.gcd(Math.abs(dx), Math.abs(dy));
              dx /= gcd;
              dy /= gcd;
              Point shadow = new Point(asteroid.x + dx, asteroid.y + dy);
              while (shadow.x >= 0 && shadow.x < space.getBounds().getWidth()
                      && shadow.y >= 0 && shadow.y < space.getBounds().getWidth()) {
                visibleAsteroids.removeValueAt(shadow);
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
