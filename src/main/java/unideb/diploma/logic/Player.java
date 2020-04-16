package unideb.diploma.logic;

import unideb.diploma.cache.Direction;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;

public interface Player {
	
	FieldColor getColor();
	
	Operator getNextMove(State state);

	String getName();
	
	Direction[] getDirections();
	
	boolean isEquals(Player other);
}
