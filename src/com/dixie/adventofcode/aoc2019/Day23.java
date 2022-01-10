package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.util.concurrent.SettableFuture;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.LongSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day23 extends Day {
  public static void main(String[] args) {
    new Day23().solve();
  }

  private final ExecutorService executor = Executors.newFixedThreadPool(50);
  private final Object lock = new Object();
  private long[] program;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    program = StreamUtils.streamLongs(lines.get(0), ",").toArray();
    return super.solve(lines, part1);
  }

  @Override
  protected long part1(List<String> lines) {
    Intcode[] ics = Stream.generate(() -> new Intcode(program)).limit(50).toArray(Intcode[]::new);
    InputSupplier[] inputs = IntStream.range(0, 50).mapToObj(i -> {
      InputSupplier supplier = new InputSupplier(i);
      ics[i].setInputSupplier(supplier);
      return supplier;
    }).toArray(InputSupplier[]::new);

    SettableFuture<Long> yAt255 = SettableFuture.create();

    for (Intcode ic : ics) {
      executor.execute(() -> {
        while (!ic.hasHalted()) {
          Long out = ic.executeUntilOutput();
          synchronized (lock) {
            if (out == null) break;
            int address = out.intValue();
            long x = ic.executeUntilOutput(), y = ic.executeUntilOutput();
            if (address == 255) {
              yAt255.set(y);
              break;
            }
            inputs[address].queue.offer(x);
            inputs[address].queue.offer(y);
          }
        }
      });
    }
    try {
      long ans = yAt255.get();
      executor.shutdownNow();
      return ans;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private class InputSupplier implements LongSupplier {
    final ArrayDeque<Long> queue = new ArrayDeque<>();

    public InputSupplier(long address) {
      queue.offer(address);
    }

    @Override
    public long getAsLong() {
      synchronized (lock) {
        if (queue.isEmpty()) {
          return -1;
        }
        return queue.poll();
      }
    }
  }
}
