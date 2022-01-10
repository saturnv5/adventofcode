package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;

import java.util.List;
import java.util.function.IntUnaryOperator;

public class Day22 extends Day {
  public static void main(String[] args) {
    new Day22().solve();
  }

  private static final int TOTAL_CARDS = 10007;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    IntUnaryOperator shuffle = lines.stream()
        .map(Day22::parseFunction)
        .reduce(IntUnaryOperator.identity(), IntUnaryOperator::andThen);
    return shuffle.applyAsInt(2019);
  }

  private static IntUnaryOperator parseFunction(String line) {
    if (line.equals("deal into new stack")) {
      return i -> TOTAL_CARDS - i - 1;
    } else if (line.startsWith("cut")) {
      int cut = Integer.parseInt(line.substring(4));
      return i -> Math.floorMod(i - cut, TOTAL_CARDS);
    } else if (line.startsWith("deal with increment")) {
      int inc = Integer.parseInt(line.substring(20));
      return i -> Math.floorMod(i * inc, TOTAL_CARDS);
    }
    return null;
  }
}
