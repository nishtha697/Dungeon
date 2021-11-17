import java.util.List;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import location.Direction;
import player.Player;
import random.RandomFactory;
import random.RandomGenerator;

/**
 * The driver class that runs the {@link dungeon.Dungeon} depicting a user.
 */
public class Driver {

  /**
   * Main method.
   *
   * @param args the arguments.
   */
  public static void main(String[] args) {
    System.out.println("Welcome to the World of Dungeon");
    int rows;
    rows = Integer.parseInt(args[0]);
    System.out.println("Number of rows: " + rows);

    int columns;
    columns = Integer.parseInt(args[1]);
    System.out.println("Number of columns: " + columns);

    int interconnectivity;
    interconnectivity = Integer.parseInt(args[2]);
    System.out.println("Interconnectivity: " + interconnectivity);

    boolean isWrapping;
    isWrapping = args[3].equalsIgnoreCase("y");
    System.out.println("Is dungeon wrapping: " + isWrapping);

    double percentageOfTreasures;
    percentageOfTreasures = Integer.parseInt(args[4]);
    System.out.println("Percentage of caves to have treasures: " + percentageOfTreasures);

    String playerName = args[5];
    System.out.println("Player name: " + playerName);
    System.out.println();

    RandomFactory randomFactory = new RandomFactory();
    RandomGenerator rand = randomFactory.getRandomGenerator(true);
    Dungeon dungeon = new DungeonImpl(rows, columns, interconnectivity, isWrapping,
            percentageOfTreasures, playerName, rand);

    Player player = dungeon.getPlayer();
    dungeon.collectAllTreasures();
    System.out.println(dungeon);
    System.out.println("Collected treasures: " + player.getCollectedTreasures());

    do {
      System.out.println();
      List<Direction> possibleMoves = player.getLocation().getPossibleMoves();
      Direction move = possibleMoves.get(rand.getRandom(possibleMoves.size(), 0));
      dungeon.movePlayer(move);
      System.out.println(player.getName() + " moved to " + move);
      System.out.println("Current location co-ordinates in grid: "
              + player.getLocation().getCoordinates().getX() + ","
              + player.getLocation().getCoordinates().getY());
      System.out.println("Possible moves: " + player.getLocation().getPossibleMoves());
      dungeon.collectAllTreasures();
      System.out.println("Collected treasures: " + player.getCollectedTreasures());
      System.out.println(dungeon);
    }
    while (!dungeon.isDestinationReached());

    System.out.println("Destination reached!!");
  }
}
