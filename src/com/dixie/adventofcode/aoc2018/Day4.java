package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.dixie.adventofcode.lib.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day4 extends Day {
  public static void main(String[] args) {
    new Day4().solve();
  }

  private static final int SLEEP = Integer.MIN_VALUE;
  private static final int WAKE = SLEEP + 1;
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  @Override
  protected Object part1(List<String> lines) {
    SleepPattern maxSleep = parseSleepPatterns(lines).stream()
        .max(Comparator.comparingInt(s -> s.totalSleepMinutes))
        .get();
    return maxSleep.guardId * maxSleep.mostSleptMinute;
  }

  @Override
  protected Object part2(List<String> lines) {
    SleepPattern maxSleep = parseSleepPatterns(lines).stream()
        .max(Comparator.comparingInt(s -> s.maxTimesSleptPerMinute))
        .get();
    return maxSleep.guardId * maxSleep.mostSleptMinute;
  }

  private static Collection<SleepPattern> parseSleepPatterns(List<String> lines) {
    HashMap<Integer, SleepPattern> sleepPatterns = new HashMap<>();
    AtomicInteger lastSleepMinute = new AtomicInteger();
    AtomicInteger lastGuardId = new AtomicInteger();
    lines.stream()
        .map(Day4::parseRecord)
        .sorted(Comparator.comparing(Pair::getFirst))
        .forEach(r -> {
              if (r.second == SLEEP) {
                lastSleepMinute.set(r.first.getMinute());
              } else if (r.second == WAKE) {
                int wakeMinute = r.first.getMinute();
                if (lastGuardId.get() > 0 && lastSleepMinute.get() < wakeMinute) {
                  sleepPatterns.computeIfAbsent(lastGuardId.get(), SleepPattern::new)
                      .recordSleep(lastSleepMinute.get(), wakeMinute);
                }
              } else {
                lastGuardId.set(r.second);
              }
            }
        );
    return sleepPatterns.values();
  }

  private static Pair<LocalDateTime, Integer> parseRecord(String record) {
    LocalDateTime dateTime = LocalDateTime.parse(record.substring(1, 17), DATE_TIME_FORMATTER);
    String text = record.substring(19);
    if (text.startsWith("Guard")) {
      int id = Integer.parseInt(text.split(" ")[1].substring(1));
      return Pair.of(dateTime, id);
    } else if (text.startsWith("wakes")) {
      return Pair.of(dateTime, WAKE);
    } else {
      return Pair.of(dateTime, SLEEP);
    }
  }

  private static class SleepPattern {
    final int guardId;
    final int[] sleepMinutes = new int[60];
    int mostSleptMinute = -1;
    int maxTimesSleptPerMinute = 0;
    int totalSleepMinutes;

    SleepPattern(int guardId) {
      this.guardId = guardId;
    }

    void recordSleep(int sleepMinute, int wakeMinute) {
      IntStream.range(sleepMinute, wakeMinute).forEach(i -> {
        sleepMinutes[i]++;
        if (sleepMinutes[i] > maxTimesSleptPerMinute) {
          maxTimesSleptPerMinute++;
          mostSleptMinute = i;
        }
      });
      totalSleepMinutes += wakeMinute - sleepMinute;
    }
  }
}
