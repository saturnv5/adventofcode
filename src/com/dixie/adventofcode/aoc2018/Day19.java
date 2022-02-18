package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.aoc2018.common.Instruction;
import com.dixie.adventofcode.lib.Day;

import java.util.List;

public class Day19 extends Day {
  public static void main(String[] args) {
    new Day19().solve();
  }

  @Override
  protected Object part1(List<String> lines) {
    int ipReg = Integer.parseInt(lines.get(0).substring(4));
    List<Instruction> program = lines.stream().skip(1).map(Instruction::parse).toList();
    int[] r = new int[6];
    while (r[ipReg] >= 0 && r[ipReg] < program.size()) {
      program.get(r[ipReg]).execute(r);
      r[ipReg]++;
    }
    execute();
    return r0;
  }

  int r0, r1, r2, r4, r5;

  private void execute() {
    // Move forward 16 + 1 instructions.
    r4 += 2;
    r4 *= r4;
    r4 *= 19;
    r4 *= 11;
    r1 += 4;
    r1 *= 22;
    r1 += 2;
    r4 += r1;
    if (r0 == 0) {
      // Back to start.
      executeSub();
      return;
    }
    r1 = 27;
    r1 *= 28;
    r1 += 29;
    r1 *= 30;
    r1 += 14;
    r1 *= 32;
    r4 += r1;
    r0 = 0;
    executeSub();
  }

  private void executeSub() {
    r2 = 1;
    r5 = 1;
    while (true) {
      r1 = r2 * r5;
      r1 = r1 == r4 ? 1 : 0;
      if (r1 == 1) {
        r0 += r2;
      }
      r5++;
      r1 = r5 > r4 ? 1 : 0;
      if (r1 == 0) {
        continue;
      }
      r2++;
      r1 = r2 > r4 ? 1 : 0;
      if (r1 == 1) {
        return;
      }
      r5 = 1;
    }
  }
}
