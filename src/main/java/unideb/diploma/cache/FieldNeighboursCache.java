package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import unideb.diploma.domain.Field;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;

public class FieldNeighboursCache {
	private Field field;
	private List<Field> neighbours;
	private Field northNeighbour;
	private Field southNeighbour;
	private Field westNeighbour;
	private Field eastNeighbour;
	private Field northEastNeighbour;
	private Field southWestNeighbour;
	private static List<FieldNeighboursCache> fieldsNeighbours = generateFieldsNeighbours();
	
	static List<FieldNeighboursCache> getFieldsNeighbours(){
		return fieldsNeighbours;
	}
	
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
	
	List<Field> getNeighbours(){
		return neighbours;
	}
	
	boolean fieldIsEqual(Field field) {
		return this.field.equals(field);
	}
	
	private FieldNeighboursCache(){
	}
	
	private void setFieldAndAddNeighbours(Field field) {
		this.field = field;
		addNeighbours();
	}
	
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
	
	private Position createPositionWithShift(int xShift, int yShift) {
		Position position = field.getPosition();
		return new Position(position.getX() + xShift, position.getY() + yShift);
	}

	Field getNorthNeighbour() {
		return northNeighbour;
	}

	Field getSouthNeighbour() {
		return southNeighbour;
	}

	Field getWestNeighbour() {
		return westNeighbour;
	}

	Field getEastNeighbour() {
		return eastNeighbour;
	}

	Field getNorthEastNeighbour() {
		return northEastNeighbour;
	}

	Field getSouthWestNeighbour() {
		return southWestNeighbour;
	}
	
	List<Field> getNeighboursOfLevel(int level){
		List<Field> neighbours = Cache.getNeighbours(field).getFields();
		for(int i = 0; i < level; i++) {
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
	
	List<Field> getNeighboursOfLevelByDirection(Direction direction, int level){
		List<Field> neighbours = Cache.getNeighboursByDirection(direction, field).getFields();
		for(int i = 0; i < level; i++) {
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
