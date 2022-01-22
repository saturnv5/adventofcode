package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Space2D;
import com.google.common.base.Splitter;

import java.awt.*;
import java.util.List;

public class Day10 extends Day {
  public static void main(String[] args) {
    new Day10().solve();
  }

  private static final Splitter LIGHT_SPLITTER =
      Splitter.onPattern("position|velocity|=<|>| |, ").trimResults().omitEmptyStrings();

  private List<Light> lights;
  private int secondsWaited = 0;

  @Override
  protected Object solve(List<String> lines, boolean part1) {
    lights = lines.stream().map(Day10::parseLight).toList();
    return super.solve(lines, part1);
  }

  @Override
  protected Object part1(List<String> lines) {
    return waitUntilMessage().toPrintableImage(v -> v == null ? "  " : "██");
  }

  @Override
  protected Object part2(List<String> lines) {
    waitUntilMessage();
    return secondsWaited;
  }

  private Space2D<Boolean> waitUntilMessage() {
    secondsWaited = 0;
    int size = Integer.MAX_VALUE;
    Space2D<Boolean> message = null;

    while (true) {
      Space2D<Boolean> space = new Space2D<>();
      lights.forEach(l -> {
        space.setValueAt(l.location, true);
        l.advanceTime();
      });
      // Using half-perimeter length, but area probably works too.
      int newSize = space.getBounds().width + space.getBounds().height;
      if (newSize > size) {
        secondsWaited--;
        break;
      }
      size = newSize;
      message = space;
      secondsWaited++;
    }

    return message;
  }

  private static Light parseLight(String line) {
    int[] tokens = LIGHT_SPLITTER.splitToStream(line).mapToInt(Integer::parseInt).toArray();
    return new Light(new Point(tokens[0], tokens[1]), new Point(tokens[2], tokens[3]));
  }

  private static class Light {
    Point location;
    Point velocity;

    Light(Point location, Point velocity) {
      this.location = location;
      this.velocity = velocity;
    }

    void advanceTime() {
      location.translate(velocity.x, velocity.y);
    }
  }
}
