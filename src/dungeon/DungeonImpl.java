package dungeon;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import location.Cave;
import location.Direction;
import location.Location;
import location.LocationUpdateState;
import location.Treasure;
import player.Player;
import player.PlayerImpl;
import player.PlayerUpdateState;
import random.RandomGenerator;

/**
 * The dungeon.DungeonImpl implements {@link Dungeon} and represents a world of game that consists
 * of a dungeon, a network of tunnels and caves that are interconnected so that player can explore
 * the entire world by traveling from cave to cave through the tunnels that connect them.
 */
public class DungeonImpl implements Dungeon {

  private final List<Edge> paths;
  private final List<Edge> potentialPaths;
  private final int rows;
  private final int columns;
  private final int interconnectivity;
  private final boolean isWrapping;
  private final RandomGenerator rand;
  private final PlayerUpdateState player;
  private final LocationUpdateState start;
  private final LocationUpdateState end;
  private final List<List<LocationUpdateState>> dungeon;

  /**
   * Constructs a dungeon.
   *
   * @param rows                  the number of rows.
   * @param columns               the number of columns.
   * @param interconnectivity     the interconnectivity.
   * @param isWrapping            {@code true} is dungeon is wrapping otherwise {@code false}.
   * @param percentageOfTreasures the percentage of caves to have treasures.
   * @param rand                  the random generator.
   * @throws IllegalArgumentException <ul><li>if dungeon is wrapping and the sum of rows and columns
   *                                  is less than 7.</li>
   *                                  <li>if dungeon is non wrapping and the sum of rows and columns
   *                                  is less than 9.</li>
   *                                  <li>if there is no path between any two nodes of the dungeon
   *                                  with at least length 5.</li>
   *                                  <li>if percentage of treasures is less than 0 or more than
   *                                  100.</li>
   *                                  <li>if {@code rand} is {@code null}.</li></ul>
   */
  public DungeonImpl(int rows, int columns, int interconnectivity, boolean isWrapping,
                     double percentageOfTreasures, String playerName, RandomGenerator rand)
          throws IllegalArgumentException {

    if (rows + columns < 7 && isWrapping) {
      throw new IllegalArgumentException("Too small dungeon. Increase number of rows and/or "
              + "columns.");
    }
    if (rows + columns < 9 && !isWrapping) {
      throw new IllegalArgumentException("Too small dungeon. Increase number of rows and/or "
              + "columns.");
    }
    if (isWrapping) {
      if (interconnectivity > (2 * rows * columns - rows * columns + 1)) {
        throw new IllegalArgumentException("Invalid interconnectivity.");
      }
    } else {
      if (interconnectivity > ((2 * rows * columns - rows - columns) - rows * columns + 1)) {
        throw new IllegalArgumentException("Invalid interconnectivity.");
      }
    }
    if (percentageOfTreasures < 0 || percentageOfTreasures > 100) {
      throw new IllegalArgumentException("Percentage of caves with treasures cannot be negative.");
    }

    if (playerName == null || playerName.equals("")) {
      throw new IllegalArgumentException("Player name cannot be null or empty");
    }

    if (rand == null) {
      throw new IllegalArgumentException("Random generator cannot be null.");
    }

    this.rows = rows;
    this.columns = columns;
    this.interconnectivity = interconnectivity;
    this.isWrapping = isWrapping;
    this.rand = rand;
    this.dungeon = createDungeon();
    this.potentialPaths = createPotentialPaths();
    this.paths = createPaths();
    getValidMovesForCaves();
    addTreasureToCaves(percentageOfTreasures);
    Map.Entry<LocationUpdateState, LocationUpdateState> sourceAndDestination = setStartAndEndCave();
    this.start = sourceAndDestination.getKey();
    this.end = sourceAndDestination.getValue();
    this.player = new PlayerImpl(playerName, this.start);
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public Location getPlayerLocation() {
    return this.player.getLocation();
  }

  @Override
  public Location getStartingCave() {
    return this.start;
  }

  @Override
  public Location getDestinationCave() {
    return this.end;
  }

  @Override
  public void movePlayer(Direction direction)
          throws IllegalArgumentException {
    if (direction == null || !this.player.getLocation().getPossibleMoves().contains(direction)) {
      throw new IllegalArgumentException("Invalid direction!");
    }

    LocationUpdateState newLocation = null;

    int currentRow = 0;
    int currentColumn = 0;

    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        if (this.dungeon.get(i).get(j).getId() == this.player.getLocation().getId()) {
          currentRow = i;
          currentColumn = j;
        }
      }
    }

    int newRow = currentRow;
    int newCol = currentColumn;

