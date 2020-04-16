package unideb.diploma.domain;

import javafx.scene.paint.Color;

public enum FieldColor {
	RED(Color.RED), BLUE(Color.BLUE), WHITE(Color.WHITE);
	
	private Color value;
	
	private FieldColor(Color value) {
		this.value = value;
	}
	
	public Color getValue() {
		return value;
	}
}
