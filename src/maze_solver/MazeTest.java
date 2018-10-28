package maze_solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * @author Terrence Plunkett
 */
class MazeTest {
    static Maze maze;

    /**
     * @throws java.lang.Exception
     */
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
	maze = new Maze(0, 0);
	maze.loadFromFile("test_maze.txt");
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
    void testGetNeighbours() throws InvalidPositionException {
	ArrayList<Coordinate> neighbours;

	neighbours = maze.getNeighbours(new Coordinate(0, 0));
	assertEquals(6, neighbours.size());
	assertTrue(neighbours.get(0) == null);
	assertTrue(neighbours.get(1).toString(), neighbours.get(1).equals(new Coordinate(1, 0)));
	assertTrue(neighbours.get(2).toString(), neighbours.get(2).equals(new Coordinate(0, 1)));
	assertTrue(neighbours.get(3) == null);
	assertTrue(neighbours.get(4) == null);
	assertTrue(neighbours.get(5) == null);

	neighbours = maze.getNeighbours(new Coordinate(1, 1));
	assertEquals(6, neighbours.size());
	assertTrue(neighbours.get(0).toString(), neighbours.get(0).equals(new Coordinate(2, 0)));
	assertTrue(neighbours.get(1).toString(), neighbours.get(1).equals(new Coordinate(2, 1)));
	assertTrue(neighbours.get(2).toString(), neighbours.get(2).equals(new Coordinate(2, 2)));
	assertTrue(neighbours.get(3).toString(), neighbours.get(3).equals(new Coordinate(1, 2)));
	assertTrue(neighbours.get(4).toString(), neighbours.get(4).equals(new Coordinate(0, 1)));
	assertTrue(neighbours.get(5).toString(), neighbours.get(5).equals(new Coordinate(1, 0)));
    }

}
