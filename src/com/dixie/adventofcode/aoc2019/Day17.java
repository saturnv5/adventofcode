package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.*;
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
    Space2D<Character> space = constructSpace(lines.get(0));
    System.out.println(space.toPrintableImage(String::valueOf));
    return space.streamAllPoints()
        .filter(p -> isIntersection(space, p))
        .mapToInt(p -> p.x * p.y)
        .sum();
  }

  @Override
  protected long part2(List<String> lines) {
    Space2D<Character> space = constructSpace(lines.get(0));
    List<String> movementCommands = computeMovementCommand(space);
    System.out.println(movementCommands);
    System.out.println("Prefix: " + longestRepeatingPrefix(movementCommands));
    return super.part2(lines);
  }

  private static Space2D<Character> constructSpace(String line) {
    Intcode ic = new Intcode(line);
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
    return space;
  }

  private static List<String> computeMovementCommand(Space2D<Character> space) {
    List<String> movementCommands = new ArrayList<>();
    Point loc = space.streamAllPoints()
        .filter(p -> DIRECTIONS.containsKey(space.getValueAt(p)))
        .findFirst()
        .get();
    Direction dir = DIRECTIONS.get(space.getValueAt(loc));
    int dist = 0;
    while (true) {
      Point newLoc = dir.apply(loc); // Try forwards.
      if (isEmpty(space.getValueAt(newLoc))) {
        if (dist > 0) {
          movementCommands.add(String.valueOf(dist));
        }
        newLoc = dir.turnLeft().apply(loc); // Try left.
        if (isEmpty(space.getValueAt(newLoc))) {
          newLoc = dir.turnRight().apply(loc); // Try right.
          if (isEmpty(space.getValueAt(newLoc))) {
            return movementCommands; // Dead end.
          } else {
            movementCommands.add("R");
            dir = dir.turnRight();
          }
        } else {
          movementCommands.add("L");
          dir = dir.turnLeft();
        }
        dist = 1;
      } else {
        dist++;
      }
      loc = newLoc;
    }
  }

  private static List<String> longestRepeatingPrefix(List<String> sequence) {
    List<String> prefix = new ArrayList<>();
    while (prefix.size() < sequence.size()) {
      prefix.add(sequence.get(prefix.size()));
      boolean match = false;
      for (int i = prefix.size(); i < sequence.size() - prefix.size() + 1; i++) {
        if (prefix.equals(sequence.subList(i, i + prefix.size()))) {
          match = true;
          break;
        }
      }
      if (!match) {
        return prefix.subList(0, prefix.size() - 1);
      }
    }
    return prefix;
  }

  private static boolean isIntersection(Space2D<Character> space, Point p) {
    if (isEmpty(space.getValueAt(p))) {
      return false;
    }
    for (Direction dir : Direction.values()) {
      if (isEmpty(space.getValueAt(dir.apply(p)))) {
        return false;
      }
    }
    return true;
  }

  private static boolean isEmpty(Character ch) {
    return ch == null || ch == EMPTY;
  }
}
