package com.dixie.adventofcode.lib;

import java.util.List;

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

  /** Iff the given point is outside this cuboid, expand it to include the given point. */
  public void add(Point3D point) {
    if (point.x < min.x) min.x = point.x;
    if (point.x > max.x) max.x = point.x;
    if (point.y < min.y) min.y = point.y;
    if (point.y > max.y) max.y = point.y;
    if (point.z < min.z) min.z = point.z;
    if (point.z > max.z) max.z = point.z;
  }

  public boolean intersects(Cuboid cuboid) {
    if (cuboid.max.x < min.x || cuboid.min.x > max.x) return false;
    if (cuboid.max.y < min.y || cuboid.min.y > max.y) return false;
    if (cuboid.max.z < min.z || cuboid.min.z > max.z) return false;
    return true;
  }

  public int distX(Point3D point) {
    if (point.x >= min.x && point.x <= max.x) return 0;
    int toMin = Math.abs(min.x - point.x);
    int toMax = Math.abs(point.x - max.x);
    return Math.min(toMin, toMax);
  }

  public int distY(Point3D point) {
    if (point.y >= min.y && point.y <= max.y) return 0;
    int toMin = Math.abs(min.y - point.y);
    int toMax = Math.abs(point.y - max.y);
    return Math.min(toMin, toMax);
  }

  public int distZ(Point3D point) {
    if (point.z >= min.z && point.z <= max.z) return 0;
    int toMin = Math.abs(min.z - point.z);
    int toMax = Math.abs(point.z - max.z);
    return Math.min(toMin, toMax);
  }

  public int dist(Point3D point) {
    return distX(point) + distY(point) + distZ(point);
  }

  public List<Cuboid> subdivide() {
    int midX = (max.x + min.x) / 2, midY = (max.y + min.y) / 2, midZ = (max.z + min.z) / 2;
    return List.of(
        new Cuboid(new Point3D(min), new Point3D(midX, midY, midZ)),
        new Cuboid(new Point3D(midX + 1, min.y, min.z), new Point3D(max.x, midY, midZ)),
        new Cuboid(new Point3D(min.x, midY + 1, min.z), new Point3D(midX, max.y, midZ)),
        new Cuboid(new Point3D(midX + 1, midY + 1, min.z), new Point3D(max.x, max.y, midZ)),
        new Cuboid(new Point3D(min.x, min.y, midZ + 1), new Point3D(midX, midY, max.z)),
        new Cuboid(new Point3D(midX + 1, min.y, midZ + 1), new Point3D(max.x, midY, max.z)),
        new Cuboid(new Point3D(min.x, midY + 1, midZ + 1), new Point3D(midX, max.y, max.z)),
        new Cuboid(new Point3D(midX + 1, midY + 1, midZ + 1), new Point3D(max)));
  }
}
