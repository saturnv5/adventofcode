package com.dixie.adventofcode.lib;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Space2D<T> {
  private final HashMap<Point, T> space = new HashMap<>();
  private final Rectangle bounds;

  public static <T> Space2D<T> copyOf(Space2D<T> space) {
    Space2D<T> copy = new Space2D<>();
    copy.bounds.setBounds(space.bounds);
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

  public Space2D() {
    bounds = new Rectangle();
  }

  public Space2D(int width, int height) {
    this(width, height, null);
  }

  public Space2D(int width, int height, T defaultValue) {
    this(new Rectangle(width, height), defaultValue);
  }

  public Space2D(Rectangle bounds, T defaultValue) {
    this.bounds = bounds;
    if (defaultValue != null) {
      streamAllPointsInBounds().forEach(p -> space.put(p, defaultValue));
    }
  }

  public Rectangle getBounds() {
    return bounds;
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
    Point p = new Point(x, y);
    if (value == null) {
      removeValueAt(p);
    } else {
      space.put(p, value);
      if (space.size() == 1) {
        bounds.setBounds(p.x, p.y, 0, 0);
      } else {
        bounds.add(p);
      }
    }
  }

  public void setValueAt(Point p, T value) {
    if (value == null) {
      removeValueAt(p);
    } else {
      space.put(new Point(p), value);
      if (space.size() == 1) {
        bounds.setBounds(p.x, p.y, 0, 0);
      } else {
        bounds.add(p);
      }
    }
  }

  public void removeValueAt(int x, int y) {
    space.remove(new Point(x, y));
  }

  public void removeValueAt(Point p) {
    space.remove(p);
  }

  public Stream<Point> streamOccurrencesOf(T value) {
    return streamOccurrencesOf(value::equals).map(Map.Entry::getKey);
  }

  public Stream<Map.Entry<Point, T>> streamOccurrencesOf(Predicate<T> matcher) {
    return space.entrySet().stream().filter(e -> matcher.test(e.getValue()));
  }

  public Stream<Point> streamAllPoints() {
    return space.keySet().stream();
  }

  public Stream<Point> streamAllPointsInBounds() {
    return streamAllPointsInBounds(bounds);
  }

  public static Stream<Point> streamAllPointsInBounds(Rectangle bounds) {
    return IntStream.range(bounds.y, bounds.y + bounds.height)
        .boxed()
        .flatMap(
            y -> IntStream.range(bounds.x, bounds.x + bounds.width).mapToObj(x -> new Point(x, y)));
  }

  public String toPrintableImage(Function<T, String> valueConverter) {
    StringBuilder sb = new StringBuilder();
    Point p = new Point();
    for (int y = bounds.y; y <= bounds.y + bounds.height; y++) {
      sb.append('\n');
      for (int x = bounds.x; x <= bounds.x + bounds.width; x++) {
        p.move(x, y);
        sb.append(valueConverter.apply(getValueAt(p)));
      }
    }
    return sb.toString();
  }
}
