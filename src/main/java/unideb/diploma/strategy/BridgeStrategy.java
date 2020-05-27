package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.cache.Direction;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.exception.StrategyCanNotChooseException;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.observer.Observable;
import unideb.diploma.observer.Observer;
import unideb.diploma.strategy.strength.StrategyStrength;

/**
 * Strategy which tries to create a bridge between the two sides.
 * */
public class BridgeStrategy implements Strategy, Observer {

	/**
	 * The base of the strategy.
	 * */
	private Field base;
	
	/**
	 * The player whom want to win.
	 * */
	private Player player;
	
	/**
	 * The base selector.
	 * */
	private BaseSelector baseSelector;

	/**
	 * Indicates if the base changed.
	 * */
	private boolean baseChanged;
	
	/**
	 * Direction where the opponent make his the last move.
	 * */
	private Direction direction;

	/**
	 * Constructor.
	 * @param player The player whom want to win.
	 * */
	public BridgeStrategy(Player player) {
		this.player = player;
		baseSelector = new BaseSelector(player, this);
	}

	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	@Override
	public Operator getNextMove(State state) {
		Field selectedField = null;
		Direction[] directions;
		if (baseChanged) {
			baseChanged = false;
			return Cache.getOperatorAt(base.getPosition());
		}
		if (direction == null) {
			directions = player.getDirections();
		} else {
			directions = new Direction[] { direction, player.getDirections()[0], player.getDirections()[1] };
		}

		for (Direction direction : directions) {
			if (!bridgeIsBuiltInDirection(base, direction)) {
				Field actual = base;
				while (selectedField == null && actual != null && actual.getColor() == player.getColor()) {
					actual = selectFieldByStateAndDirection(state, actual, direction);
				}
				if (selectedField != base && actual != base) {
					selectedField = actual;
				}
			}
		}

		if (selectedField == null) {
			throw new StrategyCanNotChooseException("Bridge strategy can not choose.");
		}
		return Cache.getOperatorAt(selectedField.getPosition());
	}

	/**
	 * Selects a field.
	 * @param state The state of the game.
	 * @param actual The actual field from where to choose.
	 * @param direction The direction where want to choose.
	 * @return The selected field.
	 * */
	private Field selectFieldByStateAndDirection(State state, Field actual, Direction direction) {
		List<Field> firstLevelNeighbours = new ArrayList<>();
		List<Field> secondLevelNeighbours = new ArrayList<>();
		Field selected;
		List<Field> neighbours = Cache.getNeighboursByDirection(direction, actual)
				.withoutColor(player.getOpponentColor());
		for (Field neighbour : neighbours) {
			firstLevelNeighbours.add(neighbour);
			secondLevelNeighbours.addAll(
					Cache.getNeighboursByDirection(direction, neighbour).withoutColor(player.getOpponentColor()));
		}
		secondLevelNeighbours.removeAll(neighbours);

		selected = getPlayersFieldFromList(secondLevelNeighbours, direction);
		if (selected == null) {
			selected = getPlayersFieldFromList(firstLevelNeighbours, direction);
		}
		if (selected == null) {
			selected = selectFieldFromList(actual, direction, secondLevelNeighbours, 2);
		}
		if (selected == null) {
			selected = selectFieldFromList(actual, direction, firstLevelNeighbours, 1);
		}

		return selected;
	}

	/**
	 * Checks if the bridge is built in the direction.
	 * @param field The field which checks if its at the edge.
	 * @param direction The direction where checks.
	 * @return true if the bridge is built in the direction.
	 * */
	private boolean bridgeIsBuiltInDirection(Field field, Direction direction) {

		if (player.getColor() == FieldColor.RED) {
			int x = field.getX();
			if (x == 0 || x == App.BOARD_SIZE - 1 || x == 1 || x == App.BOARD_SIZE - 2) {
				return true;
			}
		}

		if (player.getColor() == FieldColor.BLUE) {
			int y = field.getY();
			if (y == 0 || y == App.BOARD_SIZE - 1 || y == 1 || y == App.BOARD_SIZE - 2) {
				return true;
			}
		}

		for (Field actual : Cache.getNeighboursOfDistanceByDirection(direction, field, 1).withColor(player.getColor())) {
			return bridgeIsBuiltInDirection(actual, direction);
		}
		return false;
	}

	/**
	 * Gets a field with the color of the player from the list in the direction.
	 * @param fields The list of fields which choose.
	 * @return The chosen field.
	 * */
	private Field getPlayersFieldFromList(List<Field> fields, Direction direction) {
		List<Field> actualFields = new ArrayList<>();
		for (Field field : fields) {
			if (field.getColor() == player.getColor()) {
				actualFields.add(field);
			}
		}

		Field selected = null;
		int max = Integer.MIN_VALUE;
		for (Field field : actualFields) {
			List<Field> neighbours = Cache.getNeighboursByDirection(direction, field)
					.withoutColor(player.getOpponentColor());
			if (!neighbours.isEmpty() && max < neighbours.size()) {
				selected = field;
				max = neighbours.size();
			}
		}

		return selected;
	}

