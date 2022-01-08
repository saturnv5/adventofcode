package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.GraphUtils;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.base.Predicates;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

public class Day18 extends Day {
  public static void main(String[] args) {
    new Day18().solve();
  }

  private static final char ENTRANCE = '@';
  private static final char WALL = '#';

  private Space2D<Character> space;
  private long numKeys;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    space = Space2D.parseFromStrings(lines, c -> (char) c);
    numKeys = space.streamAllPoints().map(space::getValueAt).filter(Day18::isKey).count();
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    Point start = space.streamOccurrencesOf(ENTRANCE).findFirst().get();
    return GraphUtils.<State>shortestNonWeightedPath(this::validMoves,
        new State(start, new HashSet<>()), s -> s.unlocked.size() == numKeys).getCost();
  }

  Stream<State> validMoves(State state) {
    return Direction.CARDINALS.stream().map(dir -> {
      Point newLocation = dir.apply(state.location);
      Character cell = space.getValueAt(newLocation);
      if (cell == WALL) {
        return null;
      }
      if (isDoor(cell) && !state.unlocked.contains(cell)) {
        return null; // Door still locked.
      }
      if (isKey(cell)) {
        State s = new State(newLocation, new HashSet<>(state.unlocked));
        s.unlocked.add(Character.toUpperCase(cell));
        return s;
      }
      return new State(newLocation, state.unlocked);
    }).filter(Predicates.notNull());
  }

  static boolean isKey(char cell) {
    return cell >= 'a' && cell <= 'z';
  }

  static boolean isDoor(char cell) {
    return cell >= 'A' && cell <= 'Z';
  }

  private static class State {
    final Point location;
    final Set<Character> unlocked;

    public State(Point location, Set<Character> unlocked) {
      this.location = location;
      this.unlocked = unlocked;
    }

    @Override
    public boolean equals(Object o) {
      State state = (State) o;
      return location.equals(state.location) && unlocked.equals(state.unlocked);
    }

    @Override
    public int hashCode() {
      return Objects.hash(location, unlocked);
    }
  }
}
