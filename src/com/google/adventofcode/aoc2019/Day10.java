package com.google.adventofcode.aoc2019;

import com.google.adventofcode.lib.Day;
import com.google.adventofcode.lib.Memoizer;
import com.google.adventofcode.lib.Space2D;
import com.google.common.math.IntMath;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class Day10 extends Day {
  public static void main(String[] args) {
    new Day10().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    Space2D<Boolean> space = Space2D.parseFromStrings(lines, c -> c == '#' ? true : null);
    return space.streamAllPoints()
            .mapToLong(asteroid -> computeVisible(space, asteroid).streamAllPoints().count())
            .max()
            .getAsLong();
  }

  @Override
  protected long part2(List<String> lines) {
    Space2D<Boolean> space = Space2D.parseFromStrings(lines, c -> c == '#' ? true : null);
    Point station = space.streamAllPoints()
            .max(Comparator.comparing(
                    Memoizer.memoize(a -> computeVisible(space, a).streamAllPoints().count())))
            .get();
    space.removeValueAt(station);
    int asteroidsDestroyed = 0;
    while (asteroidsDestroyed < 200) {
      List<Point> toHit = computeVisible(space, station).streamAllPoints()
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

  private static Space2D<Boolean> computeVisible(Space2D<Boolean> space, Point station) {
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
              while (shadow.x >= 0 && shadow.x < space.getWidth()
                      && shadow.y >= 0 && shadow.y < space.getHeight()) {
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
