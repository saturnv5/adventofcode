package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.collect.Iterables;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day13 extends Day {
  public static void main(String[] args) {
    new Day13().solve();
  }

  private enum Track { VERTICAL, HORIZONTAL, ADJ_CORNER, OPP_CORNER, INTERSECTION }

  private static final Map<Character, Direction> CART_DIR = Map.of(
      '^', Direction.NORTH,
      'v', Direction.SOUTH,
      '<', Direction.WEST,
      '>', Direction.EAST);

  private Space2D<Track> tracks;
  private Map<Point, Cart> carts;

  @Override
  protected void prepare(List<String> lines) {
    tracks = new Space2D<>(lines.get(0).length(), lines.size());
    carts = new HashMap<>();
    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        char ch = line.charAt(x);
        tracks.setValueAt(x, y, parseTrack(ch));
        if (CART_DIR.containsKey(ch)) {
          carts.put(new Point(x, y), new Cart(new Point(x, y), CART_DIR.get(ch)));
        }
      }
    }
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    // System.out.println(tracks.toPrintableImage(Day13::print));
    Point firstCollision = null;
    while (true) {
      List<Cart> sortedCarts = carts.values()
          .stream()
          .sorted(
              Comparator.<Cart>comparingInt(c -> c.location.y).thenComparingInt(c -> c.location.x))
          .toList();
      for (Cart cart : sortedCarts) {
        if (!carts.containsKey(cart.location)) {
          continue;
        }
        carts.remove(cart.location);
        cart.moveStep();
        if (carts.containsKey(cart.location)) {
          carts.remove(cart.location);
          if (firstCollision == null) {
            firstCollision = cart.location;
          }
        } else {
          carts.put(new Point(cart.location), cart);
        }
      }
      if (part1 && firstCollision != null) {
        return firstCollision.x + "," + firstCollision.y;
      }
      if (carts.size() == 1) {
        Point last = Iterables.getOnlyElement(carts.keySet());
        return last.x + "," + last.y;
      }
    }
  }

  private static Track parseTrack(char track) {
    return switch (track) {
      case '^', 'v', '|' -> Track.VERTICAL;
      case '<', '>', '-' -> Track.HORIZONTAL;
      case '/' -> Track.ADJ_CORNER;
      case '\\' -> Track.OPP_CORNER;
      case '+' -> Track.INTERSECTION;
      default -> null;
    };
  }

  private static String print(Track track) {
    if (track == null) {
      return " ";
    }
    return switch (track) {
      case VERTICAL -> "|";
      case HORIZONTAL -> "-";
      case ADJ_CORNER -> "/";
      case OPP_CORNER -> "\\";
      case INTERSECTION -> "+";
    };
  }

  private class Cart {
    Point location;
    Direction direction;
    int nextTurn;

    Cart(Point location, Direction direction) {
      this.location = location;
      this.direction = direction;
    }

    void moveStep() {
      Track track = tracks.getValueAt(location);
      switch (track) {
        case ADJ_CORNER -> {
          switch (direction) {
            case NORTH, SOUTH -> direction = direction.turnRight();
            case WEST, EAST -> direction = direction.turnLeft();
          }
        }
        case OPP_CORNER -> {
          switch (direction) {
            case NORTH, SOUTH -> direction = direction.turnLeft();
            case WEST, EAST -> direction = direction.turnRight();
          }
        }
        case INTERSECTION -> {
          if (nextTurn == 0) direction = direction.turnLeft();
          else if (nextTurn == 2) direction = direction.turnRight();
          nextTurn = (nextTurn + 1) % 3;
        }
      }
      location.translate(direction.dx, direction.dy);
    }
  }
}
