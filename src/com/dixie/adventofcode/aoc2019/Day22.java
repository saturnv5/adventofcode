package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;

import java.math.BigInteger;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

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
    LongUnaryOperator shuffle = lines.stream()
        .map(Day22::parseFunction)
        .reduce(LongUnaryOperator.identity(), LongUnaryOperator::andThen);
    return shuffle.applyAsLong(2019);
  }

  private static LongUnaryOperator parseFunction(String line) {
    if (line.equals("deal into new stack")) {
      return Day22::reverse;
    } else if (line.startsWith("cut")) {
      int cut = Integer.parseInt(line.substring(4));
      return i -> cut(i, cut);
    } else if (line.startsWith("deal with increment")) {
      int inc = Integer.parseInt(line.substring(20));
      return i -> inc(i, inc);
    }
    return null;
  }

  private static long reverse(long i) {
    return TOTAL_CARDS - i - 1;
  }

  private static long cut(long i, long cut) {
    return Math.floorMod(i - cut, TOTAL_CARDS);
  }

  private static long inc(long i, long inc) {
    return Math.floorMod(i * inc, TOTAL_CARDS);
  }
}
