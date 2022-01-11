package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.MathUtils;
import com.dixie.adventofcode.lib.Memoizer;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongUnaryOperator;

public class Day22 extends Day {
  public static void main(String[] args) {
    new Day22().solve();
  }

  private static final int TOTAL_CARDS_1 = 10007;
  private static final long TOTAL_CARDS_2 = 119315717514047L;

  private LongUnaryOperator inverseShuffle;

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

  @Override
  protected long part2(List<String> lines) {
    Collections.reverse(lines);
    inverseShuffle = lines.stream()
        .map(Day22::parseInverseFunction)
        .reduce(LongUnaryOperator.identity(), LongUnaryOperator::andThen);
    return memoizedReverseShuffle.apply(2020L, 101741582076661L);
  }

  private static IntUnaryOperator parseFunction(String line) {
    if (line.equals("deal into new stack")) {
      return i -> TOTAL_CARDS_1 - i - 1;
    } else if (line.startsWith("cut")) {
      int cut = Integer.parseInt(line.substring(4));
      return i -> Math.floorMod(i - cut, TOTAL_CARDS_1);
    } else if (line.startsWith("deal with increment")) {
      int inc = Integer.parseInt(line.substring(20));
      return i -> Math.floorMod(i * inc, TOTAL_CARDS_1);
    }
    return null;
  }

  private static LongUnaryOperator parseInverseFunction(String line) {
    if (line.equals("deal into new stack")) {
      return i -> TOTAL_CARDS_2 - i - 1;
    } else if (line.startsWith("cut")) {
      int cut = Integer.parseInt(line.substring(4));
      return i -> Math.floorMod(i + cut, TOTAL_CARDS_2);
    } else if (line.startsWith("deal with increment")) {
      int inc = Integer.parseInt(line.substring(20));
      return i -> Math.floorMod(i * MathUtils.modInverse(inc, TOTAL_CARDS_2), TOTAL_CARDS_2);
    }
    return null;
  }

  private final BiFunction<Long, Long, Long> memoizedReverseShuffle =
      Memoizer.memoize(this::reverseShuffle);

  private long maxShuffles = 0;

  private long reverseShuffle(long endIndex, long numShuffles) {
    if (numShuffles == 0) {
      return endIndex;
    }
    if (numShuffles == 1) {
      return inverseShuffle.applyAsLong(endIndex);
    }
    long half = numShuffles / 2;
    endIndex = memoizedReverseShuffle.apply(endIndex, half);
    if (half > maxShuffles) {
      System.err.println(half);
      maxShuffles = half;
    }
    return memoizedReverseShuffle.apply(endIndex, numShuffles - half);
  }
}
