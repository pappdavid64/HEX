package unideb.diploma.game;

import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;

/**
 * Represents a move.
 * */
public class Operator {
	
	/**
	 * The position of the move.
	 * */
	private Position position;
	
	/**
	 * Constructor.
	 * @param position The position of the operator.
	 * */
	public Operator(Position position) {
		this.position = position;
	}

	/**
	 * Checks if usable on a table.
	 * @param table The table.
	 * @return true if usable on the table.
	 * */
	public boolean isUsableOn(Table table) {
		FieldColor color = table.getFieldAt(position).getColor();
		if(color == FieldColor.WHITE) {
			return true;
		}
		return false;
	}

	/**
	 * Use the operator on the table.
	 * @param table The table.
	 * @param color The color which will be set at the position..
	 * */
	public void use(Table table, FieldColor color) {
		table.setFieldColorAtPosition(position, color);
	}
	
	/**
	 * Gets the position.
	 * @return The position.
	 * */
	public Position getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "Operator [position=" + position + "]";
	}
	
	
}
