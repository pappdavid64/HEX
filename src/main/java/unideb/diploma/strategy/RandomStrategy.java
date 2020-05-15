package unideb.diploma.strategy;

import java.util.Random;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.strategy.strength.StrategyStrength;

/**
 * Strategy which makes move by randomly generated.
 * */
public class RandomStrategy implements Strategy {
	
	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	@Override
	public Operator getNextMove(State state) {
		Random random = new Random();
		return Cache.getOperatorAt(random.nextInt(App.BOARD_SIZE), random.nextInt(App.BOARD_SIZE));
	}

	/**
	 * Gets the goodness of the strategy by the state.
	 * @param state The state of the game.
	 * @return The strength of the strategy by the state.
	 * */
	@Override
	public StrategyStrength getGoodnessByState(State state) {
		return StrategyStrength.veryWeak(1);
	}
	
}