    switch (direction) {
      case SOUTH:
        newRow = currentRow + 1;
        if (this.isWrapping) {
          if (newRow == this.rows) {
            newRow = 0;
          }
        }
        break;

      case NORTH:
        newRow = currentRow - 1;
        if (this.isWrapping) {
          if (newRow == -1) {
            newRow = this.rows - 1;
          }
        }
        break;

      case WEST:
        newCol = currentColumn - 1;
        if (this.isWrapping) {
          if (newCol == -1) {
            newCol = this.columns - 1;
          }
        }
        break;

      case EAST:
        newCol = currentColumn + 1;
        if (this.isWrapping) {
          if (newCol == this.columns) {
            newCol = 0;
          }
        }
        break;

      default: //No action required.
    }
    newLocation = this.dungeon.get(newRow).get(newCol);
    this.player.move(newLocation);
  }

  @Override
  public void collectAllTreasures() {
    this.player.collectTreasures(List.of(Treasure.values()));
  }

  @Override
  public void collectTreasure(List<Treasure> treasures) throws IllegalArgumentException {
    if (treasures == null) {
      throw new IllegalArgumentException("Treasures cannot be null.");
    }
    this.player.collectTreasures(new ArrayList<>(treasures));
  }

  @Override
  public boolean isDestinationReached() {
    return this.player.getLocation().getId() == this.end.getId();
  }

  @Override
  public String toString() {
    StringBuilder dungeonBuilder = new StringBuilder();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.NORTH)) {
          dungeonBuilder.append("     |     ");
        } else {
          dungeonBuilder.append("           ");
        }
      }
      dungeonBuilder.append("\n");
      for (int j = 0; j < columns; j++) {

        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.WEST)) {
          dungeonBuilder.append("--- ");
        } else {
          dungeonBuilder.append("    ");
        }
        Location location = this.dungeon.get(i).get(j);
        String locationString;
        if (this.player != null && location.getId() == this.player.getLocation().getId()) {
          locationString = "P";
        } else if (location.getId() == this.getStartingCave().getId()) {
          locationString = "S";
        } else if (location.getId() == this.getDestinationCave().getId()) {
          locationString = "D";
        } else {
          locationString = location.toString();
        }
        dungeonBuilder.append(String.format("[%s]", locationString));
        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.EAST)) {
          dungeonBuilder.append(" ---");
        } else {
          dungeonBuilder.append("    ");
        }
      }
      dungeonBuilder.append("\n");
      for (int j = 0; j < columns; j++) {
        if (this.dungeon.get(i).get(j).getPossibleMoves().contains(Direction.SOUTH)) {
          dungeonBuilder.append("     |     ");
        } else {
          dungeonBuilder.append("           ");
        }
      }
      dungeonBuilder.append("\n");
    }

    return dungeonBuilder.toString();
  }

  private void addTreasureToCaves(double percentageOfTreasures) {
    List<LocationUpdateState> allLocations = getCavesOnly();
    int cavesWithTreasures = (int) (allLocations.size() * percentageOfTreasures / 100);
    final List<Treasure> allTreasures = List.of(Treasure.values());

    for (int i = 0; i < cavesWithTreasures; i++) {
      List<Treasure> treasuresToBeAdded = new ArrayList<>();
      int numberOfTreasures = rand.getRandom(4, 1);

      for (int j = 0; j < numberOfTreasures; j++) {
        treasuresToBeAdded.add(allTreasures.get(rand.getRandom(Treasure.values().length,
                0)));
      }
      int index = rand.getRandom(allLocations.size(), 0);
      allLocations.get(index).addTreasures(treasuresToBeAdded);
      allLocations.remove(index);
    }
  }

  private List<List<LocationUpdateState>> createDungeon() {
    List<List<LocationUpdateState>> dungeon = new ArrayList<>();
    for (int i = 0; i < this.rows; i++) {
      List<LocationUpdateState> dungeonCol = new ArrayList<>();
      for (int j = 0; j < this.columns; j++) {
        dungeonCol.add(new Cave((this.columns * i) + j, i, j));
      }
      dungeon.add(dungeonCol);
    }
    return dungeon;
  }

  private List<Edge> createPotentialPaths() {
    List<Edge> possibleEdges = new ArrayList<>();
    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < columns - 1; j++) {
        possibleEdges.add(new Edge(this.dungeon.get(i).get(j).getId(),
                this.dungeon.get(i).get(j + 1).getId()));
        possibleEdges.add(new Edge(this.dungeon.get(i).get(j).getId(),
                this.dungeon.get(i + 1).get(j).getId()));
      }
    }

    //border edges
    for (int j = 0; j < columns - 1; j++) {
      possibleEdges.add(new Edge(this.dungeon.get(rows - 1).get(j).getId(),
              this.dungeon.get(rows - 1).get(j + 1).getId()));
    }
    for (int i = 0; i < rows - 1; i++) {
      possibleEdges.add(new Edge(this.dungeon.get(i).get(columns - 1).getId(),
              this.dungeon.get(i + 1).get(columns - 1).getId()));
    }

    //edges for wrapping dungeon
    if (this.isWrapping) {
      for (int i = 0; i < rows; i++) {
        possibleEdges.add(new Edge(this.dungeon.get(i).get(0).getId(), this.dungeon.get(i)
                .get(columns - 1).getId()));
      }
      for (int j = 0; j < columns; j++) {
        possibleEdges.add(new Edge(this.dungeon.get(0).get(j).getId(), this.dungeon.get(rows - 1)
                .get(j).getId()));
      }
    }
    return possibleEdges;
  }

  private List<Edge> createPaths() {
    List<Edge> potentialPaths = new ArrayList<>(this.potentialPaths);
    List<Edge> paths = new ArrayList<>();
    List<Edge> leftOverPaths = new ArrayList<>();
    int nodes = this.rows * this.columns;
    Subset[] subsets = new Subset[nodes];
    for (int i = 0; i < nodes; i++) {
      subsets[i] = new Subset(i, 0);
    }
    int currentEdge = 0;

    while (currentEdge < nodes - 1 && potentialPaths.size() > 1) {
      int randomIndex = this.rand.getRandom(potentialPaths.size(), 0);
      Edge nextEdge = potentialPaths.get(randomIndex);

      int x = find(subsets, nextEdge.getFirstLocation());
      int y = find(subsets, nextEdge.getSecondLocation());

      if (x != y) {
        paths.add(nextEdge);
        union(subsets, x, y);
        currentEdge++;
      } else {
        leftOverPaths.add(nextEdge);
      }
      potentialPaths.remove(randomIndex);
    }

    for (int i = 0; i < this.interconnectivity; i++) {
      if (leftOverPaths.size() == 0) {
        int randomIndex = this.rand.getRandom(potentialPaths.size(), 0);
        paths.add(potentialPaths.get(randomIndex));
        potentialPaths.remove(randomIndex);
      } else {
        int randomIndex = this.rand.getRandom(leftOverPaths.size(), 0);
        paths.add(leftOverPaths.get(randomIndex));
        leftOverPaths.remove(randomIndex);
      }
      currentEdge++;
    }
    return paths;
  }

  private int find(Subset[] subsets, int i) {
    if (subsets[i].parent != i) {
      subsets[i].parent = find(subsets, subsets[i].parent);
    }
    return subsets[i].parent;
  }

  private void union(Subset[] subsets, int x, int y) {
    int rootOfX = find(subsets, x);
    int rootOfY = find(subsets, y);

    if (subsets[rootOfX].rank < subsets[rootOfY].rank) {
      subsets[rootOfX].parent = rootOfY;
    } else if (subsets[rootOfX].rank > subsets[rootOfY].rank) {
      subsets[rootOfY].parent = rootOfX;
    } else {
      subsets[rootOfY].parent = rootOfX;
      subsets[rootOfX].rank++;
    }
  }

  private void getValidMovesForCaves() {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        List<Direction> validDirections = new ArrayList<>();
        LocationUpdateState cave = this.dungeon.get(i).get(j);

        //down
        if (i < this.rows - 1) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i + 1).get(j).getId()))) {
            validDirections.add(Direction.SOUTH);
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(0).get(j).getId()))) {
            validDirections.add(Direction.SOUTH);
          }
        }
        //up
        if (i > 0) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i - 1).get(j).getId()))) {
            validDirections.add(Direction.NORTH);
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(this.rows - 1).get(j)
                  .getId()))) {
            validDirections.add(Direction.NORTH);
          }
        }
        //right
        if (j < this.columns - 1) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(j + 1).getId()))) {
            validDirections.add(Direction.EAST);
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(0).getId()))) {
            validDirections.add(Direction.EAST);
          }
        }
        //left
        if (j > 0) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(j - 1).getId()))) {
            validDirections.add(Direction.WEST);
          }
        } else if (isWrapping) {
          if (this.paths.contains(new Edge(cave.getId(), this.dungeon.get(i).get(this.columns - 1)
                  .getId()))) {
            validDirections.add(Direction.WEST);
          }
        }
        cave.setValidMoves(validDirections);
      }
    }
  }

  private List<LocationUpdateState> getCavesOnly() {
    List<LocationUpdateState> allLocations = new ArrayList<>();
    for (List<LocationUpdateState> locations : this.dungeon) {
      allLocations.addAll(locations);
    }
    return allLocations.stream().filter(location -> !location.isTunnel())
            .collect(Collectors.toList());
  }

  private LocationUpdateState getRandomCave(List<LocationUpdateState> locations) {
    int index = this.rand.getRandom(locations.size(), 0);
    return locations.get(index);
  }

  private AbstractMap.SimpleImmutableEntry<LocationUpdateState, LocationUpdateState>
              setStartAndEndCave() throws IllegalArgumentException {
    List<LocationUpdateState> potentialSources = getCavesOnly();
    LocationUpdateState source;
    LocationUpdateState destination;

    do {
      source = getRandomCave(potentialSources);
      destination = setEndCave(source);
      potentialSources.remove(source);
    }
    while (destination == null && potentialSources.size() != 0);

    if (destination == null) {
      throw new IllegalArgumentException("Dungeon too small or interconnected! Cannot find any"
              + " path of at length 5 between two nodes.");
    }

    return new AbstractMap.SimpleImmutableEntry<>(source, destination);
  }

  private LocationUpdateState setEndCave(LocationUpdateState source) {
    LocationUpdateState destination;
    int distance;
    List<LocationUpdateState> allLocations = getCavesOnly();

    do {
      destination = getRandomCave(allLocations);
      distance = getMinimumDistance(source, destination);
      allLocations.remove(destination);
    }
    while (distance < 5 && allLocations.size() > 0);

    if (distance < 5) {
      return null;
    }

    return destination;
  }

  private int getMinimumDistance(LocationUpdateState start, LocationUpdateState end) {
    NodeWithDistanceFromSource source = new NodeWithDistanceFromSource(0, 0, 0);
    Queue<NodeWithDistanceFromSource> nodes = new LinkedList<>();
    boolean[][] visited = new boolean[this.dungeon.size()][this.dungeon.get(0).size()];

    sourceLoop:
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        if (this.dungeon.get(i).get(j).getId() == start.getId()) {
          source.x = i;
          source.y = j;
          break sourceLoop;
        }
      }
    }

    nodes.add(new NodeWithDistanceFromSource(source.x, source.y, 0));
    visited[source.x][source.y] = true;

    while (!nodes.isEmpty()) {
      NodeWithDistanceFromSource node = nodes.remove();
      int x;
      int y;

      // Destination reached
      if (this.dungeon.get(node.x).get(node.y).getId() == end.getId()) {
        return node.distance;
      }

      // up
      x = node.x - 1;
      y = node.y;
      if (this.isWrapping && node.x == 0) {
        x = this.rows - 1;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves()
              .contains(Direction.NORTH)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }

      // down
      x = node.x + 1;
      y = node.y;
      if (this.isWrapping && node.x == this.rows - 1) {
        x = 0;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves()
              .contains(Direction.SOUTH)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }

      // left
      x = node.x;
      y = node.y - 1;
      if (this.isWrapping && node.y == 0) {
        y = this.columns - 1;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves().contains(Direction.WEST)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }

      // right
      x = node.x;
      y = node.y + 1;
      if (this.isWrapping && node.y == this.columns - 1) {
        y = 0;
      }
      if (isValid(x, y, visited)
              && this.dungeon.get(node.x).get(node.y).getPossibleMoves().contains(Direction.EAST)) {
        nodes.add(new NodeWithDistanceFromSource(x, y,
                node.distance + 1));
        visited[x][y] = true;
      }
    }
    return -1;
  }

  private boolean isValid(int x, int y, boolean[][] visited) {
    return (x >= 0 && y >= 0 && x < this.dungeon.size() && y < this.dungeon.get(0).size()
            && !visited[x][y]);
  }

  /**
   * This represents a {@link Location} in the dungeon with co-ordinates of the location in the
   * dungeon grid and its distance from the source location.
   */
  private static class NodeWithDistanceFromSource {
    private int x;
    private int y;
    private final int distance;

    /**
     * Constructs a location node.
     *
     * @param x        the x co-ordinate of location in the dungeon grid.
     * @param y        the y co-ordinate of location in the dungeon grid.
     * @param distance the distance from the source location.
     */
    private NodeWithDistanceFromSource(int x, int y, int distance) {
      this.x = x;
      this.y = y;
      this.distance = distance;
    }
  }

  /**
   * This represents a subset of the dungeon grid with the root parent and rank. This class is used
   * for building the dungeon grid.
   */
  private static class Subset {
    private int parent;
    private int rank;

    private Subset(int parent, int rank) {
      this.parent = parent;
      this.rank = rank;
    }
  }

}
