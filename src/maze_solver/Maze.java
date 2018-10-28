package maze_solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * @author Terrence
 */
public class Maze {
    private ArrayList<HexType> map;
    int			       height = 0;
    int			       width  = 0;

    public static enum HexType {
	WALL("W"), OPEN(" "), MOUSE("M"), DEAD_END("x"), CHEESE("C"), CURRENT_PATH("*"), INVALID_TYPE("!");
	String display = "";

	@Override
	public String toString() {
	    return display;
	}

	HexType(final String display) {
	    this.display = display;
	}
    }

    public Maze(final int width, final int height) {
	this.width = width;
	this.height = height;
	generateEmptyMap();
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    /**
     * Fills the map with space characters.
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
     * @param rawMap
     * @return
     * @throws FileNotFoundException
     */
    public boolean loadFromFile(final String filePath) throws FileNotFoundException {
	final Scanner file = new Scanner(new File(filePath));
	char c;

	width = file.nextInt();
	height = file.nextInt();
	generateEmptyMap();

	try {
	    for (int i = 0; i < map.size(); i++) {
		HexType t;
		c = file.next().toUpperCase().charAt(0);

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
	    if (e instanceof InputMismatchException || e instanceof NoSuchElementException)
	        return true;
	    else throw e;
	}

	return false;
    }

    public void set(final int x, final int y, final HexType type) {
	map.set(y * width + x, type);
    }

    public void set(final Coordinate position, final HexType type) {
	set(position.getX(), position.getY(), type);
    }

    public HexType get(final int x, final int y) {
	return map.get(y * width + x);
    }

    public HexType get(final Coordinate position) {
	return get(position.getX(), position.getY());
    }

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

	// Set neighbours for even and odd Y values
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

	// Remove neighbours that are off the map.
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
}
