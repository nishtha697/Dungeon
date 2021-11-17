package dungeon;

import java.util.Objects;

/**
 * This represents an undirected edge of the {@link Dungeon}. A package-private class.
 */
class Edge {

  private final int x;
  private final int y;

  /**
   * Constructs an undirected edge.
   *
   * @param x the id of the first location of the edge.
   * @param y the id of the second location of the edge.
   */
  Edge(int x, int y) {
    this.x = x;
    this.y = y;
  }

  int getFirstLocation() {
    return this.x;
  }

  int getSecondLocation() {
    return this.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof Edge) {
      Edge edge = (Edge) o;
      return (this.x == edge.x && this.y == edge.y) || (this.x == edge.y && this.y == edge.x);
    }
    return false;
  }
}
