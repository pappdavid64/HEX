package unideb.diploma.strategy.connection;

import unideb.diploma.domain.Position;

public class VirtualField {
	private Position position;
	
	public VirtualField(int x, int y) {
		position = new Position(x,y);
	}
	
	public Position getPosition() {
		return position;
	}
}
