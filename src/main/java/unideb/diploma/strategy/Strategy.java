package unideb.diploma.strategy;

import unideb.diploma.game.Operator;
import unideb.diploma.game.State;

public interface Strategy {
	Operator getNextMove(State state);
	int getGoodnessByState(State state);
	boolean isActive();
	void deActivate();
	void activate();
}
