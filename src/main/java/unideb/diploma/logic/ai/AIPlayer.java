package unideb.diploma.logic.ai;

import unideb.diploma.domain.FieldColor;
import unideb.diploma.exception.StrategyCanNotChooseException;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;
import unideb.diploma.strategy.BridgeStrategy;
import unideb.diploma.strategy.Strategy;
import unideb.diploma.strategy.strength.StrategyStrength;

/**
 * AI player.
 * */
public class AIPlayer extends PlayerWithNameAndColor {

	/**
	 * Strategies of the AI.
	 * */
	private Strategy[] strategies;
	
	/**
	 * Strategy which the AI use at the moment.
	 * */
	private Strategy strategy;
	
	/**
	 * Constructor.
	 * @param name The name of the player.
	 * @param color The color of the player.
	 * */
	public AIPlayer(String name, FieldColor color) {
		super(name, color);
	}

	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
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

		return nextMove;
	}
	
	/**
	 * Choose the strategy by the state.
	 * @param state The state of the game.
	 * */
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
	
	/**
	 * Sets the strategies of the AI player.
	 * @param strategies The strategies.
	 * */
	@Override
	public void setStrategies(Strategy[] strategies) {
		this.strategies = strategies;
	}

	@Override
	public void init(){
		for(Strategy strategy : strategies){
			strategy.init();
		}
	}
}
