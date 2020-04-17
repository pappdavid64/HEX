package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.strength.StrategyStrength;

public class WinningStrategy implements Strategy {

	private boolean active = true;
	private Player player;
	private Field selected;
	private int depth;
	private int numberOfTurns;
	
	public WinningStrategy(Player player, int depth) {
		this.player = player;
		this.depth = depth;
	}
	
	@Override
	public Operator getNextMove(State state) {
		return Cache.getOperatorAt(selected.getPosition());
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		boolean couldEnd = (couldEndInXTurns(state, depth));
		return  couldEnd ? StrategyStrength.veryStrong(App.BOARD_SIZE - numberOfTurns) : StrategyStrength.veryWeak(0);
	}

	private boolean couldEndInXTurns(State state, int x) {
		for(int i = 0; i < x; i++) {
			if(couldEndInXTurns(Cache.getUseableOperators(), state.clone(), i+1)) {
				numberOfTurns = i;
				return true;
			}

		}
		return false;
	}


	private boolean couldEndInXTurns(List<Operator> useableOperators, State state, int x) {
		if(x == 0) {
			return state.isEndState(player.getColor());
		}
		
		for(Operator operator : useableOperators) {
			operator.use(state.getTable(), player.getColor());
			List<Operator> remainingOperators = new ArrayList<>(useableOperators);
			remainingOperators.remove(operator);
			boolean couldEnd = couldEndInXTurns(remainingOperators, state, x-1);
			if(couldEnd) {
				selected = state.getFieldAt(operator.getPosition());
				return true;
			}
			operator.use(state.getTable(), FieldColor.WHITE);
		}
		return false;
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
