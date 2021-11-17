package player;

import java.util.List;

import location.LocationUpdateState;
import location.Treasure;

/**
 * The player.PlayerUpdateState extends {@link Player} and represents a player that explores the
 * entire world of dungeon. This class is used of internal mutation of the player and is not client
 * facing.
 */
public interface PlayerUpdateState extends Player {

  /**
   * Collects the given type pf {@link Treasure}(s) from the current location of the player if
   * they exist.
   *
   * @param treasures the list of treasures to be collected.
   */
  void collectTreasures(List<Treasure> treasures);

  /**
   * Moves the player to the given location.
   *
   * @param newLocation the new location the player need to be moved to.
   */
  void move(LocationUpdateState newLocation);
}
