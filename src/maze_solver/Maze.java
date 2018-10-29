package maze_solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * @author Terrence Plunkett
 */
public class Maze {
    /**
     * A 1D ArrayList that contains HexType objects for every position in the maze.
     * Used as a 2D grid.
     */
    private ArrayList<HexType> map;
    /**
     * Height of the maze.
     */
    int			       height = 0;
    /**
     * Width of the maze.
     */
    int			       width  = 0;

    /**
     * Represents the type of hex that is in the map.
     */
    public static enum HexType {
	/**
	 * @WALL Represented by 'W'. An impassable space.
	 * @OPEN Represented by ' '. A space that can be moved through.
	 * @MOUSE Represented by 'M'. Current position of the mouse. Starts at the start
	 *        of the maze.
	 * @CHEESE Represented by 'C'. End position of maze.
	 * @CURRENT_PATH Represented by '*'. The resulting path travelled by the player
	 *               from start to finish.
	 * @INVALID_TYPE Represented by '!'. An invalid HexType.
	 */
	WALL('W'), @SuppressWarnings("javadoc")
	OPEN(' '), @SuppressWarnings("javadoc")
	MOUSE('M'), @SuppressWarnings("javadoc")
	DEAD_END('x'), @SuppressWarnings("javadoc")
	CHEESE('C'), @SuppressWarnings("javadoc")
	CURRENT_PATH('*'), @SuppressWarnings("javadoc")
	INVALID_TYPE('!');
	/**
	 * A character that will be printed when the object is printed.
	 */
	char display;

	@Override
	public String toString() {
	    return new String() + display;
	}

	/**
	 * Create a new HexType that is represented by display.
	 *
	 * @param display
	 *            A character that represents the HexType when printed.
	 */
	HexType(final char display) {
	    this.display = display;
	}
    }

    /**
     * Create a maze filled with HexType.OPEN of the input width and height.
     *
     * @param width
     *            Width of maze.
     * @param height
     *            Height of maze.
     */
    public Maze(final int width, final int height) {
	this.width = width;
	this.height = height;
	generateEmptyMap();
    }

