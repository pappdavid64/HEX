package unideb.diploma.game;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;

public class State {
	private Table table;
	private FieldColor color;

	public State(Table table, FieldColor color) {
		this.table = table;
		this.color = color;
	}
	
	public State(Table table) {
		this.table = table;
	}
	
	public Table getTable() {
		return this.table;
	}
	
	public FieldColor getColor() {
		return color;
	}
	
	public void applyOperator(Operator operator) {
		if(operator.isUsableOn(table)) {
			operator.use(table, color);
			color = (color == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE;
		}
	}
	
	public Field getFieldAt(Position position) {
		return table.getFieldAt(position);
	}
	
	public Field getFieldAt(int x, int y) {
		return table.getFieldAt(new Position(x, y));
	}
	
	public boolean isEndState() {
		List<Position> checkedIndexes = new ArrayList<>();
		FieldColor lastPlayer = (color == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE;
		boolean hasPath = false;
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			if(lastPlayer == FieldColor.BLUE) {
				hasPath = hasPath(i, 0, FieldColor.BLUE, checkedIndexes);
			} else {
				hasPath = hasPath(0, i, FieldColor.RED, checkedIndexes);
			}
			if(hasPath) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEndState(FieldColor lastPlayer) {
		List<Position> checkedIndexes = new ArrayList<>();
		boolean hasPath = false;
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			if(lastPlayer == FieldColor.BLUE) {
				hasPath = hasPath(i, 0, FieldColor.BLUE, checkedIndexes);
			} else {
				hasPath = hasPath(0, i, FieldColor.RED, checkedIndexes);
			}
			if(hasPath) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasPath(int x, int y, FieldColor playerColor, List<Position> checkedIndexes) {
		
		//Check for invalid row and column
		
		if(x < 0 || x > App.BOARD_SIZE - 1) {
			return false;
		}
		if(y < 0 || y > App.BOARD_SIZE - 1) {
			return false;
		}
		
		Position index = new Position(x, y);
		FieldColor fieldColor = table.getFieldAt(index).getColor();
		
		//This check has to be here to ensure that the following two work properly 
		
		if(fieldColor != playerColor) {
			checkedIndexes.add(index);
			return false;
		}
		
		if(playerColor == FieldColor.BLUE && y == App.BOARD_SIZE - 1){
			return true;
		}
		
		if(playerColor == FieldColor.RED && x == App.BOARD_SIZE - 1){
			return true;
		}
		
		if(checkedIndexes.contains(index)) {
			return false;
		}
		checkedIndexes.add(index);
		return (
				hasPath(x-1, y, playerColor, checkedIndexes) ||
				hasPath(x-1, y+1, playerColor, checkedIndexes) ||
				hasPath(x, y+1, playerColor, checkedIndexes) ||
				hasPath(x+1, y, playerColor, checkedIndexes) ||
				hasPath(x+1, y-1, playerColor, checkedIndexes) ||
				hasPath(x, y-1, playerColor, checkedIndexes)
				);
	}
	
	public int getLongestPathLength(int x, int y, FieldColor playerColor, List<Position> checkedIndexes) {
		if(x < 0 || x > App.BOARD_SIZE - 1) {
			return 0;
		}
		if(y < 0 || y > App.BOARD_SIZE - 1) {
			return 0;
		}
		Position index = new Position(x, y);
		FieldColor fieldColor = table.getFieldAt(index).getColor();
		
		//This check has to be here to ensure that the following two work properly 
		
		if(fieldColor != playerColor) {
			checkedIndexes.add(index);
			return 0;
		}
		
		if(checkedIndexes.contains(index)) {
			return 0;
		}
		
		int plus = 1;
		for(Position position : checkedIndexes) {
			if(playerColor == FieldColor.BLUE && table.getFieldAt(position).getColor() == FieldColor.BLUE) {
				if(position.getY() == index.getY()) {
					plus = 0;
				}
			} 
			if(playerColor == FieldColor.RED && table.getFieldAt(position).getColor() == FieldColor.RED) {
				if(position.getX() == index.getX()) {
					plus = 0;
				}
			}
		}
		
		checkedIndexes.add(index);
		
		if(playerColor == fieldColor){
			return 	(
					getLongestPathLength(x-1, y, playerColor, checkedIndexes) +
					getLongestPathLength(x-1, y+1, playerColor, checkedIndexes) +
					getLongestPathLength(x, y+1, playerColor, checkedIndexes) +
					getLongestPathLength(x+1, y, playerColor, checkedIndexes) +
					getLongestPathLength(x+1, y-1, playerColor, checkedIndexes) +
					getLongestPathLength(x, y-1, playerColor, checkedIndexes)
					) + plus;
		}
		
		return 0;
	}
	
	public int getLongestPathLength(FieldColor playerColor) {
		int longestPathLength = Integer.MIN_VALUE;
		
		for(Field field : Cache.withColor(table.getFields(), playerColor)) {
			int actualLength = getLongestPathLength(field.getX(), field.getY(), playerColor, new ArrayList<>());
			if(actualLength > longestPathLength) {
				longestPathLength = actualLength;
			}
		}
		
		return longestPathLength;
	}
	
	public int getPathLength(FieldColor playerColor) {
		int sum = 0;
		for(Field field : table.getFields()) {
			if(field.getColor() == playerColor) {
				sum += getLongestPathLength(field.getX(), field.getY(), playerColor, new ArrayList<>()) - 1;
			}
		}
		return sum;
	}
	
	
	public State clone() {
		return new State(table.clone(), color);
	}

	public List<Field> getReachableFieldsFrom(Field field, List<Field> reachableFields) {
		List<Field> neighbours = Cache.getNeighbours(field);
		for(Field neighbour : neighbours) {
			if(neighbour.getColor() == color && !reachableFields.contains(neighbour)) {
				reachableFields.add(neighbour);
				getReachableFieldsFrom(neighbour, reachableFields);
			}
		}
		return reachableFields;
	}
	
}
