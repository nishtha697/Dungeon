# Dungeon Model

## About/Overview

The world for our game consists of a dungeon, a network of tunnels and caves that are interconnected
so that player can explore the entire world by traveling from cave to cave through the tunnels that
connect them.

Consider the following example:
<pre>
     |                     |          |     
    [T] ------ [C] ------ [C] ------ [T]    
                |          |                
                |          |                
--- [P] ------ [C] ------ [T]        [T] ---
                |                     |     
                |                     |     
    [T] ------ [T]        [D] ------ [T]    
     |                                      
     |                                      
--- [C]        [S] ------ [C] ------ [C] ---
     |                     |          |
</pre>

This example dungeon is represented by a 4 x 4 two-dimensional grid. Each location in the grid
represents a location in the dungeon where a player can explore and can be connected to at most
four (4) other locations: one to the north, one to the east, one to the south, and one to the west.
Notice that in this dungeon some locations "wrap" to the one on the other side of the grid. For
example, moving to the north from row 0 (at the top) in the grid moves the player to the location in
the same column in row 5 (at the bottom). A location can further be classified as tunnel (which has
exactly 2 entrances) or a cave (which has 1, 3 or 4 entrances). In the dungeon above, we are
representing caves with the letter <b>C</b>, tunnels with the letter <b>T</b>, the source cave is
represented with letter <b>S</b>, the destination cave with letter <b>D</b> and the current location
of the player with the letter <b>P</b>.

In many games, these dungeons are generated at random following some set of constraints resulting in
a different network each time the game begins. Following are the constraints of the dungeon:

1. The dungeon should be able to be represented on a 2-D grid.
2. There should be a path from every cave in the dungeon to every other cave in the dungeon.
3. Each dungeon can be constructed with a degree of interconnectivity. We define an
   interconnectivity = 0 when there is exactly one path from every cave in the dungeon to every
   other cave in the dungeon. Increasing the degree of interconnectivity increases the number of
   paths between caves.
4. Not all dungeons "wrap" from one side to the other (as defined above).
5. One cave is randomly selected as the start and one cave is randomly selected to be the end. The
   path between the start and the end locations should be at least of length 5.

## List of features

1. The model creates and support a player moving through the world.
2. Both wrapping and non-wrapping dungeons can be created with different degrees of
   interconnectivity.
3. Provides support for three types of treasure: diamonds, rubies, and sapphires. Treasure are added
   to a specified percentage of caves defined by the user. For example, the client asked the model
   to add treasure to 20% of the caves and then the model will add a random treasure to at least 20%
   of the caves in the dungeon. A cave can have more than one treasure.
4. Provides a description of the player that includes a description of what treasure the player has
   collected.
5. Provides a description of the player's location that includes a description of treasure in the
   room and the possible moves (north, east, south, west)
   that the player can make from their current location.
6. Moves the player from their current location.
7. A player can pick up treasure that is located in their same location. A player can either collect
   all treasures at once for that location or can enter the particular treasures it wants to pick.

## How to Run

java -jar ./res/Dungeon.jar command-line arguements

## How to Use the Program

Run the jar file (Refer "How to Run") with the command line arguements.

Consider the following example:

<pre>
java -jar res/Dungeon.jar 5 4 2 y 57 Nishtha
</pre>

1. 5 is the number of rows of the dungeon.
2. 4 is the number of columns of the dungeon.
3. 2 is the interconnectivity of the dungeon.
4. y indicates that the dungeon is wrapping. Anything other than "y" or "Y" results in a non
   wrapping dungeon.
5. 57 is the percentage of caves to have treasures in the dungeon.
6. Nishtha is the player name.

## Description of Examples

##### Non_wrapping_run.txt

1. Prints an introduction.
2. Prints the user entered number of rows = 5, number of columns = 6, interconnectivity = 6, if the
   dungeon is wrapping or not = n, the percentage of caves to have treasures in the dungeon = 25 and
   player name = "Nishtha".
3. Prints the dungeon.
6. Player collects the treasures and the collected treasures are printed on the screen.
7. Queries for possible moves from the current location and one of these possible move is selected
   at random.
8. Player is moved to the selected random direction.
9. The direction player moved to is printed.
10. Player collects all treasures of this location.
11. Collected treasures till now are printed to the screen.
12. Repeats step 7 to 9 until the player reaches the destination location.
13. Prints that the destination is reached.

##### Wrapping_run.txt

