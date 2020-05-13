package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.strength.StrategyStrength;

public class PathBlockingStrategy implements Strategy {

	private Player player;
	private Field selected;
	private int blockedPathLength;
	
	
	public PathBlockingStrategy(Player player) {
		this.player = player;
	}
	
	@Override
	public Operator getNextMove(State state) {
		return Cache.getOperatorAt(selected.getPosition());
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		boolean canBlockEnemyPath = canBlockEnemyPath(Cache.getUseableOperators(), state.clone());
		return canBlockEnemyPath ? StrategyStrength.medium(blockedPathLength) : StrategyStrength.veryWeak(0);
	}

	private boolean canBlockEnemyPath(List<Operator> useableOperators, State state) {
		
		int longestPathLength = state.getLongestPathLength(player.getOpponentColor());
		List<Field> makesTheEnemyPathLonger = new ArrayList<>();
		for(Operator operator : useableOperators) {
			operator.use(state.getTable(), player.getOpponentColor());
			int actualLength = state.getLongestPathLength(operator.getPosition().getX(), operator.getPosition().getY(), player.getOpponentColor(), new ArrayList<>());
			if(actualLength > longestPathLength) {
				longestPathLength = actualLength;
				makesTheEnemyPathLonger.clear();
				makesTheEnemyPathLonger.add(state.getFieldAt(operator.getPosition()));
			} else if (actualLength == longestPathLength) {
				makesTheEnemyPathLonger.add(state.getFieldAt(operator.getPosition()));
			}
			operator.use(state.getTable(), FieldColor.WHITE);
		}
		if(makesTheEnemyPathLonger.size() ==  1) {
			selected = makesTheEnemyPathLonger.get(0);
			State copyState = state.clone();
			Cache.getOperatorAt(selected.getPosition()).use(copyState.getTable(), player.getOpponentColor());
			blockedPathLength = copyState.getLongestPathLength(selected.getX(), selected.getY(), player.getOpponentColor(), new ArrayList<>());
			return true;
		}
		return false;
	}

}
