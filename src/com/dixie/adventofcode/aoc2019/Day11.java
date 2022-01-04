package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
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
    Robot robot = new Robot(program);
    Space2D<Integer> panels = new Space2D<>();

    while (robot.advance(panels)) ;

    return panels.streamAllPoints().count();
  }

  private static class Robot {
    private final Intcode program;
    private final Point location = new Point(0, 0);
    private Direction direction = Direction.NORTH;

    Robot(long[] program) {
      this.program = new Intcode(program);
    }

    boolean advance(Space2D<Integer> panels) {
      Long colourToPaint = program.executeUntilOutput(panels.getValueAt(location, 0));
      if (colourToPaint == null) {
        return false;
      }
      panels.setValueAt(location, colourToPaint.intValue());
      Long leftOrRight = program.executeUntilOutput(panels.getValueAt(location, 0));
      direction = leftOrRight == 0 ? direction.turnLeft() : direction.turnRight();
      location.translate(direction.dx, direction.dy);
      return true;
    }
  }
}
