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

public class VirtualConnection {

	private List<Field> connections;
	
	public VirtualConnection(Field a, Field b) {
		if(distanceIsOneField(a, b)) {
			connections = getConnectionsBetween(a,b);
		} else {
			connections = new ArrayList<>();
		}
	}
	
	public VirtualConnection(Field real, VirtualField virtual) {
		List<Field> neighboursOfReal = Cache.getNeighbours(real);
		List<Field> neighboursOfVirtual = getNeighboursOfVirtualField(virtual);
		
		connections = new ArrayList<>();
		
		for(Field neighbour : neighboursOfReal) {
			if(neighboursOfVirtual.contains(neighbour)) {
				connections.add(neighbour);
			}
		}
	}
	
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
	
	private Field getFieldByDirection(Direction direction, Position position) {
		Field field = null;
		Table table = Cache.getBaseState().getTable();		
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
	
	private Position createPositionWithShift(Position position, int xShift, int yShift) {
		return new Position(position.getX() + xShift, position.getY() + yShift);
	}
	
	public List<Field> getConnections(){
		return connections;
	}
	
	public int getConnectionsCount() {
		return connections.size();
	}
	
	private boolean distanceIsOneField(Field a, Field b) {
		return Cache.getNeighboursOfLevel(a, 1).contains(b);
	}
	
	private List<Field> getConnectionsBetween(Field a, Field b) {
		List<Field> neighbours = Cache.getNeighbours(a);
		List<Field> connections = new ArrayList<>();
		for(Field neighbour : neighbours) {
			if(reachableFrom(neighbour, b)) {
				connections.add(neighbour);
			}
		}
		removeNotWhiteFields(connections);
		return connections;
	}
	
	private boolean reachableFrom(Field a, Field b) {
		boolean reachable = false;
		List<Field> neighbours = Cache.getNeighbours(a);
		for(Field neighbour : neighbours) {
			if(neighbour.equals(b)) {
				reachable = true;
			}
		}
		return reachable;
	}


	private void removeNotWhiteFields(List<Field> neighbours) {
		List<Field> forRemove = new ArrayList<>();
		neighbours.forEach((neighbour) -> {
			if(neighbour.getColor() != FieldColor.WHITE) {
				forRemove.add(neighbour);
			}
		});
		neighbours.removeAll(forRemove);
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
