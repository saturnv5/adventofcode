package com.dixie.adventofcode.aoc2019.common;

import com.dixie.adventofcode.lib.StreamUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import java.util.stream.IntStream;

public class Intcode {
  private final HashMap<Long, Long> memory = new HashMap<>();
  private long index = 0;
  private long baseIndex = 0;
  private long lastOutput = -1;
  private boolean hasHalted = false;
  private LongSupplier inputSupplier;
  private Consumer<Long> outputConsumer;

  public Intcode(String program) {
    this(StreamUtils.streamLongs(program, ",").toArray());
  }

  public Intcode(int[] memory) {
    IntStream.range(0, memory.length).forEach(i -> this.memory.put((long) i, (long) memory[i]));
  }

  public Intcode(long[] memory) {
    IntStream.range(0, memory.length).forEach(i -> this.memory.put((long) i, memory[i]));
  }

  public void setInputSupplier(LongSupplier inputSupplier) {
    this.inputSupplier = inputSupplier;
  }

  public void setOutputConsumer(Consumer<Long> outputConsumer) {
    this.outputConsumer = outputConsumer;
  }

  public long executeUntilEnd(long... inputOverride) {
    while (executeNextInstruction(createInputSupplier(inputOverride), false, false)) ;
    return lastOutput;
  }

  public long executeUntilInput(long... inputOverride) {
    while (executeNextInstruction(createInputSupplier(inputOverride), true, false)) ;
    return lastOutput;
  }

  public Long executeUntilOutput(long... inputOverride) {
    while (executeNextInstruction(createInputSupplier(inputOverride), false, true)) ;
    return hasHalted ? null : lastOutput;
  }

  private LongSupplier createInputSupplier(long[] inputOverride) {
    if (inputOverride.length == 0) {
      return inputSupplier;
    }
    PrimitiveIterator.OfLong itr = Arrays.stream(inputOverride).iterator();
    return itr::next;
  }

  public boolean hasHalted() {
    return hasHalted;
  }

  public long getLastOutput() {
    return lastOutput;
  }

  private boolean executeNextInstruction(
      LongSupplier input, boolean stopOnInput, boolean stopOnOutput) {
    if (hasHalted || Thread.interrupted()) {
      hasHalted = true;
      return false;
    }
    String op = String.valueOf(readMemory(index));
    int opCode = op.length() == 1
            ? op.charAt(0) - '0'
            : Integer.parseInt(op.substring(op.length() - 2));
    switch (opCode) {
      case 1 -> {
        add(op);
        return true;
      }
      case 2 -> {
        mul(op);
        return true;
      }
      case 3 -> {
        input(op, input.getAsLong());
        return !stopOnInput;
      }
      case 4 -> {
        output(op);
        return !stopOnOutput;
      }
      case 5 -> {
        jump(op, true);
        return true;
      }
      case 6 -> {
        jump(op, false);
        return true;
      }
      case 7 -> {
        lessThan(op);
        return true;
      }
      case 8 -> {
        eq(op);
        return true;
      }
      case 9 -> {
        adjRelBase(op);
        return true;
      }
    }
    hasHalted = true;
    return false;
  }

  private void add(String op) {
    long val = evalArg(op, 1) + evalArg(op, 2);
    memory.put(evalAddress(op, 3), val);
    index += 4;
  }

  private void mul(String op) {
    long val = evalArg(op, 1) * evalArg(op, 2);
    memory.put(evalAddress(op, 3), val);
    index += 4;
  }

  private void input(String op, long input) {
    memory.put(evalAddress(op, 1), input);
    index += 2;
  }

  private void output(String op) {
    lastOutput = evalArg(op, 1);
    index += 2;
    if (outputConsumer != null) {
      outputConsumer.accept(lastOutput);
    }
  }

  private void jump(String op, boolean jumpIfTrue) {
    long arg = evalArg(op, 1);
    if ((jumpIfTrue && arg != 0) || (!jumpIfTrue && arg == 0)) {
      index = evalArg(op, 2);
    } else {
      index += 3;
    }
  }

  private void lessThan(String op) {
    long val = evalArg(op, 1) < evalArg(op, 2) ? 1 : 0;
    memory.put(evalAddress(op, 3), val);
    index += 4;
  }

  private void eq(String op) {
    long val = evalArg(op, 1) == evalArg(op, 2) ? 1 : 0;
    memory.put(evalAddress(op, 3), val);
    index += 4;
  }

  private void adjRelBase(String op) {
    long val = evalArg(op, 1);
    baseIndex += val;
    index += 2;
  }

  private long evalArg(String op, int argIndex) {
    int mode = op.length() >= argIndex + 2 ? op.charAt(op.length() - argIndex - 2) - '0' : 0;
    return switch (mode) {
      case 1 -> readMemory(index + argIndex); // immediate
      case 2 -> readMemory(baseIndex + readMemory(index + argIndex)); // relative
      default -> readMemory(readMemory(index + argIndex)); // position
    };
  }

  private long evalAddress(String op, int argIndex) {
    if (op.length() >= argIndex + 2 && op.charAt(op.length() - argIndex - 2) == '2') {
      return baseIndex + readMemory(index + argIndex);
    } else {
      return readMemory(index + argIndex);
    }
  }
  
  private long readMemory(long address) {
    return memory.getOrDefault(address, 0L);
  }
}
