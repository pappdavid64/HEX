package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.Position;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;
import unideb.diploma.strategy.connection.VirtualField;

public class VirtualConnectionCache {
	private List<Map> playersVirtualConnection;
	
	public VirtualConnectionCache() {
		playersVirtualConnection = new ArrayList<>();
	}
	
	void registerPlayer(Player player) {
		if(search(player) == null) {
			playersVirtualConnection.add(new Map(player, new ArrayList<>()));
		}
	}
	
	void addVirtualConnection(Player player, VirtualConnection connection) {
		search(player).addVirtualConnection(connection);
	}
	
	void removeVirtualConnection(Player player, VirtualConnection connection) {
		search(player).removeVirtualConnection(connection);
	}
	
	void removeVirtualConnections(Player player, List<VirtualConnection> connections) {
		for(VirtualConnection connection : connections) {
			search(player).removeVirtualConnection(connection);
		}
	}
	
	List<VirtualConnection> getVirtualConnectionsOf(Player player){
		return (search(player).getConnections() == null) ? new ArrayList<>() : search(player).getConnections();
	}

	List<VirtualConnection> getVirtualConnectionsFrom(Player player, Field field, State state){
		List<VirtualConnection> virtualConnections = new ArrayList<>();
		
		for(Field neighbour : Cache.getNeighboursOfLevel(field, 1)) {
			if(state.getFieldAt(neighbour.getPosition()).getColor() == player.getColor()) {
				VirtualConnection virtualConnection = new VirtualConnection(field, neighbour);
				if(virtualConnection.getConnectionsCount() != 0) {
					virtualConnections.add(virtualConnection);
				}
			}
			for(Direction direction : player.getDirections()) {
				if(isOneFieldAwayFromEnd(field, direction)) {
					virtualConnections.add(new VirtualConnection(field, getVirtualField(field, direction)));
				}
			}
		}
		return virtualConnections;
	}
	
	void print() {
		for(Map m : playersVirtualConnection) {
			System.out.println(m);
		}
	}
	
	private Map search(Player player) {
		for(Map map : playersVirtualConnection) {
			if(player.isEquals(map.getPlayer())) {
				return map;
			}
		}
		return null;
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
		return Cache.getNeighboursOfLevelByDirection(direction, selectedField, 1).size() < 3;
	}
	
	private static class Map {
		Player player;
		List<VirtualConnection> connections;
		
		public Map(Player player, List<VirtualConnection> connections) {
			this.player = player;
			this.connections = connections;
		}
		
		public void addVirtualConnection(VirtualConnection connection) {
			if(!connections.contains(connection)) {
				connections.add(connection);
			}
		}
		
		public void removeVirtualConnection(VirtualConnection connection) {
			connections.remove(connection);
		}
		
		public List<VirtualConnection> getConnections(){
			return connections;
		}
		
		public Player getPlayer() {
			return player;
		}
		
		@Override
		public String toString() {
			return "Player: " + player.toString() + ", Connections: " + connections.toString();
		}
	}
}
