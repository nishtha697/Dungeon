import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import location.Cave;
import location.Direction;
import location.LocationUpdateState;
import location.Treasure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link Cave}.
 */
public class CaveTest {

  LocationUpdateState cave;

  @Before
  public void setUp() {
    cave = new Cave(1, 0, 1);
  }

  @Test
  public void testGetId() {
    assertEquals(1, cave.getId());
  }

  @Test
  public void testGetCoordinates() {
    assertEquals(0, cave.getCoordinates().getX());
    assertEquals(1, cave.getCoordinates().getY());
  }

  @Test
  public void testGetTreasures() {
    assertEquals(Collections.emptyList(), cave.getTreasures());
    cave.addTreasures(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.DIAMOND), cave.getTreasures());
  }

  @Test
  public void testGetPossibleMoves() {
    assertEquals(Collections.emptyList(), cave.getPossibleMoves());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH));
    assertEquals(List.of(Direction.SOUTH, Direction.NORTH), cave.getPossibleMoves());
  }

  @Test
  public void testTunnel() {
    assertFalse(cave.isTunnel());
    cave.setValidMoves(List.of(Direction.SOUTH));
    assertFalse(cave.isTunnel());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH));
    assertTrue(cave.isTunnel());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH, Direction.EAST));
    assertFalse(cave.isTunnel());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST));
    assertFalse(cave.isTunnel());
  }

  @Test
  public void testSetValidMoves() {
    assertEquals(Collections.emptyList(), cave.getPossibleMoves());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST));
    assertEquals(List.of(Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST),
            cave.getPossibleMoves());
  }

  @Test
  public void testAddTreasures() {
    assertEquals(Collections.emptyList(), cave.getTreasures());
    cave.addTreasures(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE));
    assertEquals(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE), cave.getTreasures());
  }

  @Test
  public void testRemoveTreasures() {
    assertEquals(Collections.emptyList(), cave.getTreasures());
    List<Treasure> addedTreasures = new ArrayList<>(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE));
    cave.addTreasures(addedTreasures);
    assertEquals(List.of(Treasure.DIAMOND, Treasure.SAPPHIRE), cave.getTreasures());
    cave.removeTreasures(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.SAPPHIRE), cave.getTreasures());
    cave.removeTreasures(List.of(Treasure.SAPPHIRE));
    assertEquals(Collections.emptyList(), cave.getTreasures());
  }

  @Test
  public void testToString() {
    assertEquals("C", cave.toString());
    cave.setValidMoves(List.of(Direction.SOUTH));
    assertEquals("C", cave.toString());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH));
    assertEquals("T", cave.toString());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH, Direction.EAST));
    assertEquals("C", cave.toString());
    cave.setValidMoves(List.of(Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST));
    assertEquals("C", cave.toString());
  }
}