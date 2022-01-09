package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.GraphUtils;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.base.Predicates;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Day20 extends Day {
  public static void main(String[] args) {
    new Day20().solve();
  }

  private static final char EMPTY = '.';

  Space2D<Character> space;
  HashMap<Point, Point> portals;
  HashMap<String, Point> namedPortals;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    constructSpaceAndPortals(lines);
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    return GraphUtils.shortestNonWeightedPath(
        this::successors, namedPortals.get("AA"), namedPortals.get("ZZ")).getCost();
  }

  @Override
  protected long part2(List<String> lines) {
    return GraphUtils.shortestNonWeightedPath(this::successors,
        new State(namedPortals.get("AA"), 0),
        new State(namedPortals.get("ZZ"), 0)).getCost();
  }

  private void constructSpaceAndPortals(List<String> lines) {
    space = Space2D.parseFromStrings(lines);
    portals = new HashMap<>();
    namedPortals = new HashMap<>();
    space.streamAllPoints().filter(this::isPortal).forEach(p -> {
      Direction dir = getEmptyDirection(p).orElse(null);
      if (dir != null) {
        String portal = portalName(p, dir.turnBack().apply(p));
        Point p1 = dir.apply(p);
        if (namedPortals.containsKey(portal)) {
          Point p2 = namedPortals.get(portal);
          portals.put(p1, p2);
          portals.put(p2, p1);
        } else {
          namedPortals.put(portal, p1);
        }
      }
    });
  }

  private Stream<Point> successors(Point p) {
    if (portals.containsKey(p)) {
      return Stream.concat(Stream.of(portals.get(p)),
          Direction.CARDINALS.stream().map(dir -> dir.apply(p)).filter(this::isEmpty));
    } else {
      return Direction.CARDINALS.stream().map(dir -> dir.apply(p)).filter(this::isEmpty);
    }
  }

  private Stream<State> successors(State state) {
    return successors(state.location).map(p -> {
      if (isInnerPortal(p)) {
        if (state.depth == 0) {
          return null;
        }
        return new State(p, state.depth - 1);
      } else if (isOuterPortal(p)) {
        return new State(p, state.depth + 1);
      } else {
        return new State(p, state.depth);
      }
    }).filter(Predicates.notNull());
  }

  private Optional<Direction> getEmptyDirection(Point p) {
    return Direction.CARDINALS.stream()
        .filter(dir -> space.getValueAt(dir.apply(p), '\0') == EMPTY)
        .findFirst();
  }

  private boolean isPortal(Point p) {
    char ch = space.getValueAt(p, '\0');
    return ch >= 'A' && ch <= 'Z';
  }

  private boolean isEmpty(Point p) {
    return space.getValueAt(p, '\0') == EMPTY;
  }

  private String portalName(Point a, Point b) {
    if (a.x < b.x || a.y < b.y) {
      return space.getValueAt(a) + "" + space.getValueAt(b);
    } else {
      return space.getValueAt(b) + "" + space.getValueAt(a);
    }
  }

  private boolean isOuterPortal(Point p) {
    if (portals.containsKey(p)) {
      return p.x == 2 || p.x == space.getBounds().getWidth() - 3 || p.y == 2 ||
          p.y == space.getBounds().getHeight() - 3;
    }
    return false;
  }

  private boolean isInnerPortal(Point p) {
    if (portals.containsKey(p)) {
      return !isOuterPortal(p);
    }
    return false;
  }

  private record State(Point location, int depth) {}
}
