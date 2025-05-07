// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.map;

import java.util.Objects;

/**
 * Represents a position on the game map using row and column coordinates.
 * Used to determine entity placement, movement, and spatial relationships.
 */
public class Position {
    private int row, col;

    /**
     * Constructs a new Position with the specified row and column.
     *
     * @param row the row coordinate (Y-axis)
     * @param col the column coordinate (X-axis)
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Checks if another object is equal to this Position.
     * Two positions are considered equal if they have the same row and column values.
     *
     * @param obj the object to compare with this position
     * @return true if both positions have the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Returns the hash code for this position.
     * Required for correct behavior in hash-based collections.
     *
     * @return the hash code based on row and column
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Returns a string representation of the Position in the format "(row, col)".
     *
     * @return the string representation of this position
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    /**
     * Returns the row (Y-axis) of the position.
     *
     * @return the row value
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column (X-axis) of the position.
     *
     * @return the column value
     */
    public int getCol() {
        return col;
    }

    /**
     * Calculates the Manhattan distance between this position and another position.
     * Manhattan distance is the sum of the absolute differences in row and column.
     *
     * @param other the other position to compare to
     * @return the Manhattan distance between the two positions
     */
    public int distanceTo(Position other) {
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }
}
