package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;

import java.util.Arrays;
import java.util.List;

public class Day9 extends Day {
  public static void main(String[] args) {
    new Day9().solve();
  }

  private static final int NUM_PLAYERS = 447, MAX_MARBLE = 71510;

  @Override
  protected Object part1(List<String> lines) {
    return maxScore(NUM_PLAYERS, MAX_MARBLE);
  }

  @Override
  protected Object part2(List<String> lines) {
    return maxScore(NUM_PLAYERS, MAX_MARBLE * 100);
  }

  private static long maxScore(int numPlayers, long maxMarble) {
    long[] playerPoints = new long[numPlayers];
    Marble zeroth = new Marble(0);
    Marble current = zeroth;
    for (int p = 0, m = 1; m <= maxMarble; p = (p + 1) % numPlayers, m++) {
      if (m % 23 == 0) {
        playerPoints[p] += m;
        Marble seventh = current.getPrevAtIndex(7);
        playerPoints[p] += seventh.points;
        seventh.remove();
        current = seventh.next;
      } else {
        Marble next = new Marble(m);
        current.next.addNext(next);
        current = next;
      }
      // printMarbles(zeroth);
    }
    return Arrays.stream(playerPoints).max().getAsLong();
  }

  private static void printMarbles(Marble marble) {
    Marble cur = marble;
    do {
      System.out.print(cur.points + ", ");
      cur = cur.next;
    } while (cur != marble);
    System.out.println();
  }

  private static class Marble {
    final int points;
    Marble prev = this, next = this;

    Marble(int points) {
      this.points = points;
    }

    void addNext(Marble marble) {
      marble.prev = this;
      marble.next = next;
      next.prev = marble;
      next = marble;
    }

    void remove() {
      prev.next = next;
      next.prev = prev;
    }

    Marble getPrevAtIndex(int index) {
      if (index == 0) {
        return this;
      }
      return prev.getPrevAtIndex(index - 1);
    }
  }
}
