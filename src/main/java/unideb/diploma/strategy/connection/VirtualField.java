package unideb.diploma.strategy.connection;

import unideb.diploma.domain.Position;

/**
 * Field with negative x or y.
 * Used for making virtual connections with the edges of the board.
 * */
public class VirtualField {
	/**
	 * Position of the virtual field.
	 * */
	private Position position;
	
	/**
	 * Constructor.
	 * @param x The row.
	 * @param y The column.
	 * */
	public VirtualField(int x, int y) {
		position = new Position(x,y);
	}
	
	/**
	 * Gets the position.
	 * @return The position.
	 * */
	public Position getPosition() {
		return position;
	}
}
