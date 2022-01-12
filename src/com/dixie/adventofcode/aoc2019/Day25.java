package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;

import java.io.IOException;
import java.util.List;
import java.util.PrimitiveIterator;

public class Day25 extends Day {
  public static void main(String[] args) {
    new Day25().solve();
  }

  @Override
  protected long part1(List<String> lines) {
    Intcode ic = new Intcode(lines.get(0));
    PrimitiveIterator.OfLong input =
        String.join("\n", COMMANDS_1).chars().mapToLong(c -> c).iterator();
    ic.setInputSupplier(() -> {
      long next = input.nextLong();
      System.out.print((char) next);
      return next;
    });
    // Uncomment to play text adventure manually instead.
    // ic.setInputSupplier(Day25::input);
    ic.setOutputConsumer(c -> System.out.print((char) c.intValue()));
    ic.executeUntilEnd();
    return 0;
  }

  private static long input() {
    try {
      return System.in.read();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final String[] COMMANDS_1 = {
      "west",
      "take ornament",
      "west",
      "take astrolabe",
      "north",
      "take fuel cell",
      "south",
      "south",
      "take hologram",
      "north",
      "east",
      "east",
      "east",
      "south",
      "west",
      "north",
      "west",
      "north",
      "west",
      "north",
      ""
  };
}
