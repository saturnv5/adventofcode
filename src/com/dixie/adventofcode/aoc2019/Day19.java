package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;

import java.util.List;
import java.util.stream.IntStream;

public class Day19 extends Day {
  public static void main(String[] args) {
    new Day19().solve();
  }

  private long[] program;

  @Override
  protected void prepare(List<String> lines) {
    program = StreamUtils.streamLongs(lines.get(0), ",").toArray();
  }

  @Override
  protected Object part1(List<String> lines) {
    return IntStream.range(0, 50)
        .mapToLong(y -> IntStream.range(0, 50).filter(x -> withinBeam(x, y)).count())
        .sum();
  }

  @Override
  protected Object part2(List<String> lines) {
    int x = 300, y = 200; // Can't imagine any input would have a beam large enough before 200.
    // Zigzag down the right edge of the beam while checking the extremities of a 100x100 box.
    // This only works by assuming the beam is solid and moves monotonically down and to the right.
    while (true) {
      if (withinBeam(x, y)) {
        // Test the opposite corner.
        if (withinBeam(x - 99, y + 99)) {
          return y + (x - 99) * 10000;
        } else {
          x++; // Too low, zig rightwards.
        }
      } else {
        y++; // Too high, zag downwards.
      }
    }
  }

  private boolean withinBeam(int x, int y) {
    return new Intcode(program).executeUntilEnd(x, y) == 1;
  }
}
