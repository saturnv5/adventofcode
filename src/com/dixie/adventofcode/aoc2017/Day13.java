package com.dixie.adventofcode.aoc2017;

import com.dixie.adventofcode.lib.Day;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class Day13 extends Day {
  public static void main(String[] args) {
    new Day13().solve();
  }

  private List<Layer> layers;

  @Override
  protected void prepare(List<String> lines) {
    layers = lines.stream().map(Layer::parse).toList();
  }

  @Override
  protected Object part1(List<String> lines) {
    LongAdder severity = new LongAdder();
    isPacketUndetected(0, severity);
    return severity.longValue();
  }

  @Override
  protected Object part2(List<String> lines) {
    // Brute-force only takes ~1 second, so I didn't bother optimizing.
    return IntStream.iterate(1, i -> i + 1)
        .filter(this::isPacketUndetected)
        .findFirst()
        .getAsInt();
  }

  private boolean isPacketUndetected(int delay) {
    return isPacketUndetected(delay, new LongAdder());
  }

  private boolean isPacketUndetected(int delay, LongAdder severity) {
    AtomicBoolean isUndetected = new AtomicBoolean(true);
    layers.stream()
        .filter(layer -> (layer.depth() + delay) % (layer.range() * 2 - 2) == 0)
        .forEach(layer -> {
          severity.add(layer.severity());
          isUndetected.set(false);
        });
    return isUndetected.get();
  }

  private record Layer(int depth, int range) {
    static Layer parse(String line) {
      String[] tokens = line.split(": ");
      return new Layer(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }

    int severity() {
      return depth * range;
    }
  }
}
