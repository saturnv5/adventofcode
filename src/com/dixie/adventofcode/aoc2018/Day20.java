package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.GraphUtils;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.OptionalInt;

public class Day20 extends Day {
  public static void main(String[] args) {
    new Day20().solve();
  }

  private static final Point START = new Point(0, 0);

  private final MutableGraph<Point> rooms = GraphBuilder.undirected().build();
  private String regex;

  @Override
  protected void prepare(List<String> lines) {
    regex = lines.get(0).substring(1, lines.get(0).length() - 1);
    Deque<Point> branchedRooms = new ArrayDeque<>();
    branchedRooms.push(START);
    walk(branchedRooms, START, 0);
  }

  @Override
  protected Object part1(List<String> lines) {
    return GraphUtils.longestBfsPath(p -> rooms.successors(p).stream(), START).getCost();
  }

  private void walk(Deque<Point> branchedRooms, Point currentRoom, int index) {
    if (branchedRooms.isEmpty()) return;
    // Progress along linear path first.
    for (; index < regex.length(); index++) {
      Direction dir = parseDirection(regex.charAt(index));
      if (dir == null) break;
      Point next = dir.apply(currentRoom);
      rooms.putEdge(currentRoom, next);
      currentRoom = next;
    }
    if (index == regex.length()) return;
    char ch = regex.charAt(index);
    if (ch == '|') {
      // Continue along current branch, if there is more.
      OptionalInt nextIndex = findClosingBracket(index + 1);
      if (nextIndex.isPresent()) {
        Point thisBranch = branchedRooms.pop();
        walk(branchedRooms, currentRoom, nextIndex.getAsInt() + 1);
        branchedRooms.push(thisBranch);
      }
      // Next branch, restarting at previous branching room.
      walk(branchedRooms, branchedRooms.peek(), index + 1);
      return;
    } else if (ch == '(') {
      branchedRooms.push(currentRoom);
    } else if (ch == ')') {
      branchedRooms.pop();
    }
    walk(branchedRooms, currentRoom, index + 1);
  }

  private OptionalInt findClosingBracket(int fromIndex) {
    int depth = 1;
    for (int index = fromIndex; index < regex.length(); index++) {
      char ch = regex.charAt(index);
      if (ch == '(') {
        depth++;
      } else if (ch == ')') {
        depth--;
        if (depth == 0) {
          return OptionalInt.of(index);
        }
      }
    }
    return OptionalInt.empty();
  }

  private static Direction parseDirection(char ch) {
    return switch (ch) {
      case 'N' -> Direction.NORTH;
      case 'E' -> Direction.EAST;
      case 'S' -> Direction.SOUTH;
      case 'W' -> Direction.WEST;
      default -> null;
    };
  }
}
