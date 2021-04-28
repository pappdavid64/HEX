package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;


/**
 * Strategy which tries to prevent the opponent from winning.
 * */
public class BlockingStrategy extends GameEndingStrategy {

	/**
	 * Constructor.
	 * @param player The player whom opponent want to prevent to find an end state.
	 * @param depth The maximum allowed steps number.
	 * */
	public BlockingStrategy(Player player, int depth) {
		super(player.getOpponent(), depth);
	}

	@Override
	public void init(){

	}
	/**
	 * Selects a field from the list of used operators.
	 * @param state The state of the game.
	 * @param usedOperators The operators used for the ending state.
	 * */
	@Override
	public void selectField(State state, List<Operator> usedOperators) {
		List<Field> fieldsWithMinimum = new ArrayList<>();
		VirtualConnection selectedConnection = Cache.getConnectionFromField(player.getOpponent(), selected);
		int max = selected == null ? Integer.MAX_VALUE
				: (selectedConnection == null) ? Integer.MAX_VALUE : selectedConnection.getConnectionsCount();
		for (Operator usedOperator : usedOperators) {
			Field actualField = state.getFieldAt(usedOperator.getPosition());
			VirtualConnection connection = Cache.getConnectionFromField(player.getOpponent(), actualField);
			int actual = (connection == null) ? 0 : connection.getConnectionsCount();
			if (actual < max) {
				fieldsWithMinimum.clear();
				max = actual;
				fieldsWithMinimum.add(actualField);
			} else if (actual == max) {
				fieldsWithMinimum.add(actualField);
			}
		}
		if (!fieldsWithMinimum.isEmpty()) {
			selected = fieldsWithMinimum.get(new Random().nextInt(fieldsWithMinimum.size()));
		}
	}

}
