package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day7 extends Day {
  public static void main(String[] args) {
    new Day7().solve();
  }

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    int[] program = StreamUtils.streamInts(lines.get(0), ",").toArray();
    if (part1) {
      return maxSignal(program);
    } else {
      return Collections2.permutations(IntStream.rangeClosed(5, 9)
                      .boxed().toList())
              .stream()
              .mapToLong(perm -> tryPhaseSetting(program, perm))
              .max()
              .getAsLong();
    }
  }

  private static long maxSignal(int[] program) {
    return maxSignal(program, 0, IntStream.rangeClosed(0, 4).toArray());
  }

  private static long maxSignal(int[] program, long input, int[] remainingPhases) {
    if (remainingPhases.length == 0) {
      return input;
    }
    long maxSignal = 0;
    for (int phase : remainingPhases) {
      Intcode ic = new Intcode(program);
      long output = ic.executeUntilEnd(phase, input);
      long signal = maxSignal(program, output, Arrays.stream(remainingPhases)
              .filter(p -> p != phase)
              .toArray());
      maxSignal = Math.max(maxSignal, signal);
    }
    return maxSignal;
  }

  private static long tryPhaseSetting(int[] program, List<Integer> phases) {
    List<Intcode> ics = phases.stream().map(phase -> {
      Intcode ic = new Intcode(program);
      ic.executeUntilInput(phase);
      return ic;
    }).toList();
    long nextInput = 0;
    for (int i = 0; !ics.get(i).hasHalted(); i = (i + 1) % ics.size()) {
      nextInput = ics.get(i).executeUntilOutput(nextInput);
    }
    return Iterables.getLast(ics).getLastOutput();
  }
}
