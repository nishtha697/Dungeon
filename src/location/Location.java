package location;

import java.util.List;

import location.coordinate.Coordinate;

/**
 * The location.Location represents a location or cave in the {@link dungeon.Dungeon}. Each location
 * in the grid represents a location in the dungeon where a player can explore and can be connected
 * to at most four (4) other locations: one to the north, one to the east, one to the south, and one
 * to the west.
 */
public interface Location {

  /**
   * Gets the id of the location.
   *
   * @return the id.
   */
  int getId();

  /**
   * Returns the {@link Coordinate} containing x and y coordinates of the location in the 2D
   * dungeon grid.
   *
   * @return the coordinates.
   */
  Coordinate getCoordinates();

  /**
   * Returns the {@link List} of {@link Treasure}(s) located at the location.
   *
   * @return the list of treasures.
   */
  List<Treasure> getTreasures();

  /**
   * Returns the {@link List} of possible {@link Direction}(s) the player ca go from the location.
   *
   * @return the list of possible directions or moves.
   */
  List<Direction> getPossibleMoves();

  /**
   * Returns if the location is a tunnel. A location with exactly two paths is a tunnel.
   *
   * @return {@code true} if tunnel otherwise {@code false}.
   */
  boolean isTunnel();
}
