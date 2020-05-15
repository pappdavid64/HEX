package unideb.diploma.strategy.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import unideb.diploma.cache.Cache;
import unideb.diploma.cache.Direction;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;

/**
 * Virtual connection between two field.
 * They can be connected by one move. 
 * */
public class VirtualConnection {

	/**
	 * Connections between the two field.
	 * 
	 * */
	private List<Field> connections;
	
	/**
	 * First field.
	 * */
	private Field a;
	
	/**
	 * Second field.
	 * If the virtual connection has virtual field then this field is null.
	 * */
	private Field b;
	
	/**
	 * The virtual field of the virtual connection.
	 * Only exists if the connections is at the edge of the table.
	 * */
	private VirtualField virtual;
	
	/**
	 * Indicates if the virtual connection has a virtual field.
	 * */
	private boolean hasVirtualField;
	
	/**
	 * Constructor.
	 * @param a Field a.
	 * @param b Field b.
	 * */
	public VirtualConnection(Field a, Field b) {
		this.a = a;
		this.b = b;
		hasVirtualField = false;
		if(distanceIsOneField(a, b)) {
			connections = getConnectionsBetween(a,b);
		} else {
			connections = new ArrayList<>();
		}
	}
	
	/**
	 * Constructor.
	 * @param real The real field.
	 * @param virtual The virtual field.
	 * */
	public VirtualConnection(Field real, VirtualField virtual) {
		this.a = real;
		this.virtual = virtual;
		hasVirtualField = true;
		List<Field> neighboursOfReal = Cache.getNeighbours(real).getFields();
		List<Field> neighboursOfVirtual = getNeighboursOfVirtualField(virtual);
		
		connections = new ArrayList<>();
		
		for(Field neighbour : neighboursOfReal) {
			if(neighboursOfVirtual.contains(neighbour)) {
				connections.add(neighbour);
			}
		}
	}
	
	/**
	 * Gets the neighbours of a virtual field.
	 * @param virtual The virtual field.
	 * @return The list of neighbours.
	 * */
	private List<Field> getNeighboursOfVirtualField(VirtualField virtual) {
		List<Field> neighbours = new ArrayList<>();

		neighbours.add(getFieldByDirection(Direction.NORTH, virtual.getPosition()));
		neighbours.add(getFieldByDirection(Direction.NORTH_EAST, virtual.getPosition()));
		neighbours.add(getFieldByDirection(Direction.EAST, virtual.getPosition()));
		neighbours.add(getFieldByDirection(Direction.SOUTH, virtual.getPosition()));
		neighbours.add(getFieldByDirection(Direction.SOUTH_WEST, virtual.getPosition()));
		neighbours.add(getFieldByDirection(Direction.WEST, virtual.getPosition()));
		
		neighbours.removeIf(Objects::isNull);
		removeNotWhiteFields(neighbours);
		return neighbours;
	}
	
	/**
	 * Gets the field by direction.
	 * @param direction The direction.
	 * @param position The position of the virtual field.
	 * @return The field if it exists. Returns null otherwise.
	 * */
	private Field getFieldByDirection(Direction direction, Position position) {
		Field field = null;
		Table table = Cache.getState().getTable();		
		switch (direction) {
		case EAST:
			field = table.getFieldAt(createPositionWithShift(position, 0, 1));
			break;
		case WEST:
			field = table.getFieldAt(createPositionWithShift(position, 0, -1));
			break;
		case SOUTH:
			field = table.getFieldAt(createPositionWithShift(position, 1, 0));
			break;
		case NORTH:
			field = table.getFieldAt(createPositionWithShift(position, -1, 0));
			break;
		case NORTH_EAST:
			field = table.getFieldAt(createPositionWithShift(position, -1, 1));
			break;
		case SOUTH_WEST:
			field = table.getFieldAt(createPositionWithShift(position, 1, -1));
			break;
			
		default:
			break;
		}
		
		return field;
	}
	
	/**
	 * Creates a position with shift.
	 * @param position The base position.
	 * @param xShift The offset of x.
	 * @param yShift The offset of y.
	 * @return The new position.
	 * */
	private Position createPositionWithShift(Position position, int xShift, int yShift) {
		return new Position(position.getX() + xShift, position.getY() + yShift);
	}
	
	/**
	 * Check if the given two field is the fields in the virtual connection.
	 * @param a The first field given.
	 * @param b The second field given.
	 * @return true if it is that connection.
	 * */
	public boolean isThatConnection(Field a, Field b) {
		if(hasVirtualField) {
			return this.a.equals(a) || this.a.equals(b);
		}
		return (this.a.equals(a) && this.b.equals(b)) || (this.b.equals(a) && this.a.equals(b));
	}
	
	/**
	 * Gets the connections.
	 * @return The connections.
	 * */
	public List<Field> getConnections(){
		return connections;
	}
	
	/**
	 * Gets the number of connections.
	 * @return The number of connections.
	 * */
	public int getConnectionsCount() {
		return connections.size();
	}
	
	/**
	 * Gets a.
	 * @return a.
	 * */
	public Field getA() {
		return a;
	}
	
	/**
	 * Gets b.
	 * @return b.
	 * */
	public Field getB() {
		return b;
	}
	
	/**
	 * Gets the virtual field.
	 * @return The virtual field.
	 * */
	public VirtualField getVirtual() {
		return virtual;
	}
	
	/**
	 * Returns true if it has a virtual field.
	 * @return true if it has a virtual field.
	 * */
	public boolean hasVirtualField() {
		return hasVirtualField;
	}
	
	/**
	 * Checks if the distance is one between the two field.
	 * @param a First field.
	 * @param b Second field.
	 * @return true if the distance is one.
	 * */
	private boolean distanceIsOneField(Field a, Field b) {
		return Cache.getNeighboursOfDistance(a, 1).getFields().contains(b);
	}
	
	/**
	 * Gets the connections between the two field.
	 * @param a First field.
	 * @param b Second field.
	 * @return The list of fields between the two field which connecting them.
	 * */
	private List<Field> getConnectionsBetween(Field a, Field b) {
		List<Field> neighbours = Cache.getNeighbours(a).getFields();
		List<Field> connections = new ArrayList<>();
		for(Field neighbour : neighbours) {
			if(reachableFrom(neighbour, b)) {
				connections.add(neighbour);
			}
		}
	
		removeNotWhiteFields(connections);
		
		return connections;
	}
	
	/**
	 * Checks if the two field are neighbours.
	 * @param a First field.
	 * @param b Second field.
	 * @return true if they are neighbours.
	 * */
	private boolean reachableFrom(Field a, Field b) {
		boolean reachable = false;
		List<Field> neighbours = Cache.getNeighbours(a).getFields();
		for(Field neighbour : neighbours) {
			if(neighbour.equals(b)) {
				reachable = true;
			}
		}
		return reachable;
	}

	/**
	 * Remove not white fields from a list.
	 * @param fields The list.
	 * */
	private void removeNotWhiteFields(List<Field> fields) {
		List<Field> forRemove = new ArrayList<>();
		fields.forEach((neighbour) -> {
			if(neighbour.getColor() != FieldColor.WHITE) {
				forRemove.add(neighbour);
			}
		});
		fields.removeAll(forRemove);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(connections);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof VirtualConnection)) {
			return false;
		}
		VirtualConnection other = (VirtualConnection) obj;
		return Objects.equals(connections, other.connections);
	}

	@Override
	public String toString() {
		return "VirtualConnection [connections=" + connections + "]";
	}
	
	
}
