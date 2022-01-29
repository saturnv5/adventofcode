package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Memoizer;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.base.Splitter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day16 extends Day {
  public static void main(String[] args) {
    new Day16().solve();
  }

  private static final Splitter SAMPLE_SPLITTER =
      Splitter.onPattern("Before: |After:  |, |\\[|\\]").omitEmptyStrings();

  private List<Sample> samples;
  private List<int[]> program;
  private final Function<Sample, Op[]> potentialOps = Memoizer.memoize(sample ->
      Arrays.stream(Op.values()).filter(op -> {
        int[] out = new Instruction(op, sample.instruction).execute(sample.before.clone());
        return Arrays.equals(out, sample.after);
    }).toArray(Op[]::new));

  @Override
  protected void prepare(List<String> lines) {
    samples = new ArrayList<>();
    int i = 0;
    for (; i < lines.size(); i += 4) {
      String line = lines.get(i);
      if (!line.startsWith("Before")) {
        break;
      }
      int[] before = SAMPLE_SPLITTER.splitToStream(line).mapToInt(Integer::parseInt).toArray();
      int[] instruction = StreamUtils.streamInts(lines.get(i + 1)).toArray();
      int[] after =
          SAMPLE_SPLITTER.splitToStream(lines.get(i + 2)).mapToInt(Integer::parseInt).toArray();
      samples.add(new Sample(before, instruction, after));
    }
    program = new ArrayList<>();
    for (; i < lines.size(); i++) {
      if (lines.get(i).isEmpty()) continue;
      program.add(StreamUtils.streamInts(lines.get(i)).toArray());
    }
  }

  @Override
  protected Object part1(List<String> lines) {
    return samples.stream().filter(s -> potentialOps.apply(s).length >= 3).count();
  }

  @Override
  protected Object part2(List<String> lines) {
    Op[] opMap = computeOpMapping();
    int[] r = new int[4];
    program.stream()
        .map(inst -> new Instruction(opMap[inst[0]], inst))
        .forEach(in -> in.execute(r));
    return r[0];
  }

  private Op[] computeOpMapping() {
    Op[] mapping = new Op[Op.values().length];
    Set<Op> unMappedOps = Arrays.stream(Op.values()).collect(Collectors.toSet());
    List<Sample> unMappedSamples = samples;
    while (!unMappedSamples.isEmpty()) {
      List<Sample> remainingSamples = new ArrayList<>();
      for (Sample sample : unMappedSamples) {
        if (mapping[sample.instruction[0]] != null) continue;
        Op[] possibleOps = Arrays.stream(potentialOps.apply(sample))
            .filter(unMappedOps::contains)
            .toArray(Op[]::new);
        if (possibleOps.length == 1) {
          unMappedOps.remove(possibleOps[0]);
          mapping[sample.instruction[0]] = possibleOps[0];
        } else {
          remainingSamples.add(sample);
        }
      }
      unMappedSamples = remainingSamples;
    }
    return mapping;
  }

  private record Sample(int[] before, int[] instruction, int[] after) {}

  private enum Op {
    ADDR, ADDI, MULR, MULI, BANR, BANI, BORR, BORI, SETR, SETI, GTIR, GTRI, GTRR, EQIR, EQRI, EQRR
  }

  private record Instruction(Op op, int a, int b, int c) {
    Instruction(Op op, int[] args) {
      this(op, args[1], args[2], args[3]);
    }

    int[] execute(int[] r) {
      switch (op) {
        case ADDR -> r[c] = r[a] + r[b];
        case ADDI -> r[c] = r[a] + b;
        case MULR -> r[c] = r[a] * r[b];
        case MULI -> r[c] = r[a] * b;
        case BANR -> r[c] = r[a] & r[b];
        case BANI -> r[c] = r[a] & b;
        case BORR -> r[c] = r[a] | r[b];
        case BORI -> r[c] = r[a] | b;
        case SETR -> r[c] = r[a];
        case SETI -> r[c] = a;
        case GTIR -> r[c] = a > r[b] ? 1 : 0;
        case GTRI -> r[c] = r[a] > b ? 1 : 0;
        case GTRR -> r[c] = r[a] > r[b] ? 1 : 0;
        case EQIR -> r[c] = a == r[b] ? 1 : 0;
        case EQRI -> r[c] = r[a] == b ? 1 : 0;
        case EQRR -> r[c] = r[a] == r[b] ? 1 : 0;
      }
      return r;
    }
  }
}
