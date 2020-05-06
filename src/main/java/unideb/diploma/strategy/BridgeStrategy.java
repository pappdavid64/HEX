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

public class BridgeStrategy implements Strategy, Observer{

	private Field base;
	private Player player;
	private BaseSelector baseSelector;
	private boolean active;
	private boolean baseChanged;
	private Direction direction;
	
	
	public BridgeStrategy(Player player) {
		this.player = player;
		active = true;
		baseSelector = new BaseSelector(player, this);
	}
	
	@Override
	public Operator getNextMove(State state) {
		Field selectedField = null;
		Direction[] directions;
		if(baseChanged) {
			baseChanged = false;
			return Cache.getOperatorAt(base.getPosition());
		}
		if(direction == null) {
			directions = player.getDirections();
		} else {
			directions = new Direction[] {direction, player.getDirections()[0], player.getDirections()[1]};
		}
		
		for(Direction direction : directions) {
			if(!bridgeIsBuiltInDirection(base, direction)) {
				Field actual = base;
				while(selectedField == null && actual.getColor() == player.getColor()) {
					actual = selectField(state, actual, direction);
				}
				if(selectedField != base && actual != base) {
					selectedField = actual;
				}
			}
		}

		if(selectedField == null) {
			throw new StrategyCanNotChooseException("Bridge strategy can not choose.");
		}
		return Cache.getOperatorAt(selectedField.getPosition());
	}
	
	private Field selectField(State state, Field actual, Direction direction) {
		List<Field> firstLevelNeighbours = new ArrayList<>();
		List<Field> secondLevelNeighbours = new ArrayList<>();
		Field selected;
		List<Field> neighbours =  Cache.withoutColor(Cache.getNeighboursByDirection(direction, actual), player.getOpponentColor());
		for(Field neighbour : neighbours) {
			firstLevelNeighbours.add(neighbour);
			secondLevelNeighbours.addAll(Cache.withoutColor(Cache.getNeighboursByDirection(direction, neighbour), player.getOpponentColor()));
		}
		secondLevelNeighbours.removeAll(neighbours);
		
		selected = getPlayersFieldFromList(secondLevelNeighbours, direction);
		if(selected == null) {
			selected = getPlayersFieldFromList(firstLevelNeighbours, direction);
		}
		if(selected == null) {
			selected = selectFieldFromList(actual, direction, secondLevelNeighbours, 2);
		}		
		if(selected == null) {
			selected = selectFieldFromList(actual, direction, firstLevelNeighbours, 1);
		}

		return selected;
	}
	
	private boolean bridgeIsBuiltInDirection(Field field, Direction direction) {

		if(player.getColor() == FieldColor.RED) {
			int x = field.getX();
			if(x == 0 || x == App.BOARD_SIZE - 1 || x == 1 || x == App.BOARD_SIZE - 2) {
				return true;
			}
		}
		
		if(player.getColor() == FieldColor.BLUE) {
			int y = field.getY();
			if(y == 0 || y == App.BOARD_SIZE - 1  || y == 1 || y == App.BOARD_SIZE - 2) {
				return true;
			}
		}
		
		for(Field actual : Cache.withColor(Cache.getNeighboursOfLevelByDirection(direction, field, 1), player.getColor())) {
			return bridgeIsBuiltInDirection(actual, direction);							
		}
		return false;
	}

	private Field getPlayersFieldFromList(List<Field> fields, Direction direction) {
		List<Field> actualFields = new ArrayList<>();
		for(Field field : fields) {
			if(field.getColor() == player.getColor()) {
				actualFields.add(field);
			}
		}
		
		Field selected = null;
		int max = Integer.MIN_VALUE;
		for(Field field : actualFields) {
			List<Field> neighbours = Cache.withoutColor(Cache.getNeighboursByDirection(direction, field), player.getOpponentColor());
			if(!neighbours.isEmpty() && max < neighbours.size()) {
				selected = field;
				max = neighbours.size();
			}
		}
		
		return selected;
	}
	
	private Field selectFieldFromList(Field actualField, Direction direction, List<Field> fields, int minimum) {
		Field selected = null;
		List<Field> fieldsWithMinimum = new ArrayList<>();
		for(Field field : fields) {
			int actualFieldCounter = (field.getColor() == FieldColor.WHITE) ? getFieldAppearences(fields, field) : Integer.MIN_VALUE;
			if( actualFieldCounter >= minimum) {
				fieldsWithMinimum.add(field);
			}
		}
		
		int reachableEndFieldsCounter = Integer.MIN_VALUE;
		
		for(Field field : fieldsWithMinimum) {
			int actualReachableEndFieldsCounter = (distanceIs(field, actualField, minimum)) ? 6 * getReachableEndFields(field, direction) : getReachableEndFields(field, direction);
			if(actualReachableEndFieldsCounter > reachableEndFieldsCounter) {
				reachableEndFieldsCounter = actualReachableEndFieldsCounter;
				selected = field;
			}
		}
		
		return selected;
	}

	private boolean distanceIs(Field field, Field actualField, int distance) {
		if(player.getColor() == FieldColor.BLUE) {
			if(Math.abs(field.getY() - actualField.getY() ) == distance) {
				return true;
			}
		}
		if(player.getColor() == FieldColor.RED) {
			if(Math.abs(field.getX() - actualField.getX() ) == distance) {
				return true;
			}
		}
		return false;
	}

	private int getReachableEndFields(Field field, Direction direction) {
		int sum = 0;
		for(Field actual : Cache.withoutColor(Cache.getNeighboursByDirection(direction, field),player.getOpponentColor())) {
			sum += getReachableEndFields(actual, direction);
		}
		if(player.getColor() == FieldColor.BLUE) {
			if(field.getY() == 0 || field.getY() == App.BOARD_SIZE - 1) {
				sum += 1;
			}
		}
		if(player.getColor() == FieldColor.RED) {
			if(field.getX() == 0 || field.getX() == App.BOARD_SIZE - 1) {
				sum += 1;
			}
		}
		return sum;
	}

	private int getFieldAppearences(List<Field> fields, Field field) {
		int counter = 0;
		for(Field actField : fields) {
			if(actField.equals(field)) {
				counter++;
			}
		}
		return counter;
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		if(base == null) {
			base = baseSelector.selectBaseFromWhiteFields(state).getBase();
			baseChanged = true;
		}
		if(baseSelector.canReachTheEndFromBase(state)) { 
			return StrategyStrength.medium(3);
		} else {
			base = baseSelector.selectBaseFromWhiteFields(state).getBase();
			baseChanged = true;
			return StrategyStrength.veryWeak(0);
		}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void deActivate() {
//		active = false;
		base = null;
	}

	@Override
	public void activate() {
		active = true;		
	}

	@Override
	public void reCalculate(State state) {
		base = baseSelector.selectBaseFromWhiteFields(state).getBase();
	}

	@Override
	public void notify(Observable observable) {
		Field field = (Field)observable;
		direction = null;
		if(player.getColor() == FieldColor.BLUE) {
			if(field.getY() > base.getY()) {
				direction = Direction.WEST;
			}
			if(field.getY() < base.getY()) {
				direction = Direction.EAST;
			}
		}
		if(player.getColor() == FieldColor.RED) {
			if(field.getX() > base.getX()) {
				direction = Direction.SOUTH;
			}
			if(field.getX() < base.getX()) {
				direction = Direction.NORTH;
			}
		}
	}
	
}
