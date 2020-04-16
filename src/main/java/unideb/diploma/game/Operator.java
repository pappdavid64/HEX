package unideb.diploma.game;

import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;

public class Operator {
	
	private Position position;
	
	public Operator(Position position) {
		this.position = position;
	}

	public boolean isUsableOn(Table table) {
		FieldColor color = table.getFieldAt(position).getColor();
		if(color == FieldColor.WHITE) {
			return true;
		}
		return false;
	}

	public void use(Table table, FieldColor color) {
		table.setFieldColorAtPosition(position, color);
	}
	
	public Position getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return "Operator [position=" + position + "]";
	}
	
	
}
