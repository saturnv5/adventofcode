package com.dixie.adventofcode.aoc2019;

import com.dixie.adventofcode.aoc2019.common.Intcode;
import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.StreamUtils;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day23 extends Day {
  public static void main(String[] args) {
    new Day23().solve();
  }

  private final Object lock = new Object();
  private final AtomicLong natX = new AtomicLong();
  private final AtomicLong natY = new AtomicLong();
  private ExecutorService executor;
  private Intcode[] ics;
  private InputSupplier[] inputs;

  @Override
  protected long solve(List<String> lines, boolean part1) {
    executor = Executors.newFixedThreadPool(50);
    long[] program = StreamUtils.streamLongs(lines.get(0), ",").toArray();

    for (int i = 0; i < 50; i++) {
      ics[i] = new Intcode(program);
      inputs[i] = new InputSupplier(i);
      ics[i].setInputSupplier(inputs[i]);
    }

    long ans = super.solve(lines, part1);
    stopNetwork();
    return ans;
  }

  @Override
  protected long part1(List<String> lines) {
    SettableFuture<Long> yAt255 = SettableFuture.create();
    startNetwork(yAt255);
    return Futures.getUnchecked(yAt255);
  }

  @Override
  protected long part2(List<String> lines) {
    startNetwork(SettableFuture.create());
    long lastY = Long.MIN_VALUE;
    while (true) {
      synchronized (lock) {
        if (Arrays.stream(inputs).allMatch(InputSupplier::isIdle)) {
          if (lastY == natY.get()) {
            return lastY;
          }
          inputs[0].queue(natX.get());
          inputs[0].queue(natY.get());
          lastY = natY.get();
        }
      }
    }
  }

  private void startNetwork(SettableFuture<Long> yAt255) {
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
              natX.set(x);
              natY.set(y);
            } else {
              inputs[address].queue(x);
              inputs[address].queue(y);
            }
          }
        }
      });
    }
  }

  private void stopNetwork() {
    try {
      executor.shutdownNow();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private class InputSupplier implements LongSupplier {
    private final ArrayDeque<Long> queue = new ArrayDeque<>();
    private boolean isIdle;

    public InputSupplier(long address) {
      queue.offer(address);
    }

    public void queue(long val) {
      queue.offer(val);
      isIdle = false;
    }

    public boolean isIdle() {
      return isIdle;
    }

    @Override
    public long getAsLong() {
      synchronized (lock) {
        if (queue.isEmpty()) {
          isIdle = true;
          return -1;
        }
        return queue.poll();
      }
    }
  }
}
