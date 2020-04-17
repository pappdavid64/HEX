package unideb.diploma.logic.ai;

import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;
import unideb.diploma.strategy.Strategy;
import unideb.diploma.strategy.strength.StrategyStrength;

public class AIPlayer extends PlayerWithNameAndColor {

	private Strategy[] strategies;
	private Strategy strategy;
	
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
				System.out.println("Strategy deactiveted: " + strategy.getClass());
				strategy.deActivate();
			}
		}

		System.out.println(strategy.getClass());
		return nextMove;
	}
	
	private Strategy chooseStrategy(State state) {
		Strategy bestStrategy = null;
		StrategyStrength strongest = null;
		for(Strategy strat : strategies) {
			if(strat.isActive()) {
				StrategyStrength strength = strat.getGoodnessByState(state);
				if(strength.isStrongerThan(strongest)) {
					bestStrategy = strat;
					strongest = strength;
				}
			}
		}
		return bestStrategy;
	}
	
	public void setStrategies(Strategy[] strategies) {
		this.strategies = strategies;
	}
	
}
