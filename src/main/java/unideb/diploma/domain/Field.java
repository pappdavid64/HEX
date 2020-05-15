package unideb.diploma.domain;

import unideb.diploma.App;
import unideb.diploma.observer.Observable;

/**
 * A field of the table.
 * It can be observed.
 * */
public class Field extends Observable{
	
	/**
	 * The position of the field. 
	 * */
	private final Position position;
	
	/**
	 * The color of the field.
	 * */
	private FieldColor color;
	
	/**
	 * The base goodness of the field.
	 * */
	private final int goodness;
	
	/**
	 * Constructor.
	 * Base goodness calculated.
	 * @param position The position of the field.
	 * @param color The color of the field.
	 * */
	public Field(Position position, FieldColor color) {
		super();
		this.position = position;
		this.color = color;
		goodness = Math.abs(Math.abs(position.getX() - ((App.BOARD_SIZE - 1)/2)) + Math.abs(position.getY() - ((App.BOARD_SIZE - 1)/2)) - App.BOARD_SIZE);
	}

	/**
	 * Gets the color.
	 * @return The color.
	 * */
	public FieldColor getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 * @param color The color.
	 * */
	public void setColor(FieldColor color) {
		this.color = color;
		notifyObservers();
	}

	/**
	 * Gets the position.
	 * @return The position.
	 * */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Gets the row of the position.
	 * @return The row of the position.
	 * */
	public int getX() {
		return position.getX();
	}
	
	/**
	 * Gets the column of the position.
	 * @return The column of the position.
	 * */
	public int getY() {
		return position.getY();
	}

	/**
	 * Gets the goodness of the field.
	 * @return The goodness of the field.
	 * */
	public int getGoodness() {
		return goodness;
	}
	
	/**
	 * Cloning the field.
	 * @return The copy of the field.
	 * */
	public Field clone() {
		return new Field(position.clone(), color);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Field)) {
			return false;
		}
		Field other = (Field) obj;
		if (!position.equals(other.position)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Field [position=" + position + ", color=" + color + "]";
	}
	
	
	
}
