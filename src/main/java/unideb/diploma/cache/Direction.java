package unideb.diploma.cache;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.Position;


public enum Direction {
	EAST(0, 1), SOUTH(1,0), WEST(0, -1), NORTH(-1, 0), NORTH_EAST(-1, 1), SOUTH_WEST(1, -1);

	
	private Position position;
	
	
	private Direction(int x, int y) {
		this.position = new Position(x, y);
	}
	
	public Position getPosition() {
		return position;
	}

	
	public static Direction getDirectionBetween(Field a, Field b) {
		Direction direction = null;
		Position position = new Position(a.getX() - b.getX(), a.getY() - b.getY());

		for(Direction actual : Direction.values()) {
			if(actual.position.equals(position)) {
				direction = actual;
			}
		}
		
		return direction;
	}
	
}
