package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.Position;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;
import unideb.diploma.strategy.connection.VirtualField;

/**
 * Cache for the virtual connections.
 * */
public class VirtualConnectionCache {
	
	/**
	 * List of maps with players with his virtual connections.
	 * */
	private List<Map> playersVirtualConnection;
	
	/**
	 * Constructor.
	 * */
	VirtualConnectionCache() {
		playersVirtualConnection = new ArrayList<>();
	}
	
	/**
	 * Register a player.
	 * @param player The player.
	 * */
	void registerPlayer(Player player) {
		if(search(player) == null) {
			playersVirtualConnection.add(new Map(player, new ArrayList<>()));
		}
	}
	
	/**
	 * Resetting the map.
	 * */
	void reset() {
		for(Map m : playersVirtualConnection) {
			m.reset();
		}
	}
	
	/**
	 * Adding a virtual connection to the player.
	 * @param player The player.
	 * @param connection The connection which will be added.
	 * */
	void addVirtualConnection(Player player, VirtualConnection connection) {
		search(player).addVirtualConnection(connection);
	}
	
	/**
	 * Removes a virtual connection from the player.
	 * @param player The player.
	 * @param connection The connection which will be removed.
	 * */
	void removeVirtualConnection(Player player, VirtualConnection connection) {
		search(player).removeVirtualConnection(connection);
	}
	
	/**
	 * Removes virtual connection from a player.
	 * @param player The player.
	 * @param connections The connections which will be removed.
	 * */
	void removeVirtualConnections(Player player, List<VirtualConnection> connections) {
		for(VirtualConnection connection : connections) {
			search(player).removeVirtualConnection(connection);
		}
	}
	
	/**
	 * Returns the virtual connections of the player.
	 * @param player The player.
	 * @return The virtual connections.
	 * */
	List<VirtualConnection> getVirtualConnectionsOf(Player player){
		return (search(player).getConnections() == null) ? new ArrayList<>() : search(player).getConnections();
	}

	/**
	 * Gets virtual connections from a field for a player.
	 * @param player The player.
	 * @param field The field.
	 * @param state The state of the game.
	 * @return The virtual connections made from the field.
	 * */
	List<VirtualConnection> getVirtualConnectionsFrom(Player player, Field field, State state){
		List<VirtualConnection> virtualConnections = new ArrayList<>();
		List<Field> reachableFields = state.getReachableFieldsFrom(field, new ArrayList<>(), player.getColor());
		
		for(Field neighbour : Cache.getNeighboursOfDistance(field, 1)) {
			if(!reachableFields.contains(neighbour)) {
				if(neighbour.getColor() == player.getColor()) {
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
		}
		return virtualConnections;
	}
	
	/**
	 * Gets the virtual connection which contains the field from the players virtual connections.
	 * @param player The player.
	 * @param field The field.
	 * @return The virtual conenction.
	 * */
	VirtualConnection getVirtualConnection(Player player, Field field) {
		Map map = search(player);
		VirtualConnection selected = null;
		for(VirtualConnection connection : map.getConnections()) {
			if(connection.getConnections().contains(field)) {
				selected = connection;
			}
		}
		return selected;
	}
	
	/**
	 * Prints the players with their conenctions.
	 * */
	void print() {
		for(Map m : playersVirtualConnection) {
			System.out.println(m);
		}
	}
	
	/**
	 * Search for the map with the player.
	 * @param player The player.
	 * @return The map with the player.
	 * */
	private Map search(Player player) {
		for(Map map : playersVirtualConnection) {
			if(player.isEquals(map.getPlayer())) {
				return map;
			}
		}
		return null;
	}
	
	/**
	 * Gets a virtual field from a selected field.
	 * @param selectedField The selected field.
	 * @param direcetion The direction.
	 * @return The virtual field.
	 * */
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
	
	/**
	 * Checks if a field is one field away from the edge of the table in the direction.
	 * @param selectedField The field.
	 * @param direction The direction.
	 * @return true if the field is one field away from the edge of the table in the direction.
	 * */
	private boolean isOneFieldAwayFromEnd(Field selectedField, Direction direction) {
		return Cache.getNeighboursOfDistanceByDirection(direction, selectedField, 1).getFields().size() < 3;
	}
	
	/**
	 * Static inner class for saving player with his virtual connections.
	 * */
	private static class Map {
		/**
		 * The player.
		 * */
		Player player;
		
		/**
		 * The virtual connections.
		 * */
		List<VirtualConnection> connections;
		
		/**
		 * Constructor.
		 * @param player The player.
		 * @param connections The connections.
		 * */
		public Map(Player player, List<VirtualConnection> connections) {
			this.player = player;
			this.connections = connections;
		}
		
		/**
		 * Adding virtual connection to the player.
		 * @param connection The connection which will be added.
		 * */
		public void addVirtualConnection(VirtualConnection connection) {
			if(!connections.contains(connection)) {
				connections.add(connection);
			}
		}
		
		/**
		 * Removing virtual connection from the player.
		 * @param connection The connection which will be removed.
		 * */
		public void removeVirtualConnection(VirtualConnection connection) {
			connections.remove(connection);
		}
		
		/**
		 * Gets the virtual connections of the player.
		 * @return The virtual connections of the player.
		 * */
		public List<VirtualConnection> getConnections(){
			return connections;
		}
		
		/**
		 * Gets the player.
		 * @return The player.
		 * */
		public Player getPlayer() {
			return player;
		}
		
		/**
		 * Reseting the connections of the player.
		 * */
		public void reset() {
			connections = new ArrayList<>();
		}
		
		@Override
		public String toString() {
			return "Player: " + player.toString() + ", Connections: " + connections.toString();
		}
	}
}
