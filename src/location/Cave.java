package location;

import java.util.ArrayList;
import java.util.List;

import location.coordinate.Coordinate;
import location.coordinate.CoordinateImpl;

/**
 * The Cave implements {@link LocationUpdateState} and represents a location in the
 * {@link dungeon.Dungeon}. Each cave has either 1, 3, or 4 possible exits. A tunnel is a special
 * type of cave that has exactly 2 exits.
 */
public class Cave implements LocationUpdateState {

  private final int id;
  private final Coordinate coordinates;
  private List<Direction> validMoves;
  private List<Treasure> treasures;

  /**
   * Constructs a cave.
   *
   * @param id  the id of the cave.
   * @param row the row (x) coordinate in the dungeon grid.
   * @param column the column (y) coordinate in the dungeon grid.
   * @throws IllegalArgumentException <ul><li>if {@code row} is negative</li>
   *                                  <li>if {@code column} is negative</li></ul>
   */
  public Cave(int id, int row, int column) {
    this.id = id;
    this.coordinates = new CoordinateImpl(row, column);
    this.validMoves = new ArrayList<>();
    this.treasures = new ArrayList<>();
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public Coordinate getCoordinates() {
    return this.coordinates;
  }

  @Override
  public List<Treasure> getTreasures() {
    return new ArrayList<>(this.treasures);
  }

  @Override
  public List<Direction> getPossibleMoves() {
    return new ArrayList<>(this.validMoves);
  }

  @Override
  public boolean isTunnel() {
    return this.validMoves.size() == 2;
  }

  @Override
  public void setValidMoves(List<Direction> validMoves) {
    this.validMoves = new ArrayList<>(validMoves);
  }

  @Override
  public void addTreasures(List<Treasure> treasures) {
    this.treasures = new ArrayList<>(treasures);
  }

  @Override
  public void removeTreasures(List<Treasure> treasures) {
    List<Treasure> treasureList = new ArrayList<>(treasures);
    this.treasures.removeAll(treasureList);
  }

  /**
   * Returns the string representation of the location. "T" in case of a tunnel and "C" in case of
   * a cave.
   *
   * @return the string representation.
   */
  @Override
  public String toString() {
    if (isTunnel()) {
      return "T";
    } else {
      return "C";
    }
  }
}