	/**
	 * Select a field from the list with the minimum appearance.
	 * @param actualField
	 * @param direction
	 * @param fields The fields from choose.
	 * @param minimum The minimum number of appearance in the list.
	 * @return The field selected.
	 * */
	private Field selectFieldFromList(Field actualField, Direction direction, List<Field> fields, int minimum) {
		Field selected = null;
		List<Field> fieldsWithMinimum = new ArrayList<>();
		for (Field field : fields) {
			int actualFieldCounter = (field.getColor() == FieldColor.WHITE) ? getFieldAppearances(fields, field)
					: Integer.MIN_VALUE;
			if (actualFieldCounter >= minimum) {
				fieldsWithMinimum.add(field);
			}
		}

		int reachableEndFieldsCounter = Integer.MIN_VALUE;

		for (Field field : fieldsWithMinimum) {
			int actualReachableEndFieldsCounter = (distanceIs(field, actualField, minimum))
					? 6 * getReachableEndFields(field, direction)
					: getReachableEndFields(field, direction);
			if (actualReachableEndFieldsCounter > reachableEndFieldsCounter) {
				reachableEndFieldsCounter = actualReachableEndFieldsCounter;
				selected = field;
			}
		}

		return selected;
	}

	/**
	 * Checks if the distance between two field is the distance given in the parameters.
	 * @param a Field one.
	 * @param b Field two.
	 * @param distance The distance for check between the two field.
	 * @return true 
	 * */
	private boolean distanceIs(Field a, Field b, int distance) {
		if (player.getColor() == FieldColor.BLUE) {
			if (Math.abs(a.getY() - b.getY()) == distance) {
				return true;
			}
		}
		if (player.getColor() == FieldColor.RED) {
			if (Math.abs(a.getX() - b.getX()) == distance) {
				return true;
			}
		}
		return false;
	}

	private int getReachableEndFields(Field field, Direction direction) {
		int sum = 0;
		for (Field actual : Cache.getNeighboursByDirection(direction, field).withoutColor(player.getOpponentColor())) {
			sum += getReachableEndFields(actual, direction);
		}
		if (player.getColor() == FieldColor.BLUE) {
			if (field.getY() == 0 || field.getY() == App.BOARD_SIZE - 1) {
				sum += 1;
			}
		}
		if (player.getColor() == FieldColor.RED) {
			if (field.getX() == 0 || field.getX() == App.BOARD_SIZE - 1) {
				sum += 1;
			}
		}
		return sum;
	}

	/**
	 * Gets the appearance of a field in the list.
	 * @param fields The list of fields.
	 * @param field The searched field.
	 * @return The number of the field appeared in the list.
	 * */
	private int getFieldAppearances(List<Field> fields, Field field) {
		int counter = 0;
		for (Field actField : fields) {
			if (actField.equals(field)) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Gets the goodness of the strategy by the state.
	 * @param state The state of the game.
	 * @return The strength of the strategy by the state.
	 * */
	@Override
	public StrategyStrength getGoodnessByState(State state) {
		if (base == null) {
	//		base = baseSelector.selectBaseFromWhiteFields(state).getBase();
			base = baseSelector.selectBaseByFieldValue(state).getBase();
			baseChanged = true;
		}
		if (baseSelector.canReachTheEndFromBase(state)) {
			return StrategyStrength.medium(3);
		} else {
	//		base = baseSelector.selectBaseFromWhiteFields(state).getBase();
			base = baseSelector.selectBaseByFieldValue(state).getBase();
			baseChanged = true;
			return StrategyStrength.veryWeak(0);
		}
	}

	/**
	 * Recalculating the base.
	 * @param state The state of the game.
	 * */
	public void reCalculateBase(State state) {
		base = baseSelector.selectBaseByFieldValue(state).getBase();
		baseChanged = true;
	}

	/**
	 * If the opponent makes a move, the strategy is notified.
	 * @param observable The field.
	 * */
	@Override
	public void notify(Observable observable) {
		Field field = (Field) observable;
		direction = null;
		if (player.getColor() == FieldColor.BLUE) {
			if (field.getY() > base.getY()) {
				direction = Direction.WEST;
			}
			if (field.getY() < base.getY()) {
				direction = Direction.EAST;
			}
		}
		if (player.getColor() == FieldColor.RED) {
			if (field.getX() > base.getX()) {
				direction = Direction.SOUTH;
			}
			if (field.getX() < base.getX()) {
				direction = Direction.NORTH;
			}
		}
	}

}
