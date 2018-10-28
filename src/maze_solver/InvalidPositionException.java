package maze_solver;

/**
 * @author Terrence Plunkett
 */
public class InvalidPositionException extends Exception {
    private final Coordinate invalidPosition;
    private final Coordinate gridSize;

    public InvalidPositionException(final Coordinate position, final Coordinate gridSize) {
	invalidPosition = position;
	this.gridSize = gridSize;
    }

    @Override
    public String toString() {
	return String.format("Attempted to use position %s which is outside of maze grid  of size %s%n.",
	        invalidPosition.toString(),
	        gridSize.toString());
    }
}
