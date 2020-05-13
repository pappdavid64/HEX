package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;
import unideb.diploma.util.FieldList;

public class Cache {
	private static final OperatorCache operatorCache;
	private static final StateCache stateCache;
	private static final VirtualConnectionCache virtualConnnectionCache;
	private static final List<FieldNeighboursCache> fieldsNeighbours;

	static {
		operatorCache = new OperatorCache();
		stateCache = new StateCache();
		virtualConnnectionCache = new VirtualConnectionCache();
		fieldsNeighbours = FieldNeighboursCache.getFieldsNeighbours();
	}

	public static void reset() {
		operatorCache.fillUseableOperators();
		stateCache.resetState();
		virtualConnnectionCache.reset();
	}

	public static Operator getOperatorAt(Position position) {
		return operatorCache.getOperatorAt(position);
	}

	public static Operator getOperatorAt(int x, int y) {
		return getOperatorAt(new Position(x, y));
	}

	public static void removeOperatorFromUseableOperators(Operator operator) {
		operatorCache.removeOperatorFromUseableOperators(operator);
	}

	public static void resetUseableOperators() {
		operatorCache.fillUseableOperators();
	}

	public static List<Operator> getUseableOperators() {
		return operatorCache.getUseableOperators();
	}

	public static void addState(State state) {
		stateCache.addStateToCache(state);
	}

	public static void removeState(State state) {
		stateCache.removeStateFromCache(state);
	}

	public static void clearStates() {
		stateCache.clearStates();
	}

	public static List<State> getStates() {
		return stateCache.getStates();
	}

	public static State getState() {
		return stateCache.getState();
	}

	public static void resetState() {
		stateCache.resetState();
	}

	public static void registerPlayer(Player player) {
		virtualConnnectionCache.registerPlayer(player);
	}

	public static void addVirtualConenction(Player player, VirtualConnection connection) {
		if (connection.getConnectionsCount() != 0) {
			virtualConnnectionCache.addVirtualConnection(player, connection);
		}
	}

	public static void removeVirtualConnection(Player player, VirtualConnection connection) {
		virtualConnnectionCache.removeVirtualConnection(player, connection);
	}

	public static List<VirtualConnection> getVirtualConnectionsOf(Player player) {
		removeVirtualConnectionsWithZeroField(player);
		return virtualConnnectionCache.getVirtualConnectionsOf(player);
	}

	public static void addVirtualConnectionsFromField(Player player, Field field, State state) {
		for (VirtualConnection connection : virtualConnnectionCache.getVirtualConnectionsFrom(player, field, state)) {
			virtualConnnectionCache.addVirtualConnection(player, connection);
		}
	}

	public static VirtualConnection getConnectionFromField(Player player, Field field) {
		return virtualConnnectionCache.getVirtualConnection(player, field);
	}

	public static void printVIrtualConnectionsWithPlayers() {
		virtualConnnectionCache.print();
	}

	public static List<Field> getNeighbours(Field field) {
		return getFieldNeighboursCacheByField(field).getNeighbours();
	}

	public static Field getNeighbourByDirection(Field field, Direction direction) {
		Field neighbour = null;
		FieldNeighboursCache fnc = getFieldNeighboursCacheByField(field);
		switch (direction) {
		case EAST:
			neighbour = fnc.getEastNeighbour();
			break;
		case WEST:
			neighbour = fnc.getWestNeighbour();
			break;
		case SOUTH:
			neighbour = fnc.getSouthNeighbour();
			break;
		case NORTH:
			neighbour = fnc.getNorthNeighbour();
			break;
		case NORTH_EAST:
			neighbour = fnc.getNorthEastNeighbour();
			break;
		case SOUTH_WEST:
			neighbour = fnc.getSouthWestNeighbour();
			break;
		}
		return neighbour;
	}

