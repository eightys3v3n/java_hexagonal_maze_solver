package maze_solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import maze_solver.Maze.HexType;


/**
 * @author Terrence Plunkett
 */
class PlayerTest {
    static Maze	  maze;
    static Player player;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
	maze = new Maze(0, 0);
	maze.loadFromFile("test_maze.txt");
	player = new Player(maze, new Coordinate(2, 1));
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterAll
    static void tearDownAfterClass() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {}

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception {}

    @Test
    void testOpenNeighbours() throws Exception {
	ArrayList<Coordinate> openNeighbours;
	String neighboursString, errStr;

	openNeighbours = player.findOpenNeighbours(new Coordinate(0, 0));
	neighboursString = arrToString(openNeighbours);
	errStr = String.format("Should be no open neighbours for position (0, 0), not %n%s%n", neighboursString);
	assertEquals(errStr, 0, openNeighbours.size());

	openNeighbours = player.findOpenNeighbours(new Coordinate(0, 3));
	neighboursString = arrToString(openNeighbours);
	errStr = String.format("Should be one open neighbour at (1, 3) for position (0, 3). Not %n%s%n",
	        neighboursString);
	assertEquals(errStr, 1, openNeighbours.size());
	assertTrue(errStr, openNeighbours.get(0).equals(new Coordinate(1, 3)));

	openNeighbours = player.findOpenNeighbours(new Coordinate(1, 3));
	neighboursString = arrToString(openNeighbours);
	errStr = String.format("Should be two open neighbours at (2, 2) and (2, 4) for position (1, 3), not %n%s%n",
	        neighboursString);
	assertEquals(errStr, 2, openNeighbours.size());
	assertTrue(errStr, openNeighbours.get(0).equals(new Coordinate(2, 2)));
	assertTrue(errStr, openNeighbours.get(1).equals(new Coordinate(2, 4)));
    }

    @Test
    void testMoveTo() {
	Coordinate newPosition = new Coordinate(1, 1);

	assertTrue(player.moveTo(newPosition));
	assertFalse(player.getPosition().equals(newPosition));

	newPosition = new Coordinate(2, 2);
	assertFalse(player.moveTo(newPosition));
	assertTrue(player.getPosition().equals(newPosition));
	assertEquals(HexType.MOUSE, maze.get(2, 2));
	assertEquals(HexType.OPEN, maze.get(2, 1));

	newPosition = new Coordinate(1, 3);
	assertFalse(player.moveTo(newPosition));
	assertTrue(player.getPosition().equals(newPosition));
	assertEquals(HexType.MOUSE, maze.get(1, 3));
	assertEquals(HexType.OPEN, maze.get(2, 2));
    }

    @Test
    void testBacktrack() throws Exception {
	player.solution.add(new Coordinate(2, 2));
	player.solution.add(new Coordinate(2, 1));

	player.backtrack();
	assertTrue(player.getPosition().equals(new Coordinate(2, 2)));
	assertTrue(player.getDeadPositions().get(0).equals(new Coordinate(1, 3)));
    }

    public String arrToString(final ArrayList<Coordinate> arr) {
	String ret = "[";
	for (final Object o : arr) {
	    if (o != null) {
		ret += o.toString() + ", ";
	    }
	}
	if (ret.endsWith(", ")) {
	    ret = ret.substring(0, ret.length() - 2);
	}
	ret += "]";
	return ret;
    }

}
