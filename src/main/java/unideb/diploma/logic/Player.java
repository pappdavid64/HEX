package unideb.diploma.logic;

import unideb.diploma.cache.Direction;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.strategy.Strategy;

public interface Player {
	
	FieldColor getColor();
	
	FieldColor getOpponentColor();
	
	Operator getNextMove(State state);

	String getName();
	
	Direction[] getDirections();
	
	boolean isEquals(Player other);
	
	Player getOpponent();
	
	void setOpponent(Player opponent);
	
	void setStrategies(Strategy[] strategies);
}
