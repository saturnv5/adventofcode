package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Day17 extends Day {
  public static void main(String[] args) {
    new Day17().solve();
  }

  private static final HashMap<Character, Direction> DIRECTIONS = new HashMap<>();
  static {
    DIRECTIONS.put('^', Direction.NORTH);
    DIRECTIONS.put('>', Direction.EAST);
    DIRECTIONS.put('v', Direction.SOUTH);
    DIRECTIONS.put('<', Direction.WEST);
  }
  private static final char SCAFFOLD = '#';
  private static final char EMPTY = '.';

  @Override
  protected long part1(List<String> lines) {
    Intcode ic = new Intcode((lines.get(0)));
    Space2D<Character> space = new Space2D<>();
    Long next;
    Point cur = new Point();
    while ((next = ic.executeUntilOutput()) != null) {
      char ch = (char) next.intValue();
      if (ch == '\n') {
        cur.x = 0;
        cur.y++;
      } else {
        space.setValueAt(cur, ch);
        cur.x++;
      }
    }
    System.out.println(space.toPrintableImage(String::valueOf));
    return space.streamAllPoints()
        .filter(p -> isIntersection(space, p))
        .mapToInt(p -> p.x * p.y)
        .sum();
  }

  private static boolean isIntersection(Space2D<Character> space, Point p) {
    if (isEmpty(space.getValueAt(p))) {
      return false;
    }
    for (Direction dir : Direction.values()) {
      if (isEmpty(space.getValueAt(p.x + dir.dx, p.y + dir.dy))) {
        return false;
      }
    }
    return true;
  }

  private static boolean isEmpty(Character ch) {
    return ch == null || ch == EMPTY;
  }
}
