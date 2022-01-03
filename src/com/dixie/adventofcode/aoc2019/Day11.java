package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;
import com.dixie.adventofcode.lib.StreamUtils;

import java.awt.*;
import java.util.List;

public class Day11 extends Day {
  public static void main(String[] args) {
    new Day11().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    long[] program = StreamUtils.streamLongs(lines.get(0), ",").toArray();
    Robot robot = new Robot();
    Space2D<Boolean> panels = new Space2D<>();
    int panelsPainted = 0;
    return panelsPainted;
  }

  private static class Robot {
    Point location = new Point(0, 0);
    Direction direction = Direction.NORTH;

    void moveForward() {
      location.translate(direction.dx, direction.dy);
    }
  }
}
