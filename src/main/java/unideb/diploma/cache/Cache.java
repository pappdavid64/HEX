package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;
import unideb.diploma.util.FieldList;

/**
 * Cache for states, operators, virtual connections and fields neighbours.
 * */
public class Cache {
	/**
	 * Cache for operators.
	 * */
	private static OperatorCache operatorCache;
	
	/**
	 * Cache for states.
	 * */
	private static StateCache stateCache;
	
	/**
	 * Cache for virtual connections.
	 * */
	private static VirtualConnectionCache virtualConnnectionCache;
	
	/**
	 * Cache for fields neighbours.
	 * */
	private static List<FieldNeighboursCache> fieldsNeighbours;

	static {
		operatorCache = new OperatorCache();
		stateCache = new StateCache();
		virtualConnnectionCache = new VirtualConnectionCache();
		fieldsNeighbours = FieldNeighboursCache.getFieldsNeighbours();
	}

	/**
	 * Reseting the caches.
	 * */
	public static void reset() {
		operatorCache.fillUseableOperators();
		stateCache.resetState();
		virtualConnnectionCache.reset();
        FieldNeighboursCache.reset();
        fieldsNeighbours = FieldNeighboursCache.getFieldsNeighbours();
	}

	/**
	 * Gets the operator at position.
	 * @param position The position.
	 * @return The operator at the position.
	 * */
	public static Operator getOperatorAt(Position position) {
		return operatorCache.getOperatorAt(position);
	}

	/**
	 * Gets the operator at position(x,y).
	 * @param x The row.
	 * @param y The column.
	 * @return The operator at the position(x,y).
	 * */
	public static Operator getOperatorAt(int x, int y) {
		return getOperatorAt(new Position(x, y));
	}

	/**
	 * Removes the operator from useable operators.
	 * @param operator The operator which will be removed.
	 * */
	public static void removeOperatorFromUseableOperators(Operator operator) {
		operatorCache.removeOperatorFromUseableOperators(operator);
	}

	/**
	 * Reseting the useable operators.
	 * */
	public static void resetUseableOperators() {
		operatorCache.fillUseableOperators();
	}

	/**
	 * Gets the useable operators.
	 * @return The useable operators.
	 * */
	public static List<Operator> getUseableOperators() {
		return operatorCache.getUseableOperators();
	}

	/**
	 * Adding a state.
	 * @param state The state which will be added.
	 * */
	public static void addState(State state) {
		stateCache.addStateToCache(state);
	}

	/**
	 * Removing a state.
	 * @param state The state which will be removed.
	 * */
	public static void removeState(State state) {
		stateCache.removeStateFromCache(state);
	}

	/**
	 * Reseting the states of the state cache.
	 * */
	public static void clearStates() {
		stateCache.clearStates();
	}

	/**
	 * Gets the states of the state cache.
	 * @return The states of the state cache.
	 * */
	public static List<State> getStates() {
		return stateCache.getStates();
	}

	/**
	 * Gets the actual state.
	 * */
	public static State getState() {
		return stateCache.getState();
	}

	/**
	 * Resetting the actual state.
	 * */
	public static void resetState() {
		stateCache.resetState();
	}

	/**
	 * Register a player to the virtual connection cache.
	 * @param player The player.
	 * */
	public static void registerPlayer(Player player) {
		virtualConnnectionCache.registerPlayer(player);
	}

	/**
	 * Adding a virtual connection to the player.
	 * @param player The player.
	 * @param connection The connection which will be added.
	 * */
	public static void addVirtualConnection(Player player, VirtualConnection connection) {
		if (connection.getConnectionsCount() != 0) {
			virtualConnnectionCache.addVirtualConnection(player, connection);
		}
	}

	/**
	 * Removes a virtual connection from the player.
	 * @param player The player.
	 * @param connection The connection which will be removed.
	 * */
	public static void removeVirtualConnection(Player player, VirtualConnection connection) {
		virtualConnnectionCache.removeVirtualConnection(player, connection);
	}

	/**
	 * Returns the virtual connections of the player.
	 * @param player The player.
	 * @return The virtual connections.
	 * */
	public static List<VirtualConnection> getVirtualConnectionsOf(Player player) {
		removeVirtualConnectionsWithZeroField(player);
		return virtualConnnectionCache.getVirtualConnectionsOf(player);
	}

	/**
	 * Adding virtual connection to the player from a field.
	 * @param player The player.
	 * @param field The field.
	 * @param state The state of the game.
	 * */
	public static void addVirtualConnectionsFromField(Player player, Field field, State state) {
		for (VirtualConnection connection : virtualConnnectionCache.getVirtualConnectionsFrom(player, field, state)) {
			virtualConnnectionCache.addVirtualConnection(player, connection);
		}
	}

	/**
	 * Gets the virtual connection of a field.
	 * @param player The player.
	 * @param field The field.
	 * */
	public static VirtualConnection getConnectionFromField(Player player, Field field) {
		return virtualConnnectionCache.getVirtualConnection(player, field);
	}

