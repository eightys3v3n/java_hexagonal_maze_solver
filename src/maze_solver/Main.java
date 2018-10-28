package maze_solver;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;


/**
 * @author Terrence Plunkett
 */
public class Main {

    /**
     * @param args
     * @throws InvalidPositionException
     */
    public static void main(final String[] args) throws InvalidPositionException {
	final Maze maze = new Maze(0, 0);
	final Player player = new Player(maze, new Coordinate(2, 1));

	try {
	    maze.loadFromFile("maze.txt");
	} catch (final FileNotFoundException e) {
	    System.out.println("Invalid file name \"maze.txt\"");
	    return;
	}

	System.out.println(maze);
	System.out.println();

	while (!player.hasWon()) {

	    try {
		player.attemptMove();
	    } catch (final Exception e) {
		System.err.println("Mouse tried to make an invalid move.");
		e.printStackTrace();
	    }
	    System.out.println(maze);
	    System.out.println();

	    try {
		TimeUnit.MILLISECONDS.sleep(100);
	    } catch (final InterruptedException e) {}
	}
	System.out.println("Player has won!");
	System.out.println(player.getSolution());
    }
}
