package com.dixie.adventofcode.aoc2022

import com.dixie.adventofcode.klib.Day

class Day7 : Day() {
  private val topLevel = Dir("/", null)
  private lateinit var allDirs: List<Dir>

  override fun prepare() {
    // Parse input
    var index = 1
    var cur = topLevel
    while (index < lines.size) {
      val line = lines[index++]
      if (line.startsWith("$ cd")) {
        cur = cur.changeDir(line.substring(5))
      } else if (line == "$ ls") {
        index = addItems(cur, index)
      }
    }

    // Traverse tree into a list of dirs
    val dirs = mutableListOf<Dir>()
    val deque = ArrayDeque<Dir>()
    deque.addLast(topLevel)
    while (deque.isNotEmpty()) {
      val dir = deque.removeFirst()
      dirs.add(dir)
      dir.dirs.forEach(deque::addLast)
    }
    allDirs = dirs
  }

  private fun addItems(cur: Dir, index: Int): Int {
    var i = index
    while (i < lines.size && !lines[i].startsWith("$")) {
      val line = lines[i++]
      if (line.startsWith("dir")) {
        cur.dirs.add(Dir(line.substring(4), cur))
      } else {
        val tokens = line.split(' ')
        cur.files.add(File(tokens[1], tokens[0].toLong()))
      }
    }
    return i
  }

  override fun part1(): Any {
    return allDirs.map(Dir::size).filter { it <= 100_000L }.sum()
  }

  override fun part2(): Any {
    val freeSpace = 70_000_000L - topLevel.size
    return allDirs.map(Dir::size).sorted().find { freeSpace + it >= 30_000_000L }!!
  }
}

private data class Dir(val name: String, val parent: Dir?) {
  val dirs = mutableSetOf<Dir>()
  val files = mutableSetOf<File>()
  val size: Long by lazy { dirs.map(Dir::size).sum() + files.map(File::size).sum() }

  fun changeDir(dirName: String): Dir {
    if (dirName == "..") return parent!!
    var cd = dirs.find { it.name == dirName }
    if (cd == null) {
      cd = Dir(dirName, this)
      dirs.add(cd)
    }
    return cd
  }
}

private data class File(val name: String, val size: Long)

fun main() {
  Day7().run()
}
