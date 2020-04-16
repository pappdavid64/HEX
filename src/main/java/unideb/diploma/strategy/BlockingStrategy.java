package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

public class BlockingStrategy implements Strategy {

	private boolean active = true;
	private Player player;
	private Field selected;
	private int depth;
	
	public BlockingStrategy(Player player, int depth) {
		this.player = player;
		this.depth = depth;
	}
	
	@Override
	public Operator getNextMove(State state) {
		return Cache.getOperatorAt(selected.getPosition());
	}

	@Override
	public int getGoodnessByState(State state) {
		boolean couldEnd = (opponentCouldEndInXTurns(state, depth));
		return  couldEnd ? Integer.MAX_VALUE : Integer.MIN_VALUE;
	}

	private boolean opponentCouldEndInXTurns(State state, int x) {
		for(int i = 0; i < x; i++) {
			if(opponentCouldEndInXTurns(Cache.getUseableOperators(), state.clone(), i+1)) {
				return true;
			}
		}
		return false;
	}


	private boolean opponentCouldEndInXTurns(List<Operator> useableOperators, State state, int x) {
		FieldColor opponentColor = (player.getColor() == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE;
		if(x == 0) {
			return state.isEndState(opponentColor);
		}
		
		for(Operator operator : useableOperators) {
			operator.use(state.getTable(), opponentColor);
			List<Operator> remainingOperators = new ArrayList<>(useableOperators);
			remainingOperators.remove(operator);
			boolean couldEnd = opponentCouldEndInXTurns(remainingOperators, state, x-1);
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

}
