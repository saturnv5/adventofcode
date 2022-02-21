package com.dixie.adventofcode.lib;

public record Cuboid(Point3D min, Point3D max) {
  public int width() {
    return max.x - min.x;
  }

  public int height() {
    return max.y - min.y;
  }

  public int depth() {
    return max.z - min.z;
  }

  public int size() {
    return width() + height() + depth();
  }

  public boolean contains(Point3D point) {
    if (point.x < min.x || point.x > max.x) return false;
    if (point.y < min.y || point.y > max.y) return false;
    if (point.z < min.z || point.z > max.z) return false;
    return true;
  }

  public boolean intersects(Cuboid cuboid) {
    if (cuboid.max.x < min.x || cuboid.min.x > max.x) return false;
    if (cuboid.max.y < min.y || cuboid.min.y > max.y) return false;
    if (cuboid.max.z < min.z || cuboid.min.z > max.z) return false;
    return true;
  }
}
