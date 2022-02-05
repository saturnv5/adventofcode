package com.dixie.adventofcode.lib;

import com.google.common.collect.ImmutableList;

import java.awt.*;

public enum Direction {
  NORTH(0, -1),
  NORTH_EAST(1, -1),
  EAST(1, 0),
  SOUTH_EAST(1, 1),
  SOUTH(0, 1),
  SOUTH_WEST(-1, 1),
  WEST(-1, 0),
  NORTH_WEST(-1, -1);

  public static final Direction UP = NORTH, DOWN = SOUTH, LEFT = WEST, RIGHT = EAST;

  public static final ImmutableList<Direction> CARDINALS =
      ImmutableList.of(NORTH, EAST, SOUTH, WEST);
  public static final ImmutableList<Direction> DIAGONALS =
      ImmutableList.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);

  public final int dx, dy;

  Direction(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public Direction turnLeft() {
    int left = Math.floorMod(ordinal() - 2, Direction.values().length);
    return Direction.values()[left];
  }

  public Direction turnRight() {
    int right = Math.floorMod(ordinal() + 2, Direction.values().length);
    return Direction.values()[right];
  }

  public Direction turnBack() {
    int back = Math.floorMod(ordinal() + 4, Direction.values().length);
    return Direction.values()[back];
  }

  public Point apply(Point from) {
    return new Point(from.x + dx, from.y + dy);
  }
}
