package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BooleanSupplier;

public class Day8 extends Day {
  public static void main(String[] args) {
    new Day8().solve();
  }

  private final Map<String, LongAdder> registers = new HashMap<>();
  private List<Instruction> instructions;

  @Override
  protected void prepare(List<String> lines) {
    instructions = lines.stream().map(this::parseInstruction).toList();
  }

  @Override
  protected Object part1(List<String> lines) {
    instructions.forEach(in -> in.execute(registers));
    return largestValue();
  }

  @Override
  protected Object part2(List<String> lines) {
    registers.clear();
    LongAccumulator largest = new LongAccumulator(Math::max, 0);
    instructions.forEach(in -> {
      in.execute(registers);
      if (!registers.isEmpty()) largest.accumulate(largestValue());
    });
    return largest.longValue();
  }

  private long largestValue() {
    return registers.values()
        .stream()
        .max(Comparator.comparingLong(LongAdder::longValue))
        .get()
        .longValue();
  }

  private Instruction parseInstruction(String line) {
    String[] parts = line.split(" if ");
    String[] actionTokens = parts[0].split(" ");
    String reg = actionTokens[0];
    int delta = Integer.parseInt(actionTokens[2]);
    if (actionTokens[1].equals("dec")) delta = -delta;
    return new Instruction(reg, delta, parseCondition(parts[1]));
  }

  private BooleanSupplier parseCondition(String condition) {
    String[] tokens = condition.split(" ");
    String reg = tokens[0];
    int target = Integer.parseInt(tokens[2]);
    return switch (tokens[1]) {
      case "==" -> () -> regValue(reg) == target;
      case "!=" -> () -> regValue(reg) != target;
      case "<" -> () -> regValue(reg) < target;
      case "<=" -> () -> regValue(reg) <= target;
      case ">" -> () -> regValue(reg) > target;
      case ">=" -> () -> regValue(reg) >= target;
      default -> () -> false;
    };
  }

  private long regValue(String reg) {
    return registers.getOrDefault(reg, new LongAdder()).longValue();
  }

  private record Instruction(String reg, int delta, BooleanSupplier condition) {
    void execute(Map<String, LongAdder> registers) {
      if (condition.getAsBoolean()) {
        registers.computeIfAbsent(reg, r -> new LongAdder()).add(delta);
      }
    }
  }
}
