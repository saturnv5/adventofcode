package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day21 extends Day {
  public static void main(String[] args) {
    new Day21().solve();
  }

  private static final String[] PROGRAM_1 = {
      "OR D J",
      "NOT C T",
      "AND T J", // Jump if landing spot is ground, but C is a hole.
      "NOT A T",
      "OR T J", // Also jump if right next to a hole.
      "WALK",
      ""
  };

  private static final String[] PROGRAM_2 = {
      "NOT B T",
      "NOT C J",
      "OR T J", // B or C are holes.
      "AND D J", // Jump if landing spot is ground.
      "AND H J", // But only if H is ground, in case we need to jump again immediately.
      "NOT A T",
      "OR T J", // Also jump if right next to a hole.
      "RUN",
      ""
  };

  private Intcode ic;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    ic = new Intcode(lines.get(0));
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    // ic.setOutputConsumer(c -> System.out.print(toChar(c)));
    return ic.executeUntilEnd(toInputs(PROGRAM_1));
  }

  @Override
  protected long part2(List<String> lines) {
    // ic.setOutputConsumer(c -> System.out.print(toChar(c)));
    return ic.executeUntilEnd(toInputs(PROGRAM_2));
  }

  private static long[] toInputs(String[] program) {
    return  String.join("\n", program).chars().mapToLong(c -> c).toArray();
  }

  private static char toChar(long ch) {
    return (char) ch;
  }
}
