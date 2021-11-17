package dungeon;

import java.util.List;

import location.Direction;
import location.Location;
import location.Treasure;
import player.Player;

/**
 * The dungeon.Dungeon represents a world of game that consists of a dungeon, a network of tunnels
 * and caves that are interconnected so that player can explore the entire world by traveling from
 * cave to cave through the tunnels that connect them.
 */
public interface Dungeon {

  /**
   * Returns the {@link Player} that travels the dungeon through tunnels and caves.
   *
   * @return the player.
   */
  Player getPlayer();

  /**
   * Returns the current location of the player.
   *
   * @return the current location of the player.
   */
  Location getPlayerLocation();

  /**
   * Returns the starting cave that is selected randomly as the source of the travel through the
   * dungeon.
   *
   * @return the start cave.
   */
  Location getStartingCave();

  /**
   * Returns the ending cave that is selected randomly as the destination of the travel through the
   * dungeon.
   *
   * @return the destination cave.
   */
  Location getDestinationCave();

  /**
   * Move the {@link Player} one step in the given {@link Direction}.
   *
   * @param direction the direction to be moved in to.
   * @throws IllegalArgumentException <ul><li>if {@code direction} is {@code null}.</li>
   *                                  <li>if {@code direction} is not a valid move form current
   *                                  location.</li></ul>
   */
  void movePlayer(Direction direction);

  /**
   * Makes the {@link Player} collect all the {@link Treasure}(s) available at the current
   * {@link Location}.
   */
  void collectAllTreasures();

  /**
   * Makes the {@link Player} collect only the given type of {@link Treasure}(s) available at the
   * current {@link Location}.
   *
   * @throws IllegalArgumentException if {@code treasures} is {@code null}.
   */
  void collectTreasure(List<Treasure> treasures);

  /**
   * Returns if the destination cave is reached or not.
   *
   * @return {@code true} if current location is destination cave otherwise {@code false}.
   */
  boolean isDestinationReached();
}
