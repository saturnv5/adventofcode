package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 extends Day {
  public static void main(String[] args) {
    new Day5().solve();
  }

  private static final int REACT_DIFF = 'a' - 'A';

  @Override
  protected Object part1(List<String> lines) {
    return polymerResultSize(lines.get(0), x -> true);
  }

  @Override
  protected Object part2(List<String> lines) {
    return IntStream.range('A', 'Z' + 1)
        .map(x -> polymerResultSize(lines.get(0), u -> u != x && (u - REACT_DIFF) != x))
        .min().getAsInt();
  }

  private static int polymerResultSize(String units, IntPredicate filter) {
    LinkedList<Integer> polymer = units
        .chars()
        .filter(filter)
        .boxed()
        .collect(Collectors.toCollection(LinkedList::new));
    ListIterator<Integer> itr = polymer.listIterator();
    while (itr.hasNext()) {
      int unit = itr.next();
      itr.previous();
      if (!itr.hasPrevious()) {
        itr.next();
        continue;
      }
      int prevUnit = itr.previous();
      if (Math.abs(unit - prevUnit) == REACT_DIFF) {
        // Remove both units.
        itr.remove();
        itr.next();
        itr.remove();
      } else {
        itr.next();
        itr.next(); // Can't react, move forwards again.
      }
    }
    return polymer.size();
  }
}
