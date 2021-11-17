package location;

import java.util.List;

/**
 * The location.LocationUpdateState extends {@link Location} and represents a location in the
 * dungeon grid a player can traverse through. This has functionalities to mutate the
 * state of the location. This class is used internally and is not client facing.
 */
public interface LocationUpdateState extends Location {

  /**
   * Sets the possible/valid moves for the location.
   *
   * @param validMoves the valid moves.
   */
  void setValidMoves(List<Direction> validMoves);

  /**
   * Adds {@link Treasure}(s) to the location.
   *
   * @param treasures the treasures to be added.
   */
  void addTreasures(List<Treasure> treasures);

  /**
   * Removes the given {@link Treasure}(s) from the location if they exist.
   *
   * @param treasures the list of treasures to be removed.
   */
  void removeTreasures(List<Treasure> treasures);
}
