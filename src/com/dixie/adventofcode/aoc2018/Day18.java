package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Direction;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Day18 extends Day {
  public static void main(String[] args) {
    new Day18().solve();
  }

  private static final Character TREES = '|', LUMBERYARD = '#';

  private Space2D<Character> land;

  @Override
  protected void prepare(List<String> lines) {
    land = Space2D.parseFromStrings(lines);
  }

  @Override
  protected Object part1(List<String> lines) {
    Space2D<Character> nextLand = land;
    for (int i = 0; i < 10; i++) nextLand = advanceTime(nextLand);
    return nextLand.streamOccurrencesOf(TREES).count() *
        nextLand.streamOccurrencesOf(LUMBERYARD).count();
  }

  private static final int TARGET_2 = 1_000_000_000;

  @Override
  protected Object part2(List<String> lines) {
    BiMap<Integer, Space2D<Character>> prevLands = HashBiMap.create();
    Space2D<Character> nextLand = land;
    for (int i = 0; i < TARGET_2; i++) {
      prevLands.put(i, nextLand);
      nextLand = advanceTime(nextLand);
      if (prevLands.containsValue(nextLand)) {
        // Found cycle.
        int cycleStart = prevLands.inverse().get(nextLand);
        int cycleLength = i - cycleStart + 1;
        int remaining = TARGET_2 - cycleStart;
        int indexInCycle = remaining % cycleLength;
        nextLand = prevLands.get(cycleStart + indexInCycle);
        break;
      }
    }
    return nextLand.streamOccurrencesOf(TREES).count() *
        nextLand.streamOccurrencesOf(LUMBERYARD).count();
  }

  private static Space2D<Character> advanceTime(Space2D<Character> land) {
    Space2D<Character> newLand = Space2D.copyOf(land);
    land.streamAllPointsInBounds().forEach(p -> {
      Character acre = land.getValueAt(p);
      if (acre == TREES) {
        if (numAdjacent(land, p, LUMBERYARD) >= 3) {
          newLand.setValueAt(p, LUMBERYARD);
        }
      } else if (acre == LUMBERYARD) {
        if (numAdjacent(land, p, LUMBERYARD) == 0 || numAdjacent(land, p, TREES) == 0) {
          newLand.setValueAt(p, null);
        }
      } else if (numAdjacent(land, p, TREES) >= 3) {
        newLand.setValueAt(p, TREES);
      }
    });
    return newLand;
  }

  private static int numAdjacent(Space2D<Character> land, Point p, Character type) {
    return (int) Arrays.stream(Direction.values())
        .map(dir -> land.getValueAt(dir.apply(p)))
        .filter(type::equals)
        .count();
  }
}
