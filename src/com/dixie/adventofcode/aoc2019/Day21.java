package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day21 extends Day {
  public static void main(String[] args) {
    new Day21().solve();
  }

  private static final String[] PROGRAM = {
      "OR D J",
      "NOT C T",
      "AND T J",
      "NOT A T",
      "OR T J",
      "WALK",
      ""
  };
  private static final long[] INPUTS =
      String.join("\n", PROGRAM).chars().mapToLong(c -> c).toArray();

  private Intcode ic;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    ic = new Intcode(lines.get(0));
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    // ic.setOutputConsumer(c -> System.out.print(toChar(c)));
    return ic.executeUntilEnd(INPUTS);
  }

  private static char toChar(long ch) {
    return (char) ch;
  }
}
