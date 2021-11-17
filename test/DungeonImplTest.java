import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Direction;
import location.Treasure;
import player.Player;
import random.RandomFactory;
import random.RandomGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DungeonImpl}.
 */
public class DungeonImplTest {

  Dungeon dungeon;
  RandomGenerator rand;
  RandomGenerator randTrue;
  Player player;
  boolean isWrapping = false;

  @Before
  public void setUp() {
    rand = new RandomFactory().getRandomGenerator(false);
    randTrue = new RandomFactory().getRandomGenerator(true);
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", rand);
    player = dungeon.getPlayer();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNumberOfCellsForNonWrappingDungeon() {
    new DungeonImpl(4, 4, 4, false,
            25, "Nishtha", rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidNumberOfCellsForWrappingDungeon() {
    new DungeonImpl(2, 4, 0, true,
            25, "Nishtha", rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityForWrappingDungeon() {
    new DungeonImpl(4, 4, 18, true,
            25, "Nishtha", rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityForNonWrappingDungeon() {
    new DungeonImpl(5, 4, 13, false,
            25, "Nishtha", rand);
  }

  @Test
  public void testValidInterconnectivityForWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 25, true,
            25, "Nishtha", rand);
    StringBuilder dungeonString = new StringBuilder();
    dungeonString.append("     |          |          |          |     \n"
            + "--- [P] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [D] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "--- [C] ------ [C] ------ [C] ------ [C] ---\n"
            + "     |          |          |          |     \n");
    assertEquals(dungeonString.toString(), dungeon.toString());
  }

  @Test
  public void testValidInterconnectivityForNonWrappingDungeon() {
    dungeon = new DungeonImpl(5, 4, 12, false,
            25, "Nishtha", rand);
    StringBuilder dungeonString = new StringBuilder();
    dungeonString.append("                                            \n"
            + "    [T] ------ [P] ------ [C] ------ [T]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [C] ------ [C] ------ [C] ------ [D]    \n"
            + "     |          |          |          |     \n"
            + "     |          |          |          |     \n"
            + "    [T] ------ [C] ------ [C] ------ [T]    \n"
            + "                                            \n");
    assertEquals(dungeonString.toString(), dungeon.toString());

  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidInterconnectivityForNegativePercentageOfTreasures() {
    new DungeonImpl(5, 4, 12, false,
            -25, "Nishtha", rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidInterconnectivityForMoreThan100PercentageOfTreasures() {
    new DungeonImpl(5, 4, 12, false,
            101, "Nishtha", rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidInterconnectivityForNullRandomGenerator() {
    new DungeonImpl(5, 4, 12, false,
            100, "Nishtha", null);
  }

  @Test
  public void testGetPlayer() {
    assertEquals("Nishtha", dungeon.getPlayer().getName());
    assertEquals(player, dungeon.getPlayer());

  }

  @Test
  public void testGetPlayerLocation() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    assertEquals(dungeon.getPlayerLocation(), dungeon.getPlayerLocation());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testDungeonImplNullPlayer() {
    new DungeonImpl(5, 4, 12, false,
            100, null, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDungeonImplEmptyPlayer() {
    new DungeonImpl(5, 4, 12, false,
            100, "", rand);
  }

  @Test
  public void testGetStartingCave() {
    assertEquals(1, dungeon.getStartingCave().getId());
  }

  @Test
  public void testGetDestinationCave() {
    assertEquals(15, dungeon.getDestinationCave().getId());
  }

  @Test
  public void testMovePlayer() {
    dungeon.movePlayer(Direction.SOUTH);
    assertEquals(5, dungeon.getPlayerLocation().getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePlayerNullDirection() {
    dungeon.movePlayer(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePlayerInvalidDirection() {
    dungeon.movePlayer(Direction.NORTH);
  }

  @Test
  public void testCollectAllTreasures() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectAllTreasures();
    assertEquals(Collections.emptyList(), dungeon.getPlayerLocation().getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void testCollectTreasure() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectTreasure(List.of(Treasure.RUBY));
    assertEquals(Collections.emptyList(), dungeon.getPlayerLocation().getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void testCollectTreasureNotAvailableTreasure() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectTreasure(List.of(Treasure.DIAMOND));
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCollectTreasureNullTreasureList() {
    dungeon.collectTreasure(null);
  }

  @Test
  public void testCollectTreasureEmptyTreasureList() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectTreasure(Collections.emptyList());
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void testCollectTreasureNullTreasureElement() {
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    List<Treasure> givenTreasure = new ArrayList<>();
    givenTreasure.add(null);
    dungeon.collectTreasure(givenTreasure);
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void testIsDestinationReached() {
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.SOUTH);
    dungeon.movePlayer(Direction.EAST);
    dungeon.movePlayer(Direction.SOUTH);
    dungeon.movePlayer(Direction.SOUTH);
    dungeon.movePlayer(Direction.EAST);
    assertTrue(dungeon.isDestinationReached());
  }

  @Test
  public void testToString() {
    StringBuilder dungeonString = new StringBuilder();
    dungeonString.append("                                            \n"
            + "    [T] ------ [P] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C] ------ [C] ------ [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [T]        [T]        [C] ------ [D]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [T]        [T]        [C] ------ [C]    \n"
            + "     |          |          |                \n"
            + "     |          |          |                \n"
            + "    [C]        [C]        [T] ------ [C]    \n"
            + "                                            \n");
    assertEquals(dungeonString.toString(), dungeon.toString());

  }

  @Test
  public void testWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 22, true,
            25, "Nishtha", rand);
    player = dungeon.getPlayer();
    assertEquals(0, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.WEST);
    assertEquals(3, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.EAST);
    assertEquals(0, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.NORTH);
    assertEquals(20, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.SOUTH);
    assertEquals(0, dungeon.getPlayerLocation().getId());
  }

  @Test
  public void testNonWrappingDungeon() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.EAST);
    assertEquals(2, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.SOUTH);
    assertEquals(6, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.WEST);
    assertEquals(5, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.NORTH);
    assertEquals(1, dungeon.getPlayerLocation().getId());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testNonWrappingDungeonIllegalMove() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.NORTH);
  }

  @Test
  public void testEveryNodeCanBeReached() {
    Set<Integer> visited = new HashSet<>();
    do {
      visited.add(dungeon.getPlayerLocation().getId());
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (visited.size() != 24);
    Set<Integer> expectedVisitedNodes = new HashSet<>();
    expectedVisitedNodes.addAll(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
            17, 18, 19, 20, 21, 22, 23));
    assertEquals(expectedVisitedNodes, visited);
  }

  @Test
  public void testPercentageOfTreasures() {
    Set<Integer> visited = new HashSet<>();
    int numberOfCavesWithTreasures = 0;
    int numberOfCaves = 0;
    do {
      if (dungeon.getPlayerLocation().getTreasures().size() > 0) {
        numberOfCavesWithTreasures++;
      }
      dungeon.collectAllTreasures();
      if (!dungeon.getPlayerLocation().isTunnel() && !visited
              .contains(dungeon.getPlayerLocation().getId())) {
        numberOfCaves++;
      }
      visited.add(dungeon.getPlayerLocation().getId());
      List<Direction> possibleMoves = dungeon.getPlayerLocation().getPossibleMoves();
      Direction move = possibleMoves.get(randTrue.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
    }
    while (visited.size() != 24);
    assertEquals((int) (numberOfCaves * 0.25), numberOfCavesWithTreasures);
  }

  @Test
  public void testShortestPath5() {
    int pathLength = 0;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.EAST);
    pathLength++;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.EAST);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertTrue(pathLength >= 5);
  }

  @Test
  public void testShortestPathSmallestAndMostInterconnectedWrappingDungeon() {
    dungeon = new DungeonImpl(6, 4, 25, true,
            25, "Nishtha", rand);
    int pathLength = 0;
    dungeon.movePlayer(Direction.NORTH);
    pathLength++;
    dungeon.movePlayer(Direction.NORTH);
    pathLength++;
    dungeon.movePlayer(Direction.NORTH);
    pathLength++;
    dungeon.movePlayer(Direction.EAST);
    pathLength++;
    dungeon.movePlayer(Direction.EAST);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertEquals(5, pathLength);
  }

  @Test
  public void testShortestPathSmallestAndMostInterconnectedNonWrappingDungeon() {
    dungeon = new DungeonImpl(5, 4, 12, false,
            25, "Nishtha", rand);
    int pathLength = 0;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.EAST);
    pathLength++;
    dungeon.movePlayer(Direction.EAST);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertEquals(5, pathLength);
  }

  @Test
  public void testShortestPathZeroInterconnectivity() {
    dungeon = new DungeonImpl(3, 4, 0, true,
            25, "Nishtha", rand);
    int pathLength = 0;
    dungeon.movePlayer(Direction.WEST);
    pathLength++;
    dungeon.movePlayer(Direction.WEST);
    pathLength++;
    dungeon.movePlayer(Direction.WEST);
    pathLength++;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    dungeon.movePlayer(Direction.SOUTH);
    pathLength++;
    assertTrue(dungeon.isDestinationReached());
    assertEquals(5, pathLength);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testShortestPathMaxInterconnectivitySmallestWrappingDungeon() {
    dungeon = new DungeonImpl(3, 4, 13, true,
            25, "Nishtha", rand);
  }

  @Test
  public void testPlayerStartsAtStartingLocation() {
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getStartingCave());
    assertEquals(dungeon.getPlayer().getLocation().getId(), dungeon.getStartingCave().getId());
  }

  @Test
  public void testPlayerTraversalFromStartToEnd() {
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getStartingCave());
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.SOUTH);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.SOUTH);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.EAST);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.SOUTH);
    assertFalse(dungeon.isDestinationReached());
    dungeon.movePlayer(Direction.EAST);
    assertTrue(dungeon.isDestinationReached());
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getDestinationCave());
  }

  @Test
  public void testPlayerTraversalFromStartToEndRandomCave() {
    dungeon = new DungeonImpl(6, 4, 4, isWrapping,
            25, "Nishtha", randTrue);
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getStartingCave());
    do {
      dungeon.movePlayer(dungeon.getPlayerLocation().getPossibleMoves().get(randTrue
              .getRandom(dungeon.getPlayerLocation().getPossibleMoves().size(), 0)));
    }
    while (!dungeon.isDestinationReached());
    assertTrue(dungeon.isDestinationReached());
    assertEquals(dungeon.getPlayer().getLocation(), dungeon.getDestinationCave());
  }

  @Test
  public void testPlayerDescription() {
    assertEquals("Nishtha", dungeon.getPlayer().getName());
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayer().getLocation().getTreasures());
    Map<Treasure, Integer> collectedTreasures = new HashMap<>();
    collectedTreasures.put(Treasure.SAPPHIRE, 0);
    collectedTreasures.put(Treasure.DIAMOND, 0);
    collectedTreasures.put(Treasure.RUBY, 0);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
    dungeon.collectAllTreasures();
    assertEquals(Collections.emptyList(), dungeon.getPlayer().getLocation().getTreasures());
    collectedTreasures.put(Treasure.RUBY, 1);
    assertEquals(collectedTreasures, dungeon.getPlayer().getCollectedTreasures());
  }

  @Test
  public void getLocationDescription() {
    assertEquals(1, dungeon.getPlayerLocation().getId());
    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(List.of(Treasure.RUBY), dungeon.getPlayerLocation().getTreasures());
  }

  @Test
  public void testMovingInFourDirections() {
    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(1, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.EAST);

    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(2, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(2, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.SOUTH);

    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(2, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(6, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.WEST);

    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(5, dungeon.getPlayerLocation().getId());
    dungeon.movePlayer(Direction.NORTH);

    assertEquals(0, dungeon.getPlayerLocation().getCoordinates().getX());
    assertEquals(1, dungeon.getPlayerLocation().getCoordinates().getY());
    assertEquals(1, dungeon.getPlayerLocation().getId());
  }
}


