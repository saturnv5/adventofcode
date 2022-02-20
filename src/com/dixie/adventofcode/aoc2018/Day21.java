package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.aoc2018.common.Instruction;
import com.dixie.adventofcode.lib.Day;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;

public class Day21 extends Day {
  public static void main(String[] args) {
    new Day21().solve();
  }

  private int ipReg;
  private List<Instruction> program;

  @Override
  protected void prepare(List<String> lines) {
    ipReg = Integer.parseInt(lines.get(0).substring(4));
    program = lines.stream().skip(1).map(Instruction::parse).toList();
  }

  @Override
  protected Object part1(List<String> lines) {
    AtomicInteger firstHaltingNumber = new AtomicInteger();
    findHaltingNumbers(firstHaltingNumber::set, () -> firstHaltingNumber.get() != 0);
    return firstHaltingNumber.get();
  }

  @Override
  protected Object part2(List<String> lines) {
    HashSet<Integer> haltingNumbers = new HashSet<>();
    AtomicInteger prevHaltingNumber = new AtomicInteger();
    AtomicInteger lastHaltingNumber = new AtomicInteger();
    findHaltingNumbers(i -> {
      if (haltingNumbers.add(i)) {
        prevHaltingNumber.set(i);
      } else {
        lastHaltingNumber.set(prevHaltingNumber.get());
      }
    }, () -> lastHaltingNumber.get() != 0);
    return lastHaltingNumber.get();
  }

  private void findHaltingNumbers(IntConsumer consumer, BooleanSupplier shouldStop) {
    int[] r = new int[6];
    while (r[ipReg] >= 0 && r[ipReg] < program.size()) {
      if (shouldStop.getAsBoolean()) {
        return;
      }
      Instruction inst = program.get(r[ipReg]);
      if (inst.op() == Instruction.Op.EQRR) {
        // This is the only instruction comparing with r0, and we need it to be true.
        if (inst.a() == 0) consumer.accept(r[inst.b()]);
        else if (inst.b() == 0) consumer.accept(r[inst.a()]);
      }
      inst.execute(r);
      r[ipReg]++;
    }
  }
}