	/**
	 * Gets the neighbours of the field.
	 * @param field The field.
	 * @return The neighbours of the field.
	 * */
	public static FieldList getNeighbours(Field field) {
		return new FieldList(getFieldNeighboursCacheByField(field).getNeighbours());
	}

	/**
	 * Gets the neighbour of the field by direction.
	 * @param field The field.
	 * @param direction The direction
	 * @return The neighbour of the field.
	 * */
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
	
	/**
	 * Gets the neighbours of the field by direction.
	 * @param field The field.
	 * @param direction The direction
	 * @return The neighbours of the field.
	 * */
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

	/**
	 * Gets the south neighbour of the field.
	 * @param field The field.
	 * @return The south neighbours.
	 * */
	public static FieldList getSouthNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.SOUTH),
				getNeighbourByDirection(field, Direction.SOUTH_WEST), getNeighbourByDirection(field, Direction.EAST)
//						getNeighbourByDirection(field, Direction.WEST)
		));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	/**
	 * Gets the west neighbour of the field.
	 * @param field The field.
	 * @return The west neighbours.
	 * */
	public static FieldList getWestNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.WEST),
				getNeighbourByDirection(field, Direction.SOUTH_WEST), getNeighbourByDirection(field, Direction.SOUTH)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	/**
	 * Gets the north neighbour of the field.
	 * @param field The field.
	 * @return The north neighbours.
	 * */
	public static FieldList getNorthNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.NORTH),
				getNeighbourByDirection(field, Direction.NORTH_EAST), getNeighbourByDirection(field, Direction.WEST)
//						getNeighbourByDirection(field, Direction.EAST)
		));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	/**
	 * Gets the east neighbour of the field.
	 * @param field The field.
	 * @return The east neighbours.
	 * */
	public static FieldList getEastNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.EAST),
				getNeighbourByDirection(field, Direction.NORTH_EAST), getNeighbourByDirection(field, Direction.NORTH)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	/**
	 * Gets the north-east neighbour of the field.
	 * @param field The field.
	 * @return The north-east neighbours.
	 * */
	public static FieldList getNorthEastNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.NORTH),
				getNeighbourByDirection(field, Direction.EAST), getNeighbourByDirection(field, Direction.NORTH_EAST)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	/**
	 * Gets the south-west neighbour of the field.
	 * @param field The field.
	 * @return The south-west neighbours.
	 * */
	public static FieldList getSouthWestNeighbours(Field field) {
		List<Field> neighbours = new ArrayList<>(Arrays.asList(getNeighbourByDirection(field, Direction.SOUTH),
				getNeighbourByDirection(field, Direction.WEST), getNeighbourByDirection(field, Direction.SOUTH_WEST)));
		neighbours.removeIf(Objects::isNull);
		return new FieldList(neighbours);
	}

	/**
	 * Gets the neighbours of a field which distance is equals to  the distance.
	 * @param field The field.
	 * @param distance The distance.
	 * @return The neighbours.
	 * */
	public static FieldList getNeighboursOfDistance(Field field, int distance) {
		return new FieldList(getFieldNeighboursCacheByField(field).getNeighboursOfDistance(distance));
	}

	/**
	 * Gets the neighbours of a field in direction which distance is equals to  the distance.
	 * @param direction The direction.
	 * @param field The field.
	 * @param distance The distance.
	 * @return The neighbours.
	 * */
	public static FieldList getNeighboursOfDistanceByDirection(Direction direction, Field field, int distance) {
		return new FieldList(getFieldNeighboursCacheByField(field).getNeighboursOfDistanceByDirection(direction, distance));
	}

	/**
	 * Gets a field neighbours cache by its field.
	 * @param field The field.
	 * @return The fields neighbours cache.
	 * */
	private static FieldNeighboursCache getFieldNeighboursCacheByField(Field field) {
		FieldNeighboursCache fieldNeighboursCache = null;
		for (FieldNeighboursCache fnc : fieldsNeighbours) {
			if (fnc.fieldIsEqual(field)) {
				fieldNeighboursCache = fnc;
			}
		}
		return fieldNeighboursCache;
	}

	/**
	 * Removes virtual connections with zero field.
	 * @param player Whose virtual connections will be checked.
	 * */
	private static void removeVirtualConnectionsWithZeroField(Player player) {
		List<VirtualConnection> zeroFildedConnections = new ArrayList<>();
		for (VirtualConnection connection : virtualConnnectionCache.getVirtualConnectionsOf(player)) {
			if (connection.getConnections().isEmpty()) {
				zeroFildedConnections.add(connection);
			}
		}
		virtualConnnectionCache.removeVirtualConnections(player, zeroFildedConnections);
	}

	/**
	 * Private constructor.
	 * Prevents getting an instance from the cache.
	 * */
	private Cache() {
	}
}
