package unideb.diploma.strategy;

import java.util.Random;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.strategy.strength.StrategyStrength;

public class RandomStrategy implements Strategy {

	private boolean active = true;
	
	@Override
	public Operator getNextMove(State state) {
		Random random = new Random();
		return Cache.getOperatorAt(random.nextInt(App.BOARD_SIZE), random.nextInt(App.BOARD_SIZE));
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		return StrategyStrength.veryWeak(1);
	}
	
	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void deActivate() {
		active = false;
	}

	@Override
	public void activate() {
		active = true;		
	}

	@Override
	public void reCalculate(State state) {
		// TODO Auto-generated method stub
		
	}

}
