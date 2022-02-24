package com.dixie.adventofcode.aoc2018;

import com.dixie.adventofcode.lib.Day;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day24 extends Day {
  public static void main(String[] args) {
    new Day24().solve();
  }

  private enum Team { IMMUNE_SYSTEM, INFECTION }

  private static final Splitter INPUT_SPLITTER =
      Splitter.onPattern(" |; |, |\\(|\\)").trimResults().omitEmptyStrings();

  private final AtomicInteger immuneBoost = new AtomicInteger();
  private List<Group> groups;

  @Override
  protected void prepare(List<String> lines) {
    groups = new ArrayList<>();
    Team team = Team.IMMUNE_SYSTEM;
    for (String line : lines.subList(1, lines.size())) {
      if (line.equals("Infection:")) {
        team = Team.INFECTION;
      } else if (!line.isEmpty()) {
        groups.add(Group.parse(team, immuneBoost, line));
      }
    }
  }

  @Override
  protected Object part1(List<String> lines) {
    return performCombat().stream()
        .map(Group::numUnitsRemaining)
        .mapToInt(AtomicInteger::get)
        .sum();
  }

  @Override
  protected Object part2(List<String> lines) {
    immuneBoost.set(1);
    int min = 0;
    int max = immuneBoost.get();
    // Determine min/max bounds.
    while (performCombat().get(0).team() == Team.INFECTION) {
      min = max;
      max *= 2;
      immuneBoost.set(max);
    }
    // Binary search.
    while (min < max) {
      int mid = (min + max) / 2;
      immuneBoost.set(mid);
      List<Group> winners = performCombat();
      if (winners == null || winners.get(0).team() == Team.INFECTION) {
        min = mid + 1;
      } else {
        max = mid;
      }
    }
    immuneBoost.set(min);
    return performCombat().stream()
        .map(Group::numUnitsRemaining)
        .mapToInt(AtomicInteger::get)
        .sum();
  }

  private List<Group> performCombat() {
    groups.forEach(Group::reset);
    List<Group> immuneTeam = groups.stream().filter(g -> g.team() == Team.IMMUNE_SYSTEM).toList();
    List<Group> infectTeam = groups.stream().filter(g -> g.team() == Team.INFECTION).toList();
    while (!immuneTeam.isEmpty() && !infectTeam.isEmpty()) {
      BiMap<Group, Group> selection = performSelection(immuneTeam, infectTeam);
      AtomicBoolean combatProgressed = new AtomicBoolean(false);
      selection.keySet()
          .stream()
          .sorted(Comparator.comparingInt(Group::initiative).reversed())
          .filter(selection::containsKey)
          .forEach(g -> {
            if (g.performAttack(selection.get(g))) {
              combatProgressed.set(true);
            }
          });
      if (!combatProgressed.get()) {
        return null;
      }
      immuneTeam = immuneTeam.stream().filter(g -> g.numUnitsRemaining().get() > 0).toList();
      infectTeam = infectTeam.stream().filter(g -> g.numUnitsRemaining().get() > 0).toList();
      // printGroups(immuneTeam, infectTeam);
    }
    return immuneTeam.isEmpty() ? infectTeam : immuneTeam;
  }

  private static void printGroups(List<Group> immuneTeam, List<Group> infectTeam) {
    System.out.println("Immune system:");
    immuneTeam.forEach(System.out::println);
    System.out.println("Infection:");
    infectTeam.forEach(System.out::println);
    System.out.println();
  }

  private static BiMap<Group, Group> performSelection(List<Group> immuneTeam,
      List<Group> infectTeam) {
    BiMap<Group, Group> selection = HashBiMap.create(immuneTeam.size() + infectTeam.size());
    Stream.concat(immuneTeam.stream(), infectTeam.stream())
        .sorted(Comparator.comparingInt(Group::effectivePower)
            .thenComparingInt(Group::initiative)
            .reversed())
        .forEach(g -> {
          List<Group> enemies = g.team() == Team.INFECTION ? immuneTeam : infectTeam;
          Optional<Group> target = enemies.stream()
              .filter(Predicate.not(selection::containsValue))
              .filter(e -> g.damagePotential(e) > 0)
              .max(Comparator.comparingInt(g::damagePotential)
                  .thenComparingInt(Group::effectivePower)
                  .thenComparingInt(Group::initiative));
          target.ifPresent(group -> selection.put(g, group));
    });
    return selection;
  }

  private record Group(Team team, AtomicInteger numUnitsRemaining, int startNumUnits, int hitPoints,
                       int attack, int initiative, String attackType, List<String> immunities,
                       List<String> weaknesses, AtomicInteger boost) {

    void reset() {
      numUnitsRemaining.set(startNumUnits);
    }

    int effectivePower() {
      return numUnitsRemaining.get() * (attack + boost.get());
    }

    int damagePotential(Group enemy) {
      if (numUnitsRemaining.get() <= 0) {
        return 0;
      } else if (enemy.immunities.contains(attackType)) {
        return 0;
      } else if (enemy.weaknesses.contains(attackType)) {
        return 2 * effectivePower();
      }
      return effectivePower();
    }

    boolean performAttack(Group enemy) {
      if (enemy.numUnitsRemaining.get() <= 0) return false;
      int damage = damagePotential(enemy);
      if (damage == 0) return false;
      int numUnitsEliminated = damage / enemy.hitPoints;
      if (numUnitsEliminated == 0) return false;
      enemy.numUnitsRemaining.set(enemy.numUnitsRemaining.get() - numUnitsEliminated);
      return true;
    }

    static Group parse(Team team, AtomicInteger immuneBoost, String line) {
      List<String> tokens = INPUT_SPLITTER.splitToList(line);
      int numUnits = Integer.parseInt(tokens.get(0));
      int hitPoints = Integer.parseInt(tokens.get(4));
      int attack = Integer.parseInt(tokens.get(tokens.size() - 6));
      String attackType = tokens.get(tokens.size() - 5);
      int initiative = Integer.parseInt(tokens.get(tokens.size() - 1));
      List<String> immunities = new ArrayList<>();
      List<String> weaknesses = new ArrayList<>();
      List<String> currentList = null;
      for (int i = 7; i < tokens.size() - 11; i++) {
        String token = tokens.get(i);
        if (token.equals("immune")) {
          currentList = immunities;
          i++;
        } else if (token.equals("weak")) {
          currentList = weaknesses;
          i++;
        } else {
          currentList.add(token);
        }
      }
      return new Group(team, new AtomicInteger(numUnits), numUnits, hitPoints, attack, initiative,
          attackType, immunities, weaknesses,
          team == Team.IMMUNE_SYSTEM ? immuneBoost : new AtomicInteger(0));
    }
  }
}