1. Prints an introduction.
2. Prints the user entered number of rows = 4, number of columns = 4, interconnectivity = 4, if the
   dungeon is wrapping or not = y, the percentage of caves to have treasures in the dungeon = 25 and
   player name = "Nishtha".
3. Prints the dungeon.
6. Player collects the treasures and the collected treasures are printed on the screen.
7. Queries for possible moves from the current location and one of these possible move is selected
   at random.
8. Player is moved to the selected random direction.
9. The direction player moved to is printed.
10. Player collects all treasures of this location.
11. Collected treasures till now are printed to the screen.
12. Repeats step 7 to 9 until the player reaches the destination location.
13. Prints that the destination is reached.

##### reach_all_locations.txt

1. Prints an introduction.
2. Prints the user entered number of rows = 5, number of columns = 4, interconnectivity = 2, if the
   dungeon is wrapping or not = y the percentage of caves to have treasures in the dungeon = 45 and
   player name = "Nishtha".
3. Prints the dungeon.
4. Player collects the treasures and the collected treasures are printed on the screen.
5. Queries for possible moves from the current location and one of these possible move is selected
   at random.
6. Player is moved to the selected random direction.
7. The direction player moved to and the current location's co-ordinates in the dungeon grid are
   printed.
8. Player collects all treasures of this location.
9. Collected treasures till now are printed to the screen.
10. Repeats step 7 to 9 until the all the locations in the dungeon are visited at least once.
11. Prints that all locations are visited.
12. Prints the co-ordinates of all the visited locations.

## Design/Model Changes

1. In the modified design, I created a package private class Edge to represent the edge from one
   location to another in the dungeon grid.
2. Two new internal private static classes are defined in Dungeon namely, Subset (used for tracking
   different subsets of locations within the dungeon grid to create paths) and
   NodeWithDistanceFromSource (that helps in keeping track of location with their repective
   co-ordinates in the dungeon grid and distance from the start location. This class is used while
   making sure that the distance between start and destination node is at least 5).
3. Instead of Dungeon having the method to get the possible moves of the current location of the
   player I moved those methods to Location class. And the dungeon just delegates the query to the
   location class when queried about this.
4. Previously, I decided to not store if a dungeon is wrapping or not in the class instead just
   calculate all the potentials paths on the fly in the constructor. But later I realized I also
   need to create a variable for it within the class to access at various points during execution to
   check if a dungeon is wrapping or not as many functionalities depend on it like moving the player
   from the border locations in a wrapping dungeon.
5. I also added a setPossibleMoves() method to the LocationUpdateState class in order to add the
   possible moves to that particular location once the dungeon is completely created.
6. I changed the type of id of a location from UUID to int as it was easier for me to perform find
   and union operations of locations and there subsets during the generation of dungeon using
   Kruskal's algorithm.
7. In the original design, the move() method of PlayerUpdateState takes in a direction and returns
   the new location the player is moved to in the dungeon, but I modified the method to accept the
   new location and set it as the current location and not return anything as the dungeon grid is
   available in dungeon class, and therefore it's better to calculate the new location in the
   Dungeon class based on the given direction and then simply update the current location of the
   player.
8. In my original design, I had a separate method createPlayer() in my dungeon to create a player
   and then return the read only Player object but in my revised design, the constructor of the
   Dungeon takes in a player name as an argument and therefore a dungeon will always have a player.
   I also added a getPlayer() method in my Dungeon that returns the read only Player object.
9. In my revised design, the Location has a Coordinate object (new class) that represents the x and
   y coordinates of the location in the 2D dungeon grid. The Location interface also has the
   getCoordinate() method which returns the coordinates of the location.

## Assumptions

1. A dungeon that cannot have a minimum distance of 5 between any two nodes is not a valid dungeon.
2. User has to enter a direction every time in order for the player to move.
3. Once a treasure is collected from the location it is removed from the location, so in case a user
   comes back to that location the already collected treasures will not be available at the location
   anymore.

## Limitations

The project covers all the functionalities given in the project description. Although, there can be
a few limitations -

1. There are only three treasures available.
2. The user needs to check if the destination is reached or not in order to stop the game.
3. Based on the user inputs, there can be too small dungeons for which the dungeon will not be
   created and the user will get an exception. So the user needs to be aware about the constraints
   of creating the dungeon.

## Citations

1. Effective Java - https://learning.oreilly.com/library/view/effective-java/9780134686097/
2. Head First Design Patterns
    - https://learning.oreilly.com/library/view/head-first-design/9781492077992/
3. https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
4. https://www.geeksforgeeks.org/shortest-distance-two-cells-matrix-grid/









