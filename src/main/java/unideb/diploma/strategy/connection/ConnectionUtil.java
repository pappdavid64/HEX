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

public class ConnectionUtil {
	
	public static boolean isTheOnlyOneConnection(VirtualConnection connection, Player player, State state) {
		List<VirtualConnection> connections = getVirtualConnectionsBetween(connection.getA(), connection.getB(), player, state);
		Set<Field> fieldsConnectedBy = new HashSet<>();
		for(VirtualConnection actual : connections) {
			fieldsConnectedBy.addAll(actual.getConnections());
		}
		return fieldsConnectedBy.size() == 1;
	}
	
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
	
	public static VirtualConnection getVirtualConnectionBetween(Field a, Field b, Player player) {
		VirtualConnection selected = null;
		for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
			if(connection.isThatConnection(a, b)) {
				selected = connection;
			}
		}
		return selected;
	}
	
	private static List<Field> getRoadFrom(Field field, Player player, State state, List<Field> road) {
		
		if(field == null) {
			return null;
		}
		
		if(state.getFieldAt(field.getPosition()).getColor() != player.getColor() || road.contains(field)) {
			return road;
		}
		
		road.add(field);
		
		for(Field neighbour : Cache.getNeighbours(field)) {
			getRoadFrom(neighbour, player, state, road);
		}
		
		return road;
	}

}
