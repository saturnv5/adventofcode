package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Pair;
import com.dixie.adventofcode.lib.Space2D;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 extends Day {
  public static void main(String[] args) {
    new Day12().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    Space2D<Boolean> state =
        Space2D.parseFromStrings(List.of(lines.get(0).substring(15)), ch -> ch == '#');
    Map<List<Boolean>, Boolean> notes = lines.subList(2, lines.size())
        .stream()
        .map(Day12::parseNote)
        .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    IntStream.range(0, 20).forEach(i -> {

    });
    return state.streamAllPoints().filter(state::getValueAt).mapToInt(p -> p.x).sum();
  }

  private static Pair<List<Boolean>, Boolean> parseNote(String note) {
    return Pair.of(note.chars().limit(5).mapToObj(ch -> ch == '#').toList(), note.charAt(9) == '#');
  }
}
