package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.strength.StrategyStrength;

public abstract class GameEndingStrategy implements Strategy {

	protected Field selected;
	protected Player player;
	protected int numberOfTurns;
	protected int depth;

	protected GameEndingStrategy(Player player, int depth) {
		this.player = player;
		this.depth = depth;
	}

	@Override
	public Operator getNextMove(State state) {
		Operator nextMove = Cache.getOperatorAt(selected.getPosition());
		selected = null;
		return nextMove;
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		boolean couldEnd = (couldEndInXTurns(state, depth));
		return couldEnd ? StrategyStrength.veryStrong(App.BOARD_SIZE - numberOfTurns) : StrategyStrength.veryWeak(0);
	}

	protected boolean couldEndInXTurns(State state, int x) {
		for (int i = 0; i < x; i++) {
			if (couldEndInXTurns(Cache.getUseableOperators(), new ArrayList<>(), state.clone(), i + 1)) {
				numberOfTurns = i;
				return true;
			}
		}
		return false;
	}

	protected boolean couldEndInXTurns(List<Operator> useableOperators, List<Operator> usedOperators, State state,
			int x) {
		if (x == 0) {
			return state.isEndState(player.getColor());
		}

		int pathLength = state.getLongestPathLength(player.getColor());

		for (Operator operator : useableOperators) {
			operator.use(state.getTable(), player.getColor());
			Position position = operator.getPosition();
			if (state.getLongestPathLength(position.getX(), position.getY(), player.getColor(),
					new ArrayList<>()) > pathLength) {
				List<Operator> remainingOperators = new ArrayList<>(useableOperators);
				remainingOperators.remove(operator);
				List<Operator> usedOperatorsCopy = new ArrayList<>(usedOperators);
				usedOperatorsCopy.add(operator);

				boolean couldEnd = couldEndInXTurns(remainingOperators, usedOperatorsCopy, state, x - 1);
				if (couldEnd) {
					selectField(usedOperatorsCopy, state);
					return true;
				}
			}
			operator.use(state.getTable(), FieldColor.WHITE);
		}
		return false;
	}

	protected abstract void selectField(List<Operator> usedOperators, State state);
}
