package maze_solver;

/**
 * @author Terrence Plunkett
 */
public class Coordinate {
    int	x;
    int	y;

    public Coordinate() {
	x = 0;
	y = 0;
    }

    public Coordinate(final int x, final int y) {
	this.x = x;
	this.y = y;
    }

    public Coordinate(final Coordinate c) {
	x = c.getX();
	y = c.getY();
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public void setX(final int x) {
	this.x = x;
    }

    public void setY(final int y) {
	this.y = y;
    }

    public boolean equals(final Coordinate c) {
	if (c.getX() != x) return false;
	else if (c.getY() != y) return false;
	return true;
    }

    @Override
    public String toString() {
	return String.format("(%d, %d)", x, y);
    }
}
