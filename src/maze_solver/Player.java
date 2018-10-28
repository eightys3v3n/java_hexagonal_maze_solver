package maze_solver;

import java.util.ArrayList;
import maze_solver.Maze.HexType;


/**
 * @author Terrence Plunkett
 */
public class Player {
    Coordinate		  position;
    ArrayList<Coordinate> deadPositions;
    Maze		  maze;
    ArrayList<Coordinate> solution;
    boolean		  won = false;

    public Player(final Maze maze, final Coordinate startPos) {
	position = startPos;
	deadPositions = new ArrayList<>();
	solution = new ArrayList<>();
	this.maze = maze;
    }

    public boolean attemptMove() throws Exception {
	ArrayList<Coordinate> openNeighbours = new ArrayList<>();

	if (hasWon())
	    return false;

	openNeighbours = findOpenNeighbours(position);

	if (moveToCheese(openNeighbours)) {} else if (openNeighbours.size() == 0) {
	    backtrack();
	} else {
	    addSolutionCoord(position);
	    final boolean r = moveTo(openNeighbours.get(0));
	    assert !r : "Attempted to move to a non-open space on the maze?"
	            + openNeighbours.get(0);
	}
	return false;
    }

    public boolean moveToCheese(final ArrayList<Coordinate> openNeighbours) {
	for (final Coordinate c : openNeighbours) {
	    if (maze.get(c) == HexType.CHEESE) {
		moveTo(c);
		won = true;
		// addSolutionCoord(c);
		return true;
	    }
	}
	return false;
    }

    /**
     * Actually moves the player to the given coordinate if it is a possible move.
     *
     * @param newPosition
     * @return true if attempted move to wall space, false otherwise.
     */
    public boolean moveTo(final Coordinate newPosition) {
	if (maze.get(newPosition) == HexType.WALL) return true;
	else if (maze.get(newPosition) == HexType.DEAD_END) {
	    System.out.println("Moving to a dead position. This shouldn't happen.");
	    return true;
	}

	// if (maze.get(newPosition) == HexType.CHEESE) {
	// won = true;
	// }

	// maze.set(position, HexType.OPEN);
	position = newPosition;
	maze.set(newPosition, HexType.MOUSE);

	return false;
    }

    public void addSolutionCoord(final Coordinate c) {
	solution.add(0, c);
	maze.set(c, HexType.CURRENT_PATH);
    }

    public void addDeadCoord(final Coordinate c) {
	deadPositions.add(0, c);
	maze.set(c, HexType.DEAD_END);
    }

    public void backtrack() throws Exception {
	Coordinate newPosition;

	assert solution.size() != 0 : "Trying to backtrack without going anywhere first?";
	addDeadCoord(position);
	newPosition = solution.get(0);
	solution.remove(0);

	if (moveTo(newPosition)) throw new Exception("Tried to move into an invalid space while backtracking");
    }

    /**
     * @param position
     * @return
     * @throws Exception
     */
    public ArrayList<Coordinate> findOpenNeighbours(final Coordinate position) throws InvalidPositionException {
	final ArrayList<Coordinate> openNeighbours = maze.getNeighbours(position);

	for (int i = 0; i < openNeighbours.size(); i++) {
	    final Coordinate n = openNeighbours.get(i);
	    if (n != null) {

		if (maze.get(n) == HexType.WALL) {
		    openNeighbours.set(i, null);
		    continue;
		}
		if (solution.size() > 0) {
		    for (final Coordinate s : solution) {
			if (s.equals(n)) {
			    openNeighbours.set(i, null);
			}
		    }
		}
		if (deadPositions.size() > 0) {
		    for (final Coordinate c : deadPositions) {
			if (n.equals(c)) {
			    openNeighbours.set(i, null);
			}
		    }
		}
	    }
	}

	// Remove null neighbours
	for (int i = 0; i < openNeighbours.size(); i++) {
	    if (openNeighbours.get(i) == null) {
		openNeighbours.remove(i);
		i--;
	    }
	}

	if (openNeighbours.size() > 0) {
	    assert openNeighbours.get(0) != null : "All null elements should be removed before this.";
	}

	return openNeighbours;
    }

    public Coordinate getPosition() {
	return position;
    }

    public ArrayList<Coordinate> getDeadPositions() {
	return deadPositions;
    }

    public ArrayList<Coordinate> getSolution() {
	return solution;
    }

    public boolean hasWon() {
	return won;
    }
}
