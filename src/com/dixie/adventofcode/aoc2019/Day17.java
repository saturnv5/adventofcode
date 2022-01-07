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
    List<Direction> moves = computeMovements(space);
    List<String> movementList = convertToMovementList(moves);
    System.out.println(movementList);
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

  private static List<String> convertToMovementList(List<Direction> moves) {
    List<String> movementList = new ArrayList<>();
    Direction direction = Direction.NORTH;
    int distMoved = 0;
    for (Direction move : moves) {
      if (move != direction) {
        if (distMoved > 0) {
          movementList.add(String.valueOf(distMoved));
        }
        if (direction.turnLeft() == move) {
          movementList.add("L");
        } else if (direction.turnRight() == move) {
          movementList.add("R");
        } else {
          movementList.add("L");
          movementList.add("L");
        }
        direction = move;
        distMoved = 1;
      } else {
        distMoved++;
      }
    }
    movementList.add(String.valueOf(distMoved));
    return movementList;
  }

  private static List<Direction> computeMovements(Space2D<Character> space) {
    Point loc = space.streamAllPoints()
        .filter(p -> DIRECTIONS.containsKey(space.getValueAt(p)))
        .findFirst()
        .get();
    List<Direction> moves = new ArrayList<>();

    exploreScaffolding(space, new HashSet<>(), loc, moves);

    return moves;
  }

  private static void exploreScaffolding(
      Space2D<Character> space, Set<Point> visited, Point loc, List<Direction> moves) {
    if (!visited.add(loc)) {
      return;
    }
    for (Direction dir : Direction.values()) {
      Point newLoc = dir.apply(loc);
      if (visited.contains(newLoc)) {
        continue;
      }
      if (isEmpty(space.getValueAt(newLoc))) {
        continue;
      }
      moves.add(dir);
      exploreScaffolding(space, visited, newLoc, moves);
      moves.add(dir.turnBack());
    }
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
