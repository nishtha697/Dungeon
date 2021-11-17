package location.coordinate;

/**
 * The coordinate.CoordinateImpl implements {@link Coordinate} and represents the coordinates of the
 * {@link location.Location} in the {@link dungeon.Dungeon} grid. x represents the x-coordinate and
 * y represents the y coordinate.
 */
public class CoordinateImpl implements Coordinate {

  private final int x;
  private final int y;

  /**
   * Constructs a co-ordinate. Coordinates cannot be negative as it represents the position of a
   * location in the 2-D dungeon grid.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @throws IllegalArgumentException <ul><li>if {@code x} is negative.</li>
   *                                  <li>if {@code x} is negative.</li></ul>
   */
  public CoordinateImpl(int x, int y) {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("x and/or y coordinates cannot be zero");
    }
    this.x = x;
    this.y = y;
  }

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public int getY() {
    return this.y;
  }
}
