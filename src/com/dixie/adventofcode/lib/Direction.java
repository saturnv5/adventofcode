package com.dixie.adventofcode.lib;

public enum Direction {
  NORTH(0, -1),
  EAST(1, 0),
  SOUTH(0, 1),
  WEST(-1, 0);

  public final int dx, dy;

  Direction(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

  public Direction turnLeft() {
    int left = Math.floorMod(ordinal() - 1, Direction.values().length);
    return Direction.values()[left];
  }

  public Direction turnRight() {
    int right = Math.floorMod(ordinal() + 1, Direction.values().length);
    return Direction.values()[right];
  }

  public Direction turnBack() {
    int back = Math.floorMod(ordinal() + 2, Direction.values().length);
    return Direction.values()[back];
  }
}