    /**
     * Fills the map with HexType.OPEN.
     */
    public void generateEmptyMap() {
	map = new ArrayList<>(width * height);
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		map.add(HexType.OPEN);
	    }
	}
    }

    /**
     * Loads a hexagonal map from a text file. The file should adhere to the following example format but the maze can be any dimensions.
     * The characters are according to the HexType constants except for open hexagons should be 'O'. Ignores extra characters after maze, spaces and newlines.
     * @formatter:off
     * <Begin example file>
     * 6 6
     * M O W W O W
     *  W O W O O O
     * O O W O W W
     *  O W O O O W
     * O W O W W O
     *  O O W W W C
     * <End example file>
     * @formatter:on
     *
     * @param filePath Path of text file to load.
     * @return Returns true if failed to load file, false if successfully loaded file.
     * @throws FileNotFoundException If file doesn't exist.
     */
    public boolean loadFromFile(final String filePath) throws FileNotFoundException {
	final File file = new File(filePath);
	Scanner input = null;
	char c;
	HexType t;

	try {
	    input = new Scanner(file);
	    width = input.nextInt();
	    height = input.nextInt();

	    generateEmptyMap();

	    for (int i = 0; i < map.size(); i++) {
		c = input.next().toUpperCase().charAt(0);

		switch (c) {
		case 'W':
		    t = HexType.WALL;
		    break;
		case 'O':
		    t = HexType.OPEN;
		    break;
		case 'M':
		    t = HexType.MOUSE;
		    break;
		case 'X':
		    t = HexType.DEAD_END;
		    break;
		case 'C':
		    t = HexType.CHEESE;
		    break;
		default:
		    t = HexType.INVALID_TYPE;
		}
		map.set(i, t);
	    }
	} catch (final Exception e) {
	    if (input != null) {
		input.close();
	    }

	    if (e instanceof InputMismatchException || e instanceof NoSuchElementException) {
		System.err.println("Input file is invalid.");
		return true;
	    } else if (e instanceof FileNotFoundException) {
		System.err.println("Input file '" + filePath + "' could not be read.");
		return true;
	    } else throw e;
	}

	input.close();
	return false;

    }

    /**
     * Change the type of hexagon at a specific position on the maze.
     *
     * @param x
     *            X position in maze to set the type of.
     * @param y
     *            Y position in maze to set the type of.
     * @param type
     *            HexType enum for new type.
     */
    public void set(final int x, final int y, final HexType type) {
	map.set(y * width + x, type);
    }

    /**
     * Change the type of hexagon at a specific position on the maze.
     *
     * @param position
     *            Coordinate object that describes the position in maze to set the
     *            type of.
     * @param type
     *            HexType enum for new type.
     */
    public void set(final Coordinate position, final HexType type) {
	set(position.getX(), position.getY(), type);
    }

    /**
     * @param x
     *            X Position in the maze to be returned.
     * @param y
     *            Y position in the maze to be returned.
     * @return The HexType for the given position in the maze.
     */
    public HexType get(final int x, final int y) {
	return map.get(y * width + x);
    }

    /**
     * @param position
     *            Coordinate object that describes the position in the maze to be
     *            returned.
     * @return The HexType for the given position in the maze.
     */
    public HexType get(final Coordinate position) {
	return get(position.getX(), position.getY());
    }

    /**
     * Calculate the Coordinates of the adjacent neighbour hexagons for the
     * specified position. Doesn't include positions for hexagons that don't exist,
     * IE outside maze bounds. Invalid neighbours are null. Return ArrayList indexes
     * correlate to cardinal direction of neighbour as such where p is the input position:
     * @formatter:off
     *  5 0
     * 4 p 1
     *  3 2
     * @formatter:on
     *
     * @param position
     *            Coordinate object for the position we want the adjacent neighbours
     *            of.
     * @return ArrayList<Coordinate> of size 6 where invalid neighbours are null and
     *         indexes correlate to neighbours cardinal direction.
     * @throws InvalidPositionException
     *             If position argument describes a position outside the maze.
     */
    public ArrayList<Coordinate> getNeighbours(final Coordinate position) throws InvalidPositionException {
	final ArrayList<Coordinate> neighbours = new ArrayList<>(6);

	if (position.getX() < 0 || position.getX() >= width)
	    throw new InvalidPositionException(position, new Coordinate(getWidth(), getHeight()));
	if (position.getY() < 0 || position.getY() >= height)
	    throw new InvalidPositionException(position, new Coordinate(getWidth(), getHeight()));

	// Add all neighbour positions but don't set positions that change for even and
	// odd Y values
	neighbours.add(null);
	neighbours.add(new Coordinate(position.getX() + 1, position.getY() - 0));
	neighbours.add(null);
	neighbours.add(null);
	neighbours.add(new Coordinate(position.getX() - 1, position.getY() - 0));
	neighbours.add(null);

	// Set neighbours for even and odd Y values respectively.
	if (position.getY() % 2 == 0) {
	    neighbours.set(0, new Coordinate(position.getX() + 0, position.getY() - 1));
	    neighbours.set(2, new Coordinate(position.getX() + 0, position.getY() + 1));
	    neighbours.set(3, new Coordinate(position.getX() - 1, position.getY() + 1));
	    neighbours.set(5, new Coordinate(position.getX() - 1, position.getY() - 1));
	} else {
	    neighbours.set(0, new Coordinate(position.getX() + 1, position.getY() - 1));
	    neighbours.set(2, new Coordinate(position.getX() + 1, position.getY() + 1));
	    neighbours.set(3, new Coordinate(position.getX() + 0, position.getY() + 1));
	    neighbours.set(5, new Coordinate(position.getX() + 0, position.getY() - 1));
	}

	// Remove neighbours that are outside the maze bounds.
	if (position.getY() % 2 == 0) {
	    if (position.getX() == 0) {
		neighbours.set(3, null);
		neighbours.set(4, null);
		neighbours.set(5, null);
	    }
	    if (position.getX() == width - 1) {
		neighbours.set(1, null);
		neighbours.set(2, null);
	    }
	    if (position.getY() == 0) {
		neighbours.set(0, null);
		neighbours.set(5, null);
	    }
	    if (position.getY() == width - 1) {
		neighbours.set(2, null);
		neighbours.set(3, null);
	    }
	} else {
	    if (position.getX() == 0) {
		neighbours.set(4, null);
	    }
	    if (position.getX() == width - 1) {
		neighbours.set(0, null);
		neighbours.set(1, null);
		neighbours.set(2, null);
	    }
	    if (position.getY() == 0) {
		neighbours.set(0, null);
		neighbours.set(5, null);
	    }
	    if (position.getY() == width - 1) {
		neighbours.set(2, null);
		neighbours.set(3, null);
	    }
	}

	return neighbours;
    }

    /**
     * Returns a printable maze using HexType characters for every hexagon. Every
     * second row is offset by adding a space as the first character. Every hexagon
     * character is separated with a space. An example below:
     * @formatter:off
     * W * W W W W
     *  W * *   W W
     * W W W W   W
     *  W C       W
     * W W W W W W
     *  W W W W W W
     * @formatter:on
     */
    @Override
    public String toString() {
	String output = "";

	for (int y = 0; y < height; y++) {
	    if ((y + 1) % 2 == 0) {
		output += " "; // Add a space to offset every second line.
	    }

	    for (int x = 0; x < width; x++) {
		if (map.get(y * height + x) == HexType.MOUSE) {
		    output = output.substring(0, output.length() - 1);
		    output += "[" + map.get(y * height + x) + "]";
		} else {
		    output += map.get(y * height + x) + " ";
		}
	    }
	    output += "%n";
	}

	if (output.endsWith("%n")) {
	    output = output.substring(0, output.length() - 2);
	}
	return String.format(output);
    }

    /**
     * @return Width of maze.
     */
    public int getWidth() {
	return width;
    }

    /**
     * @return Height of maze.
     */
    public int getHeight() {
	return height;
    }

}
