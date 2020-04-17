package unideb.diploma.strategy;

import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.strategy.strength.StrategyStrength;

public interface Strategy {
	Operator getNextMove(State state);
	StrategyStrength getGoodnessByState(State state);
	boolean isActive();
	void deActivate();
	void activate();
	void reCalculate(State state);
}
