package com.google.adventofcode.lib;

import com.google.common.base.Functions;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Space2D<T> {
  private final HashMap<Point, T> space = new HashMap<>();
  private final int width;
  private final int height;

  public static <T> Space2D<T> copyOf(Space2D<T> space) {
    Space2D<T> copy = new Space2D<>(space.width, space.height);
    copy.space.putAll(space.space);
    return copy;
  }

  public static Space2D<Character> parseFromStrings(List<String> lines) {
    return parseFromStrings(lines, c -> (char) c);
  }

  public static <T> Space2D<T> parseFromStrings(List<String> lines, IntFunction<T> charConverter) {
    Space2D<T> space = new Space2D<>(lines.get(0).length(), lines.size());
    for (int y = 0; y < lines.size(); y++) {
      String line = lines.get(y);
      for (int x = 0; x < line.length(); x++) {
        space.setValueAt(x, y, charConverter.apply(line.charAt(x)));
      }
    }
    return space;
  }

  public Space2D(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean hasValueAt(int x, int y) {
    return space.containsKey(new Point(x, y));
  }

  public boolean hasValueAt(Point p) {
    return space.containsKey(p);
  }

  public T getValueAt(int x, int y) {
    return getValueAt(x, y, null);
  }

  public T getValueAt(int x, int y, T defaultValue) {
    return getValueAt(new Point(x, y), defaultValue);
  }

  public T getValueAt(Point p) {
    return getValueAt(p, null);
  }

  public T getValueAt(Point p, T defaultValue) {
    T value = space.get(p);
    return value == null ? defaultValue : value;
  }

  public void setValueAt(int x, int y, T value) {
    setValueAt(new Point(x, y), value);
  }

  public void setValueAt(Point p, T value) {
    if (value == null) {
      removeValueAt(p);
    } else {
      space.put(p, value);
    }
  }

  public void removeValueAt(int x, int y) {
    space.remove(new Point(x, y));
  }

  public void removeValueAt(Point p) {
    space.remove(p);
  }

  public Stream<Point> streamOccurrencesOf(T value) {
    return space.entrySet()
            .stream()
            .filter(e -> value.equals(e.getValue()))
            .map(Map.Entry::getKey);
  }

  public Stream<Point> streamAllPoints() {
    return space.keySet().stream();
  }
}
