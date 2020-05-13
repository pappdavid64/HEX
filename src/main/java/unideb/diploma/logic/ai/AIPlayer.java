package unideb.diploma.logic.ai;

import unideb.diploma.domain.FieldColor;
import unideb.diploma.exception.StrategyCanNotChooseException;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;
import unideb.diploma.strategy.BridgeStrategy;
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
			} catch(StrategyCanNotChooseException ex){
				ex.printStackTrace();
				((BridgeStrategy)strategy).reCalculateBase(state);
			}
		}

		System.out.println(strategy.getClass());
		return nextMove;
	}
	
	private Strategy chooseStrategy(State state) {
		Strategy bestStrategy = null;
		StrategyStrength strongest = null;
		for(Strategy strategy : strategies) {
			StrategyStrength strength = strategy.getGoodnessByState(state);
			if(strength.isStrongerThan(strongest)) {
				bestStrategy = strategy;
				strongest = strength;
			}
		}
		return bestStrategy;
	}
	
	@Override
	public void setStrategies(Strategy[] strategies) {
		this.strategies = strategies;
	}
	
}
