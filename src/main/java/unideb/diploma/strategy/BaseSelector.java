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
import unideb.diploma.util.FieldList;

/**
 * Responsible for selecting a base field for the strategies.
 * */
public class BaseSelector {
	/**
	 * The base.
	 * */
	private Field base;
	
	/**
	 * The player for choose the base.
	 * */
	private Player player;
	
	/**
	 * 
	 * */
	private Observer observer;
	
	/**
	 * Strategy to help the choice.
	 * */
	private FieldValueStrategy fieldValueStrategy;

	/**
	 * Constructor.
	 * @param player The player for choose the base.
	 * */
	public BaseSelector(Player player, Observer observer) {
		this.player = player;
		this.observer = observer;
		this.fieldValueStrategy = new FieldValueStrategy(player);
	}

	public void init(){
		this.fieldValueStrategy = new FieldValueStrategy(this.player);
	}

	/**
	 * Selects the base by values of the field.
	 * */
	public BaseSelector selectBaseByFieldValue(State state) {

		base = fieldValueStrategy.getBestValueOfFields();
		addObserverToFields(getReachableFieldsFromField(base, new ArrayList<>()));

		return this;
	}

	/**
	 * Selects the base from white fields.
	 * @param state The state of the game.
	 * 
	 * */
	public BaseSelector selectBaseFromWhiteFields(State state) {
		Map<Field, Integer> goodnessOfField = new HashMap<>();
		List<Field> whiteFields = new ArrayList<>();
		FieldList list = new FieldList(state.getTable().getFields());
		for (Field field : list.withColor(FieldColor.WHITE)) {
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

	/**
	 * Selects base randomly.
	 * @param state The state of the game.
	 * */
	public BaseSelector selectBaseByRandom(State state) {
		List<Operator> operators = Cache.getUseableOperators();
		Operator random = operators.get(new Random().nextInt(operators.size()));
		base = state.getFieldAt(random.getPosition());
		addObserverToFields(getReachableFieldsFromField(base, new ArrayList<>()));
		return this;
	}

	/**
	 * Checks if can reach the and from the base.
	 * @param state The state of the game.
	 * @return true if can reach the end from base.
	 * */
	public boolean canReachTheEndFromBase(State state) {
		if (base == null) {
			return false;
		}
		return canReachTheEnd(state, base);
	}
	
	/**
	 * Checks if can reach the end from a field.
	 * @param state The state of the game.
	 * @param field The field from the check starts
	 * @return true if can reach the end from the field.
	 * */
	private boolean canReachTheEnd(State state, Field field) {
		for (Direction direction : player.getDirections()) {
			boolean canReachTheEnd = canReachTheEndInDirection(state, field, direction, new ArrayList<>());
			if (!canReachTheEnd) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if can reach the end from a field in direction.
	 * @param state The state of the game.
	 * @param actual The field from the check starts.
	 * @param direction The direction where the check goes.
	 * @return true if can reach the end from the field in the direction.
	 * */
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

	/**
	 * Gets the reachable fields from a field.
	 * @param field The field which wanted to get the reachable fields.
	 * @param reachableFields List of the fields reachable from the field.
	 * @return The reachable fields from field.
	 * */
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

	/**
	 * Add observer to the fields.
	 * @param fields The fields to add the observer.
	 * */
	private void addObserverToFields(List<Field> fields) {
		for (Field field : fields) {
			field.addObserver(observer);
		}
	}

	/**
	 * Gets the base.
	 * @return The base.
	 * */
	public Field getBase() {
		return base;
	}

	
}
