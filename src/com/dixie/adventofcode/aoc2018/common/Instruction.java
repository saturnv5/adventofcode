package com.dixie.adventofcode.aoc2018.common;

import java.util.Arrays;

public record Instruction(Op op, int a, int b, int c) {
  public enum Op {
    ADDR, ADDI, MULR, MULI, BANR, BANI, BORR, BORI, SETR, SETI, GTIR, GTRI, GTRR, EQIR, EQRI, EQRR
  }

  public static Instruction parse(String line) {
    String[] tokens = line.split(" ");
    Op op = Arrays.stream(Op.values())
        .filter(o -> o.name().equalsIgnoreCase(tokens[0]))
        .findFirst()
        .get();
    return new Instruction(op, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
        Integer.parseInt(tokens[3]));
  }

  public Instruction(Op op, int[] args) {
    this(op, args[1], args[2], args[3]);
  }

  public int[] execute(int[] r) {
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
