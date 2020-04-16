package unideb.diploma.domain;

import unideb.diploma.App;
import unideb.diploma.observer.Observable;

public class Field extends Observable{
	private final Position position;
	private FieldColor color;
	private final int goodness;
	
	public Field(Position position, FieldColor color) {
		super();
		this.position = position;
		this.color = color;
		goodness = Math.abs(Math.abs(position.getX() - ((App.BOARD_SIZE - 1)/2)) + Math.abs(position.getY() - ((App.BOARD_SIZE - 1)/2)) - App.BOARD_SIZE);
	}

	public FieldColor getColor() {
		return color;
	}

	public void setColor(FieldColor color) {
		this.color = color;
		notifyObservers();
	}

	public Position getPosition() {
		return position;
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return position.getY();
	}

	public int getGoodness() {
		return goodness;
	}
	
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
