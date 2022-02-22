package com.dixie.adventofcode.lib;

import java.util.Objects;

public class Point3D {
  public int x, y, z;

  public Point3D() {
    this(0, 0, 0);
  }

  public Point3D(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Point3D(Point3D p) {
    this.x = p.x;
    this.y = p.y;
    this.z = p.z;
  }

  public void set(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void set(Point3D p) {
    this.x = p.x;
    this.y = p.y;
    this.z = p.z;
  }

  public void translate(Point3D delta) {
    translate(delta.x, delta.y, delta.z);
  }

  public void translate(int dx, int dy, int dz) {
    this.x += dx;
    this.y += dy;
    this.z += dz;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Point3D point3D = (Point3D) o;
    return x == point3D.x && y == point3D.y && z == point3D.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d, %d)", x, y, z);
  }
}
