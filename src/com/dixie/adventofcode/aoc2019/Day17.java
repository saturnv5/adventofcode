package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day17 extends Day {
  public static void main(String[] args) {
    new Day17().solve();
  }

  private static final Map<Character, Direction> DIRECTIONS = Map.of(
      '^', Direction.NORTH,
      '>', Direction.EAST,
      'v', Direction.SOUTH,
      '<', Direction.WEST);

  private static final char EMPTY = '.';
  private static final int MAX_WIRE_LENGTH = 20;

  private Space2D<Character> space;

  @Override
  protected void prepare(List<String> lines) {
    space = constructSpace(lines.get(0));
  }

  @Override
  protected Object part1(List<String> lines) {
    System.out.println(space.toPrintableImage(String::valueOf));
    return space.streamAllPoints()
        .filter(p -> isIntersection(p))
        .mapToInt(p -> p.x * p.y)
        .sum();
  }

  @Override
  protected Object part2(List<String> lines) {
    List<String> movementCommands = computeMovementCommand();
    String input = computeRoutineInput(movementCommands);
    long[] program = StreamUtils.streamLongs(lines.get(0), ",").toArray();
    program[0] = 2;
    return new Intcode(program).executeUntilEnd(input.chars().mapToLong(c -> c).toArray());
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

  private List<String> computeMovementCommand() {
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

  private static String computeRoutineInput(List<String> movementCommands) {
    List<String> patternA = new ArrayList<>();
    List<String> patternB = new ArrayList<>();
    List<String> patternC = new ArrayList<>();
    List<String> mainFunction = findPatterns(movementCommands, patternA, patternB, patternC);
    return String.join("\n",
        String.join(",", mainFunction),
        String.join(",", patternA),
        String.join(",", patternB),
        String.join(",", patternC)) + "\nn\n";
  }

  private static List<String> findPatterns(List<String> remainingCommands, List<String> patternA,
      List<String> patternB, List<String> patternC) {
    List<String> pattern = patternA.isEmpty() ? patternA : patternB.isEmpty() ? patternB : patternC;
    String function = patternA.isEmpty() ? "A" : patternB.isEmpty() ? "B" : "C";
    Pair<Integer, Integer> indices = maxPatternIndices(remainingCommands);
    for (int i = indices.first; i < indices.second; i += 2) {
      pattern.add(remainingCommands.get(i));
      pattern.add(remainingCommands.get(i + 1));
      List<String> compressed = applyPattern(remainingCommands, pattern, function);
      if (pattern == patternC) {
        if (maxPatternIndices(compressed).first == compressed.size()) {
          // Found last pattern that fully cover the original command list.
          return compressed;
        }
      } else {
        compressed = findPatterns(compressed, patternA, patternB, patternC);
        if (compressed != null) {
          return compressed;
        }
      }
    }
    pattern.clear();
    return null;
  }

  private static Pair<Integer, Integer> maxPatternIndices(List<String> commands) {
    int from = 0;
    while (from < commands.size()) {
      String cmd = commands.get(from);
      if (cmd.equals("L") || cmd.equals("R")) {
        break;
      }
      from++;
    }
    int to = from;
    int size = 0;
    while (to < commands.size() && size < MAX_WIRE_LENGTH) {
      String cmd = commands.get(from);
      if (cmd.equals("A") || cmd.equals("B") || cmd.equals("C")) {
        break;
      }
      to++;
      size += cmd.length() + 1;
    }
    return Pair.of(from, to);
  }

  private static List<String> applyPattern(
      List<String> commands, List<String> pattern, String function) {
    List<String> compressed = new ArrayList<>(commands);
    for (int i = 0; i <= compressed.size() - pattern.size(); i++) {
      List<String> sub = compressed.subList(i, i + pattern.size());
      if (pattern.equals(sub)) {
        sub.subList(1, sub.size()).clear();
        sub.set(0, function);
      }
    }
    return compressed;
  }

  private boolean isIntersection(Point p) {
    if (isEmpty(space.getValueAt(p))) {
      return false;
    }
    for (Direction dir : Direction.CARDINALS) {
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