	public static FieldList getNeighboursByDirection(Direction direction, Field field) {
		FieldList neighbours = null;
		if (direction == Direction.EAST) {
			neighbours = getEastNeighbours(field);
		}
		if (direction == Direction.WEST) {
			neighbours = getWestNeighbours(field);
		}
		if (direction == Direction.NORTH) {
			neighbours = getNorthNeighbours(field);
		}
		if (direction == Direction.SOUTH) {
			neighbours = getSouthNeighbours(field);
		}
		if (direction == Direction.NORTH_EAST) {
			neighbours = getNorthEastNeighbours(field);
		}
		if (direction == Direction.SOUTH_WEST) {
			neighbours = getSouthWestNeighbours(field);
		}
		return neighbours;
	}

	public static FieldList getSouthNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.SOUTH),
				getNeighbourByDirection(field, Direction.SOUTH_WEST), getNeighbourByDirection(field, Direction.EAST)
//						getNeighbourByDirection(field, Direction.WEST)
		));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	public static FieldList getWestNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.WEST),
				getNeighbourByDirection(field, Direction.SOUTH_WEST), getNeighbourByDirection(field, Direction.SOUTH)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	public static FieldList getNorthNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.NORTH),
				getNeighbourByDirection(field, Direction.NORTH_EAST), getNeighbourByDirection(field, Direction.WEST)
//						getNeighbourByDirection(field, Direction.EAST)
		));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	public static FieldList getEastNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.EAST),
				getNeighbourByDirection(field, Direction.NORTH_EAST), getNeighbourByDirection(field, Direction.NORTH)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	public static FieldList getNorthEastNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.NORTH),
				getNeighbourByDirection(field, Direction.EAST), getNeighbourByDirection(field, Direction.NORTH_EAST)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	public static FieldList getSouthWestNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.SOUTH),
				getNeighbourByDirection(field, Direction.WEST), getNeighbourByDirection(field, Direction.SOUTH_WEST)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	public static List<Field> getNeighboursOfLevel(Field field, int level) {
		return getFieldNeighboursCacheByField(field).getNeighboursOfLevel(level);
	}

	public static List<Field> getNeighboursOfLevelByDirection(Direction direction, Field field, int level) {
		return getFieldNeighboursCacheByField(field).getNeighboursOfLevelByDirection(direction, level);
	}

	public static List<Field> withoutColor(List<Field> elements, FieldColor color) {
		List<Field> copyOfElements = new ArrayList<>(elements);
		List<Field> forRemove = new ArrayList<>();
		copyOfElements.forEach((element) -> {
			if (element.getColor() == color) {
				forRemove.add(element);
			}
		});
		copyOfElements.removeAll(forRemove);
		return copyOfElements;
	}

	public static List<Field> withColor(List<Field> elements, FieldColor color) {
		List<Field> copyOfElements = new ArrayList<>(elements);
		List<Field> forRemove = new ArrayList<>();
		copyOfElements.forEach((element) -> {
			if (element.getColor() != color) {
				forRemove.add(element);
			}
		});
		copyOfElements.removeAll(forRemove);
		return copyOfElements;
	}

	private static FieldNeighboursCache getFieldNeighboursCacheByField(Field field) {
		FieldNeighboursCache fieldNeighboursCache = null;
		for (FieldNeighboursCache fnc : fieldsNeighbours) {
			if (fnc.fieldIsEqual(field)) {
				fieldNeighboursCache = fnc;
			}
		}
		return fieldNeighboursCache;
	}

	private static void removeVirtualConnectionsWithZeroField(Player player) {
		List<VirtualConnection> zeroFildedConnections = new ArrayList<>();
		for (VirtualConnection connection : virtualConnnectionCache.getVirtualConnectionsOf(player)) {
			if (connection.getConnections().isEmpty()) {
				zeroFildedConnections.add(connection);
			}
		}
		virtualConnnectionCache.removeVirtualConnections(player, zeroFildedConnections);
	}

	private Cache() {
	}
}
