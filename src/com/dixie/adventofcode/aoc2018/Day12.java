package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Pair;
import com.dixie.adventofcode.lib.Space2D;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 extends Day {
  public static void main(String[] args) {
    new Day12().solve();
  }

  private static final long FIFTY_BILLION = 50_000_000_000L;

  private Space2D<Boolean> state;
  private Map<List<Boolean>, Boolean> notes;

  @Override
  protected void prepare(List<String> lines) {
    notes = lines.subList(2, lines.size())
        .stream()
        .map(Day12::parseNote)
        .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    state = Space2D.parseFromStrings(List.of(lines.get(0).substring(15)), ch -> ch == '#');
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    IntStream.range(0, 20).forEach(i -> spread());
    return currentSum();
  }

  @Override
  protected Object part2(List<String> lines) {
    long steps = 0;
    int lastSum = currentSum();
    int lastDelta = 0;
    int sameDeltas = 0;
    while (steps < FIFTY_BILLION && sameDeltas < 10) {
      // If we get same delta 10 times, that's good enough to project forwards to 50 billion.
      spread();
      steps++;
      int delta = currentSum() - lastSum;
      if (lastDelta == delta) {
        sameDeltas++;
      } else {
        lastDelta = delta;
        sameDeltas = 0;
      }
      lastSum += delta;
    }
    return lastSum + lastDelta * (FIFTY_BILLION - steps);
  }

  private void spread() {
    Rectangle bounds = state.getBounds();
    Space2D<Boolean> newState = Space2D.copyOf(state);
    IntStream.range(bounds.x - 2, bounds.x + bounds.width + 3).forEach(i -> {
      List<Boolean> surrounds =
          IntStream.range(i - 2, i + 3).mapToObj(x -> state.getValueAt(x, 0, false)).toList();
      boolean newValue = notes.getOrDefault(surrounds, false);
      if (newValue != state.getValueAt(i, 0, false)) {
        newState.setValueAt(i, 0, notes.getOrDefault(surrounds, false));
      }
    });
    state = newState;
  }

  private int currentSum() {
    return state.streamAllPoints().filter(state::getValueAt).mapToInt(p -> p.x).sum();
  }

  private static Pair<List<Boolean>, Boolean> parseNote(String note) {
    return Pair.of(note.chars().limit(5).mapToObj(ch -> ch == '#').toList(), note.charAt(9) == '#');
  }
}
