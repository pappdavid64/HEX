package unideb.diploma.domain;

import javafx.scene.paint.Color;

/**
 * The possible colors of a field.
 * */
public enum FieldColor {
	RED(Color.RED), BLUE(Color.BLUE), WHITE(Color.WHITE);
	
	/**
	 * Value of the color.
	 * */
	private Color value;
	
	/**
	 * Constructor.
	 * @param value The value of the color.
	 * */
	private FieldColor(Color value) {
		this.value = value;
	}
	
	/**
	 * Gets the color.
	 * @return The color.
	 * */
	public Color getValue() {
		return value;
	}
}
