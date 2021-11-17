package location.coordinate;

/**
 * The coordinate.Coordinate represents the coordinates of the {@link location.Location} in the
 * {@link dungeon.Dungeon} grid. x represents the x-coordinate and y represents the y coordinate.
 */
public interface Coordinate {

  /**
   * Returns the x coordinate of the location in the 2D dungeon grid.
   *
   * @return the x coordinate. This will never be negative.
   */
  int getX();

  /**
   * Returns the y coordinate of the location in the 2D dungeon grid.
   *
   * @return the y coordinate. This will never be negative.
   */
  int getY();
}
