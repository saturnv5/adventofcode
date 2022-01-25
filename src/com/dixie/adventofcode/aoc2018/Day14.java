package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;

import java.util.ArrayList;
import java.util.List;

public class Day14 extends Day {
  public static void main(String[] args) {
    new Day14().solve();
  }

  private int elf1 = 0, elf2 = 1;
  private int input;
  private ArrayList<Integer> scores;

  @Override
  protected void prepare(List<String> lines) {
    input = Integer.parseInt(lines.get(0));
    scores = new ArrayList<>(input + 100);
    scores.add(3);
    scores.add(7);
  }

  @Override
  protected Object part1(List<String> lines) {
    while (scores.size() < input + 10) {
      advanceRecipes();
    }
    return toString(input, 10);
  }

  @Override
  protected Object part2(List<String> lines) {
    List<Integer> target = lines.get(0).chars().map(c -> c - '0').boxed().toList();
    int index = 0;
    while (true) {
      if (index + target.size() >= scores.size()) {
        advanceRecipes();
      }
      if (target.equals(scores.subList(index, index + target.size()))) {
        return index;
      }
      index++;
    }
  }

  private void advanceRecipes() {
    int sum = scores.get(elf1) + scores.get(elf2);
    if (sum >= 10) {
      scores.add(1);
      sum -= 10;
    }
    scores.add(sum);
    elf1 = (elf1 + scores.get(elf1) + 1) % scores.size();
    elf2 = (elf2 + scores.get(elf2) + 1) % scores.size();
  }

  private String toString(int fromIndex, int length) {
    StringBuilder sb = new StringBuilder(length);
    scores.subList(fromIndex, fromIndex + length).forEach(sb::append);
    return sb.toString();
  }
}
