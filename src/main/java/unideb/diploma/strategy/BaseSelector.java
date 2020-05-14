package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.cache.Direction;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.observer.Observer;

public class BaseSelector {
	private Field base;
	private Player player;
	private Observer observer;
	private FieldValueStrategy fieldValueStrategy;

	public BaseSelector(Player player, Observer observer) {
		this.player = player;
		this.observer = observer;
		this.fieldValueStrategy = new FieldValueStrategy(player);
	}

	public BaseSelector selectBaseByFieldValue(State state) {

		base = fieldValueStrategy.getBestValueOfFields(state);
		addObserverToFields(getReachableFieldsFromField(base, new ArrayList<>()));

		return this;
	}

	public BaseSelector selectBaseByBridge(State state) {
		Map<Field, Integer> numberOfReachableFields = new HashMap<>();
		for (Field field : state.getTable().getFields()) {
			if (field.getColor() == player.getColor()) {
				if (canReachTheEnd(state, field)) {
					numberOfReachableFields.put(field, getReachableFieldsFromField(field, new ArrayList<>()).size());
				}
			}
		}
		int max = 0;
		for (Field field : numberOfReachableFields.keySet()) {
			if (max < numberOfReachableFields.get(field)) {
				base = field;
				max = numberOfReachableFields.get(field);
			}
		}
		return this;
	}

	public BaseSelector selectBaseFromWhiteFields(State state) {
		Map<Field, Integer> goodnessOfField = new HashMap<>();
		List<Field> whiteFields = new ArrayList<>();
		for (Field field : Cache.withColor(state.getTable().getFields(), FieldColor.WHITE)) {
			if (canReachTheEnd(state, field)) {
				goodnessOfField.put(field, field.getGoodness());
				whiteFields.add(field);
			}
		}

		int max = 0;
		for (Field field : goodnessOfField.keySet()) {
			if (max < goodnessOfField.get(field)) {
				max = goodnessOfField.get(field);
				base = field;
			}
		}
		addObserverToFields(getReachableFieldsFromField(base, new ArrayList<>()));
		return this;
	}

	public BaseSelector selectBaseByRandom(State state) {
		List<Operator> operators = Cache.getUseableOperators();
		Operator random = operators.get(new Random().nextInt(operators.size()));
		base = state.getFieldAt(random.getPosition());
		return this;
	}

	public boolean canReachTheEndFromBase(State state) {
		if (base == null) {
			return false;
		}
		return canReachTheEnd(state, base);
	}

	public Field getBase() {
		return base;
	}

	private boolean canReachTheEnd(State state, Field field) {
		for (Direction direction : player.getDirections()) {
			boolean canReachTheEnd = canReachTheEndInDirection(state, field, direction, new ArrayList<>());
			if (!canReachTheEnd) {
				return false;
			}
		}

		return true;
	}

	private boolean canReachTheEndInDirection(State state, Field actual, Direction direction, List<Field> alreadyWas) {
		for (Field field : Cache.getNeighboursByDirection(direction, actual)) {
			if (!alreadyWas.contains(field)) {
				alreadyWas.add(field);
				if (field.getX() == 0 || field.getX() == App.BOARD_SIZE - 1) {
					return true;
				}
				if (field.getY() == 0 || field.getY() == App.BOARD_SIZE - 1) {
					return true;
				}
				FieldColor color = field.getColor();
				if (color == FieldColor.WHITE || color == player.getColor()) {
					return canReachTheEndInDirection(state, field, direction, alreadyWas);
				}
			}
		}
		return false;
	}

	private List<Field> getReachableFieldsFromField(Field field, List<Field> reachableFields) {
		for (Direction direction : player.getDirections()) {
			for (Field actual : Cache.getNeighboursByDirection(direction, field)) {
				if ((actual.getColor() == player.getColor() || actual.getColor() == FieldColor.WHITE)
						&& !reachableFields.contains(actual)) {
					reachableFields.add(actual);
					getReachableFieldsFromField(actual, reachableFields);
				}
			}
		}
		return reachableFields;
	}

	private void addObserverToFields(List<Field> fields) {
		for (Field field : fields) {
			field.addObserver(observer);
		}
	}

}
