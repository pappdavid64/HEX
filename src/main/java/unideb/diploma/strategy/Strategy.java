package unideb.diploma.strategy;

import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.strategy.strength.StrategyStrength;

/**
 * A strategy can say its goodness for a state and can make a move.
 * */
public interface Strategy {
	
	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	Operator getNextMove(State state);
	
	/**
	 * Gets the goodness of the strategy by the state.
	 * @param state The state of the game.
	 * @return The strength of the strategy by the state.
	 * */
	StrategyStrength getGoodnessByState(State state);
}
