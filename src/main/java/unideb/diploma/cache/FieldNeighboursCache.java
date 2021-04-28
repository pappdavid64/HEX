package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;

/**
 * Cache for the fields neighbours.
 * */
public class FieldNeighboursCache {
	/**
	 * The field.
	 * */
	private Field field;
	
	/**
	 * The neighbours.
	 * */
	private List<Field> neighbours;

	/**
	 * The north neighbour.
	 * */
	private Field northNeighbour;
	
	/**
	 * The south neighbour.
	 * */
	private Field southNeighbour;
	
	/**
	 * The west neighbour.
	 * */
	private Field westNeighbour;
	
	/**
	 * The east neighbour.
	 * */
	private Field eastNeighbour;
	
	/**
	 * The north-east neighbour.
	 * */
	private Field northEastNeighbour;
	
	/**
	 * The south-west neighbour.
	 * */
	private Field southWestNeighbour;
	
	/**
	 * The list of the field's neighbours cache.
	 * */
	private static List<FieldNeighboursCache> fieldsNeighbours = generateFieldsNeighbours();
	
	/**
	 * Gets the fields neighbours cache for all the fields.
	 * @return The fields neighbours cache for all the field.
	 * */
	static List<FieldNeighboursCache> getFieldsNeighbours(){
		return fieldsNeighbours;
	}

	static void reset(){
		fieldsNeighbours = generateFieldsNeighbours();
	}

	/**
	 * Generating all of the fields neighbours cache.
	 * @return The fields neighbour cache for all field.
	 * */
	private static List<FieldNeighboursCache> generateFieldsNeighbours() {
		List<FieldNeighboursCache> fieldsNeighbours = new ArrayList<>();
		List<Field> allField = Cache.getState().getTable().getFields();
		
		for(Field field : allField) {
			FieldNeighboursCache fnc = new FieldNeighboursCache();
			fnc.setFieldAndAddNeighbours(field);
			fieldsNeighbours.add(fnc);
		}
		
		return fieldsNeighbours;
	}
	
	/**
	 * Gets the neighbours.
	 * @return The neighbours.
	 * */
	List<Field> getNeighbours(){
		return neighbours;
	}
	
	/**
	 * Checks if the field is equal with this field.
	 * @param field The field which will be checked.
	 * @return true if equals with the field.
	 * */
	boolean fieldIsEqual(Field field) {
		return this.field.equals(field);
	}
	
	/**
	 * Private constructor.
	 * */
	private FieldNeighboursCache(){
	}
	
	/**
	 * Sets and add neighbours to field.
	 * @param field The field.
	 * */
	private void setFieldAndAddNeighbours(Field field) {
		this.field = field;
		addNeighbours();
	}
	
	/**
	 * Add neighbours to the field.
	 * */
	private void addNeighbours() {
		neighbours = new ArrayList<>();

		northNeighbour = getFieldByDirection(Direction.NORTH);
		northEastNeighbour = getFieldByDirection(Direction.NORTH_EAST);
		eastNeighbour = getFieldByDirection(Direction.EAST);
		southNeighbour = getFieldByDirection(Direction.SOUTH);
		southWestNeighbour = getFieldByDirection(Direction.SOUTH_WEST);
		westNeighbour = getFieldByDirection(Direction.WEST);
		
		neighbours.addAll(Arrays.asList(northNeighbour, northEastNeighbour, eastNeighbour, southNeighbour, southWestNeighbour, westNeighbour));
		neighbours.removeIf(Objects::isNull);
	}
	
	/**
	 * Gets field by direction.
	 * @param direction The direction.
	 * @return The field by direction.
	 * */
	private Field getFieldByDirection(Direction direction) {
		Field field = null;
		Table table = Cache.getState().getTable();		
		switch (direction) {
		case EAST:
			field = table.getFieldAt(createPositionWithShift(0, 1));
			break;
		case WEST:
			field = table.getFieldAt(createPositionWithShift(0, -1));
			break;
		case SOUTH:
			field = table.getFieldAt(createPositionWithShift(1, 0));
			break;
		case NORTH:
			field = table.getFieldAt(createPositionWithShift(-1, 0));
			break;
		case NORTH_EAST:
			field = table.getFieldAt(createPositionWithShift(-1, 1));
			break;
		case SOUTH_WEST:
			field = table.getFieldAt(createPositionWithShift(1, -1));
			break;
			
		default:
			break;
		}
		
		return field;
	}
	
	/**
	 * Creates position with shift.
	 * @param xShift The offset of x.
	 * @param yShift The offset of y.
	 * @return The position with shift.
	 * */
	private Position createPositionWithShift(int xShift, int yShift) {
		Position position = field.getPosition();
		return new Position(position.getX() + xShift, position.getY() + yShift);
	}

	/**
	 * Gets the north neighbour.
	 * @return The north neighbour.
	 * */
	Field getNorthNeighbour() {
		return northNeighbour;
	}

	/**
	 * Gets the south neighbour.
	 * @return The south neighbour.
	 * */
	Field getSouthNeighbour() {
		return southNeighbour;
	}


	/**
	 * Gets the west neighbour.
	 * @return The west neighbour.
	 * */
	Field getWestNeighbour() {
		return westNeighbour;
	}

	/**
	 * Gets the east neighbour.
	 * @return The east neighbour.
	 * */
	Field getEastNeighbour() {
		return eastNeighbour;
	}

	/**
	 * Gets the north-east neighbour.
	 * @return The north-east neighbour.
	 * */
	Field getNorthEastNeighbour() {
		return northEastNeighbour;
	}

	/**
	 * Gets the south-west neighbour.
	 * @return The south-west neighbour.
	 * */
	Field getSouthWestNeighbour() {
		return southWestNeighbour;
	}
	
	/**
	 * Gets the neighbours of the field which distance is equals to  the distance.
	 * @param distance The distance.
	 * @return The neighbours.
	 * */
	List<Field> getNeighboursOfDistance(int distance){
		List<Field> neighbours = Cache.getNeighbours(field).getFields();
		for(int i = 0; i < distance; i++) {
			List<Field> nextLevelNeighbours = new ArrayList<>();
			for(Field neighbour : neighbours) {
				List<Field> act = Cache.getNeighbours(neighbour).getFields();
				nextLevelNeighbours.addAll(act);
			}
			nextLevelNeighbours.removeAll(neighbours);
			neighbours = new ArrayList<>( new HashSet<>(nextLevelNeighbours));
		}
		neighbours = new ArrayList<>( new HashSet<>(neighbours));
		neighbours.remove(field);
		return neighbours;
	}
	
	/**
	 * Gets the neighbours of the field in direction which distance is equals to  the distance.
	 * @param direction The direction.
	 * @param distance The distance.
	 * @return The neighbours.
	 * */
	List<Field> getNeighboursOfDistanceByDirection(Direction direction, int distance){
		List<Field> neighbours = Cache.getNeighboursByDirection(direction, field).getFields();
		for(int i = 0; i < distance; i++) {
			List<Field> nextLevelNeighbours = new ArrayList<>();
			for(Field neighbour : neighbours) {
				List<Field> act = Cache.getNeighboursByDirection(direction,neighbour).getFields();
				nextLevelNeighbours.addAll(act);
			}
			nextLevelNeighbours.removeAll(neighbours);
			neighbours = new ArrayList<>( new HashSet<>(nextLevelNeighbours));
		}
		neighbours = new ArrayList<>( new HashSet<>(neighbours));
		neighbours.remove(field);
		return neighbours;
	}
	
}
