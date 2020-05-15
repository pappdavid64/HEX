package unideb.diploma.strategy.connection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

/**
 * Utility class for making checks for a virtual connection.
 * */
public class ConnectionUtil {
	
	/**
	 * Checks if its the only connection for that field.
	 * @param connection The virtual connection under check.
	 * @param player The player whose connection is checked.
	 * @param state The state of the game.
	 * @return true if it is the only one conenction.
	 * */
	public static boolean isTheOnlyOneConnection(VirtualConnection connection, Player player, State state) {
		List<VirtualConnection> connections = getVirtualConnectionsBetween(connection.getA(), connection.getB(), player, state);
		Set<Field> fieldsConnectedBy = new HashSet<>();
		for(VirtualConnection actual : connections) {
			if(actual.hasVirtualField() == connection.hasVirtualField()) {
				fieldsConnectedBy.addAll(actual.getConnections());
			}
		}
		return fieldsConnectedBy.size() == 1;
	}
	
	/**
	 * Gets the virtual connections between two fields.
	 * @param a First field.
	 * @param b Second field.
	 * @param player The player whose virtual connection is searched.
	 * @param state The state of the game.
	 * @return The list of virtual connections between the two field.
	 * */
	public static List<VirtualConnection> getVirtualConnectionsBetween(Field a, Field b, Player player, State state){
		List<VirtualConnection> connections = new ArrayList<>();
		List<Field> roadOne = getRoadFrom(a, player, state, new ArrayList<>());
		List<Field> roadTwo = getRoadFrom(b, player, state, new ArrayList<>());
		if(roadTwo != null) {
			for(Field roadOneField : roadOne) {
				for(Field roadTwoField : roadTwo) {
					connections.add(getVirtualConnectionBetween(roadOneField, roadTwoField, player));
				}
			}
		} else {
			for(Field roadOneField : roadOne) {
				connections.add(getVirtualConnectionBetween(roadOneField, null, player));
			}
		}
		connections.removeIf(Objects::isNull);
		return connections;
	}
	
	/**
	 * Gets the virtual connection between two fields.
	 * @param a First field.
	 * @param b Second field.
	 * @param player The player whose virtual connection is searched.
	 * @param state The state of the game.
	 * @return The virtual connection between the two field.
	 * */
	public static VirtualConnection getVirtualConnectionBetween(Field a, Field b, Player player) {
		VirtualConnection selected = null;
		for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
			if(connection.isThatConnection(a, b)) {
				selected = connection;
			}
		}
		return selected;
	}
	
	/**
	 * Gets the reachable fields from a field.
	 * @param field The field.
	 * @param player The player whose fields are searched.
	 * @param road The already found fields.
	 * @return The list of reachable fields.
	 * */
	private static List<Field> getRoadFrom(Field field, Player player, State state, List<Field> road) {
		
		if(field == null) {
			return null;
		}
		
		if(field.getColor() != player.getColor() || road.contains(field)) {
			return road;
		}
		
		road.add(field);
		
		for(Field neighbour : Cache.getNeighbours(field)) {
			getRoadFrom(neighbour, player, state, road);
		}
		
		return road;
	}

}
