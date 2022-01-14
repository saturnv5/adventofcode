package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.*;
import com.google.common.base.Predicates;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day18 extends Day {
  public static void main(String[] args) {
    new Day18().solve();
  }

  private static final char ENTRANCE = '@';
  private static final char WALL = '#';

  private Space2D<Character> space;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    space = Space2D.parseFromStrings(lines, c -> (char) c);
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    Point start = space.streamOccurrencesOf(ENTRANCE).findFirst().get();
    Set<Character> lockedDoors = getDoors(Predicates.alwaysTrue());
    return GraphUtils.<State>shortestNonWeightedPath(this::stateSuccessor,
        new State(start, lockedDoors), s -> s.lockedDoors.isEmpty()).getCost();
  }

  @Override
  protected Object part2(List<String> lines) {
    Point center = space.streamOccurrencesOf(ENTRANCE).findFirst().get();
    splitIntoQuadrants(space, center);
    List<State> states = Direction.DIAGONALS.stream()
        .map(dir -> new State(dir.apply(center), getDoors(
            p -> Integer.signum(p.x - center.x) == dir.dx &&
                Integer.signum(p.y - center.y) == dir.dy)))
        .toList();
    QuadState startState = new QuadState(states, new HashSet<>());
    long numKeys = space.streamAllPoints().map(space::getValueAt).filter(Day18::isKey).count();
    return GraphUtils.shortestNonWeightedPath(
        this::stateSuccessor, startState, new QuadStateVisitor(),
        ss -> ss.unlockedDoors.size() == numKeys).getCost();
  }

  private static void splitIntoQuadrants(Space2D<Character> space, Point center) {
    space.setValueAt(center, WALL);
    Direction.CARDINALS.forEach(dir -> space.setValueAt(dir.apply(center), WALL));
    Direction.DIAGONALS.forEach(dir -> space.setValueAt(dir.apply(center), ENTRANCE));
  }

  private Set<Character> getDoors(Predicate<Point> filter) {
    return space.streamAllPoints()
        .filter(filter::test)
        .map(space::getValueAt)
        .filter(Day18::isDoor)
        .collect(Collectors.toSet());
  }

  private Stream<State> stateSuccessor(State state) {
    return validMoves(state).map(m -> state.move(m.first, m.second));
  }

  Stream<QuadState> stateSuccessor(QuadState state) {
    return IntStream.range(0, 4)
        .boxed()
        .flatMap(i -> validMoves(state.states.get(i)).map(m -> state.move(i, m.first, m.second)));
  }

  Stream<Pair<Point, Character>> validMoves(State state) {
    return Direction.CARDINALS.stream().map(dir -> {
      Point newLocation = dir.apply(state.location);
      Character cell = space.getValueAt(newLocation);
      if (cell == WALL) {
        return null;
      }
      if (isDoor(cell) && state.lockedDoors.contains(cell)) {
        return null; // Door still locked.
      }
      if (isKey(cell)) {
        Character door = Character.toUpperCase(cell);
        return Pair.of(newLocation, door);
      }
      return Pair.of(newLocation, (Character) null);
    }).filter(Predicates.notNull());
  }

  static boolean isKey(char cell) {
    return cell >= 'a' && cell <= 'z';
  }

  static boolean isDoor(char cell) {
    return cell >= 'A' && cell <= 'Z';
  }

  private record State(Point location, Set<Character> lockedDoors) {

    public State move(Point location, Character unlockedDoor) {
      if (unlockedDoor == null) {
        return new State(location, lockedDoors);
      }
      State state = new State(location, new HashSet<>(lockedDoors));
      state.lockedDoors.remove(unlockedDoor);
      return state;
    }

    public State unlockDoor(Character unlockedDoor) {
      if (unlockedDoor == null || !lockedDoors.contains(unlockedDoor)) {
        return this;
      }
      State state = new State(location, new HashSet<>(lockedDoors));
      state.lockedDoors.remove(unlockedDoor);
      return state;
    }
  }

  private record QuadState(List<State> states, Set<Character> unlockedDoors) {

    public QuadState move(int locationIndex, Point location, Character unlockedDoor) {
      List<State> newStates = new ArrayList<>(states.size());
      for (int i = 0; i < states.size(); i++) {
        if (i == locationIndex) {
          newStates.add(states.get(i).move(location, unlockedDoor));
        } else {
          newStates.add(states.get(i).unlockDoor(unlockedDoor));
        }
      }
      if (unlockedDoor == null || unlockedDoors.contains(unlockedDoor)) {
        return new QuadState(newStates, unlockedDoors);
      } else {
        QuadState state = new QuadState(newStates, new HashSet<>(unlockedDoors));
        state.unlockedDoors.add(unlockedDoor);
        return state;
      }
    }
  }

  private static class QuadStateVisitor implements Visitor<QuadState> {
    private final Map<Set<Character>, Set<State>> visited = new HashMap<>();

    @Override
    public boolean hasVisited(QuadState quadState) {
      return visited.computeIfAbsent(quadState.unlockedDoors, ul -> new HashSet<>())
          .containsAll(quadState.states);
    }

    @Override
    public boolean visit(QuadState quadState) {
      return visited.computeIfAbsent(quadState.unlockedDoors, ul -> new HashSet<>())
          .addAll(quadState.states);
    }
  }
}
