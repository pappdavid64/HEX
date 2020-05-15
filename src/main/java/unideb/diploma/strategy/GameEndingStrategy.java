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

/**
 * Strategy which watch for if the player can end the game.
 * */
public abstract class GameEndingStrategy implements Strategy {

	/**
	 * The selected field for the next move.
	 * */
	protected Field selected;
	
	/**
	 * The player whom want to find an end state.
	 * */
	protected Player player;
	
	/**
	 * The number of steps actually needed for an end state.
	 */
	protected int numberOfSteps;
	
	/**
	 * The maximum allowed steps number.
	 * */
	protected int depth;

	/**
	 * Constructor.
	 * @param player The player whom want to find an end state.
	 * @param depth The maximum allowed steps number.
	 * */
	protected GameEndingStrategy(Player player, int depth) {
		this.player = player;
		this.depth = depth;
	}

	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	@Override
	public Operator getNextMove(State state) {
		Operator nextMove = Cache.getOperatorAt(selected.getPosition());
		selected = null;
		return nextMove;
	}

	/**
	 * Gets the goodness of the strategy by the state.
	 * @param state The state of the game.
	 * @return The strength of the strategy by the state.
	 * */
	@Override
	public StrategyStrength getGoodnessByState(State state) {
		boolean couldEnd = (couldEndInXTurns(state, depth));
		return couldEnd ? StrategyStrength.veryStrong(App.BOARD_SIZE - numberOfSteps) : StrategyStrength.veryWeak(0);
	}

	/**
	 * Returns true if could end in x turns.
	 * @param state The state of the game.
	 * @param x The number of turns.
	 * @return true if could end in x turns.
	 * */
	protected boolean couldEndInXTurns(State state, int x) {
		for (int i = 0; i < x; i++) {
			if (couldEndInXTurns(state.clone(), Cache.getUseableOperators(), new ArrayList<>(), i + 1)) {
				numberOfSteps = i;
				return true;
			}
		}
		return false;
	}

	/**
	 * Return true if could end in x turns.
	 * @param state The state of the game.
	 * @param useableOperators Operators which can be used.
	 * @param usedOperators Operators which already used in this method.
	 * @param x The number of turns.
	 * @return true if could end in x turns.
	 * */
	protected boolean couldEndInXTurns(State state, List<Operator> useableOperators, List<Operator> usedOperators,
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

				boolean couldEnd = couldEndInXTurns(state, remainingOperators, usedOperatorsCopy, x - 1);
				if (couldEnd) {
					selectField(state, usedOperatorsCopy);
					return true;
				}
			}
			operator.use(state.getTable(), FieldColor.WHITE);
		}
		return false;
	}
	
	/**
	 * Selects a field from the list of used operators.
	 * @param state The state of the game.
	 * @param usedOperators The operators used for the ending state.
	 * */
	protected abstract void selectField(State state, List<Operator> usedOperators);
}
