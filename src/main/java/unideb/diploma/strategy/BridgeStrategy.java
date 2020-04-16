package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.cache.Direction;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.logic.ai.AIPlayer;
import unideb.diploma.strategy.connection.VirtualConnection;
import unideb.diploma.strategy.connection.VirtualField;

public class BridgeStrategy implements Strategy{

	private Field base;
	private Player player;
	private boolean active;
	
	public BridgeStrategy(Player player) {
		this.player = player;
		active = true;
	}
	
	@Override
	public Operator getNextMove(State state) {
		Field selectedField = null;
		Field previousField = null;
		for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
			if(connection.getConnectionsCount() == 1) {
				selectedField = connection.getConnections().get(0);
				Cache.removeVirtualConnection(player, connection);
				break;
			}
		}
		for(Direction direction : player.getDirections()) {
			Field actual = base;
			while(	selectedField == null ) {
				List<Field> neighbours = new ArrayList<>();
				for(Field neighbour : Cache.getNeighboursByDirection(direction, actual)) {
					FieldColor color = state.getFieldAt(neighbour.getPosition()).getColor();
					if(color == FieldColor.WHITE || color  == player.getColor()) {
						neighbours.addAll(Cache.getNeighboursByDirection(direction, neighbour));
					}
				}
				previousField = actual;
				actual = getPlayersFieldFromList(neighbours, state);
				if(actual == null) {
					selectedField = selectFieldFromList(neighbours, state);
					if(isOneFieldAwayFromEnd(previousField, direction)) {
						Cache.addVirtualConenction(player, new VirtualConnection(previousField, getVirtualField(previousField, direction)));
					}
					break;
				}
			}
		}
		return Cache.getOperatorAt(selectedField.getPosition());
	}


	private VirtualField getVirtualField(Field selectedField, Direction direction) {
		VirtualField virtual = null;
		Position position = selectedField.getPosition();
		switch (direction) {
			case EAST:
				virtual = new VirtualField(position.getX() - 1, position.getY() + 2);
				break;
			case WEST:
				virtual = new VirtualField(position.getX() + 1, position.getY() - 2);
				break;
			case NORTH:
				virtual = new VirtualField(position.getX() - 2, position.getY() + 1);
				break;
			case SOUTH:
				virtual = new VirtualField(position.getX() + 2, position.getY() - 1);
				break;
			default:
				break;
		}
		return virtual;
	}

	private boolean isOneFieldAwayFromEnd(Field selectedField, Direction direction) {
		return Cache.getNeighboursOfLevelByDirection(direction, selectedField, 1).isEmpty();
	}

	private Field getPlayersFieldFromList(List<Field> fields, State state) {
		for(Field field : fields) {
			if(state.getFieldAt(field.getPosition()).getColor() == player.getColor()) {
				return field;
			}
		}
		return null;
	}
	
	private Field selectFieldFromList(List<Field> fields, State state) {
		Field selected = null;
		int max = Integer.MIN_VALUE;
		for(Field field : fields) {
			int actualFieldCounter = (state.getFieldAt(field.getPosition()).getColor() == FieldColor.WHITE) ? getFieldAppearences(fields, field) : Integer.MIN_VALUE;
			if( actualFieldCounter > max) {
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
	public int getGoodnessByState(State state) {
		if(base == null) {
			base = ((AIPlayer)player).getBase();
		}
		boolean canReach = canReachTheEnd(state);
		return canReach ? Integer.MAX_VALUE : Integer.MIN_VALUE;
	}

	private boolean canReachTheEnd(State state) {
		if(base == null) {
			return false;
		}
		
		for(Direction direction : player.getDirections()) {
			boolean canReachTheEnd = canReachTheEndInDirection(state, base, direction);
			if(!canReachTheEnd) {
				return false;
			}
		}
		
		return true;
	}

	private boolean canReachTheEndInDirection(State state, Field actual, Direction direction) {
		for(Field field : Cache.getNeighboursByDirection(direction, actual)) {
			if(field.getX() == 0 || field.getX() == App.BOARD_SIZE - 1) {
				return true;
			}
			if(field.getY() == 0 || field.getY() == App.BOARD_SIZE - 1) {
				return true;
			}
			FieldColor color = state.getFieldAt(field.getPosition()).getColor();
			if(color == FieldColor.WHITE || color == player.getColor()) {
				return canReachTheEndInDirection(state, field, direction);
			}
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
