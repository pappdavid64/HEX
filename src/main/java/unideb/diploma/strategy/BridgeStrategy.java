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
				Field previous = null;
				while(	selectedField == null ) {
					List<Field> neighbours = new ArrayList<>();
					for(Field neighbour : Cache.getNeighboursByDirection(direction, actual)) {
						FieldColor color = state.getFieldAt(neighbour.getPosition()).getColor();
						if(color == FieldColor.WHITE || color  == player.getColor()) {
							neighbours.addAll(Cache.getNeighboursByDirection(direction, neighbour));
						}
						
					}
					checkForGoodnessOfElements(state, direction, neighbours);
					previous = actual;
					actual = getPlayersFieldFromList(neighbours, state);
					if(actual == null) {
						selectedField = selectFieldFromList(neighbours, 2, state);
//						if(selectedField == null) {
//							selectedField = getFieldInOneRange(previous, direction, state);
//						}
						break;
					}
				}
			}
		}
		if(selectedField == null) {
			throw new StrategyCanNotChooseException("Bridge strategy can not choose.");
		}
		return Cache.getOperatorAt(selectedField.getPosition());
	}
	
	private void checkForGoodnessOfElements(State state, Direction direction, List<Field> neighbours) {
		List<Field> removedElements = new ArrayList<>();
		for(Field field : neighbours) {
			List<Field> neighboursByDirection = Cache.getNeighboursByDirection(direction, field);
			for(Field actual : neighboursByDirection) {
				FieldColor opponentColor = (player.getColor() == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE; 
				if(state.getFieldAt(actual.getPosition()).getColor() == opponentColor) {
					removedElements.add(field);
				}
			}
		}
		neighbours.removeAll(removedElements);
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
		
		for(Field actual : Cache.getNeighboursOfLevelByDirection(direction, field, 1)) {
			if(actual.getColor() == player.getColor()) {
				return bridgeIsBuiltInDirection(actual, direction);				
			}
		}
		return false;
	}

	private Field getFieldInOneRange(Field field,Direction direction, State state) {
		Field actual = field;
		List<Field> neighbours = new ArrayList<>();
		for(Field neighbour : Cache.getNeighboursByDirection(direction, actual)) {
			FieldColor color = state.getFieldAt(neighbour.getPosition()).getColor();
			if( (color == FieldColor.WHITE || color  == player.getColor()) ) {
				neighbours.add(neighbour);
			}
		}

		return selectFieldFromList(neighbours, 1, state);
	}

	private Field getPlayersFieldFromList(List<Field> fields, State state) {
		for(Field field : fields) {
			if(state.getFieldAt(field.getPosition()).getColor() == player.getColor()) {
				return field;
			}
		}
		return null;
	}
	
	private Field selectFieldFromList(List<Field> fields, int minimumAppearence, State state) {
		Field selected = null;
		int max = Integer.MIN_VALUE;
		for(Field field : fields) {
			int actualFieldCounter = (state.getFieldAt(field.getPosition()).getColor() == FieldColor.WHITE) ? getFieldAppearences(fields, field) : Integer.MIN_VALUE;
			if( actualFieldCounter >= minimumAppearence && actualFieldCounter > max) {
				max = actualFieldCounter;
				selected = field;
			}
		}
		return selected;
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
			baseChanged = true;
			base = baseSelector.selectBaseFromWhiteFields(state).getBase();
//			base = baseSelector.selectBaseByRandom(state).getBase();
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
