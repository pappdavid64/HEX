package unideb.diploma.logic.ai;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;
import unideb.diploma.strategy.Strategy;

public class AIPlayer extends PlayerWithNameAndColor {

	private Strategy[] strategies;
	private Strategy strategy;
	private Field base;
	
	public AIPlayer(String name, FieldColor color) {
		super(name, color);
	}

	@Override
	public Operator getNextMove(State state) {
		Operator nextMove = null;
		while(nextMove == null) {
			try {
				strategy = chooseStrategy(state);
				nextMove = strategy.getNextMove(state);
			} catch(NullPointerException ex){
//				System.out.println("Strategy deactivated: " + strategy.getClass());
				strategy.deActivate();
			}
		}
		if(base == null) {
			base = state.getFieldAt(nextMove.getPosition());
		}
		System.out.println(strategy.getClass());
		return nextMove;
	}
	
	public Field getBase() {
		return base;
	}
	
	private Strategy chooseStrategy(State state) {
		Strategy bestStrategy = null;
		int maxValue = Integer.MIN_VALUE;
		for(Strategy strat : strategies) {
			if(strat.isActive()) {
				int value = strat.getGoodnessByState(state);
				maxValue = (maxValue < value) ? value : maxValue;
				bestStrategy = (maxValue == value) ? strat : bestStrategy;
			}
		}
		return bestStrategy;
	}
	
	public void setStrategies(Strategy[] strategies) {
		this.strategies = strategies;
	}
	
}
