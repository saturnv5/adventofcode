package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Day5 extends Day {
  public static void main(String[] args) {
    new Day5().solve();
  }

  private static final int REACT_DIFF = 'a' - 'A';

  @Override
  protected Object part1(List<String> lines) {
    LinkedList<Integer> polymer = lines.get(0)
        .chars()
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
