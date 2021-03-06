package unideb.diploma.game;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;
import unideb.diploma.util.FieldList;

/**
 * Represents the state of the game.
 * */
public class State {
	/**
	 * The table.
	 * */
	private Table table;
	
	/**
	 * The color of the player whose turn is.
	 * */
	private FieldColor color;

	/**
	 * Constructor.
	 * @param table The table of the game.
	 * @param color The color of the player whose turn is.
	 * */
	public State(Table table, FieldColor color) {
		this.table = table;
		this.color = color;
	}
	

	/**
	 * Constructor.
	 * @param table The table of the game.
	 * */
	public State(Table table) {
		this.table = table;
	}
	
	/**
	 * Gets the table.
	 * @return The table.
	 * */
	public Table getTable() {
		return this.table;
	}
	
	/**
	 * Gets the color.
	 * @return The color.
	 * */
	public FieldColor getColor() {
		return color;
	}
	
	/**
	 * Applies the operator.
	 * If useable on the table, then use it.
	 * @param operator The operator.
	 * */
	public void applyOperator(Operator operator) {
		if(operator.isUsableOn(table)) {
			operator.use(table, color);
			color = (color == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE;
		}
	}
	
	/**
	 * Gets the field at the position.
	 * @param position The position.
	 * @return The field at the position.
	 * */
	public Field getFieldAt(Position position) {
		return table.getFieldAt(position);
	}
	
	/**
	 * Gets the field at the position(x,y).
	 * @param x The row.
	 * @param y The column.
	 * @return The field at the position(x,y).
	 * */
	public Field getFieldAt(int x, int y) {
		return table.getFieldAt(new Position(x, y));
	}
	
	/**
	 * Checks if the state is end state.
	 * @return true if the state is end state.
	 * */
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
	
	/**
	 * Checks if the state is end state for the player.
	 * @param lastPlayer The player.
	 * @return true if the state is end state.
	 * */
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
	
	/**
	 * Checks if can reach the end from a position(x,y).
	 * @param x The row.
	 * @param y The column.
	 * @param playerColor The color for check.
	 * @param checkedIndexes The indexes which already checked.
	 * @return true if can reach the end from the position.
	 * */
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
	
	/**
	 * Gets the longest path counter from a position(x,y).
	 * @param x The row.
	 * @param y The column.
	 * @param playerColor The color for check.
	 * @param checkedIndexes The indexes which already checked.
	 * @return The longest path counter.
	 * */
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
	
	/**
	 * Gets the longest path length for the color.
	 * @param playerColor The color for check.
	 * @return The longest path length for the color.
	 * */
	public int getLongestPathLength(FieldColor playerColor) {
		int longestPathLength = Integer.MIN_VALUE;
		FieldList list = new FieldList(table.getFields());
		for(Field field : list.withColor(playerColor)) {
			int actualLength = getLongestPathLength(field.getX(), field.getY(), playerColor, new ArrayList<>());
			if(actualLength > longestPathLength) {
				longestPathLength = actualLength;
			}
		}
		
		return longestPathLength;
	}
	
	/**
	 * Gets the path length.
	 * @param playerColor The color of the checked fields.
	 * @return The sum of the path length.
	 * */
	public int getPathLength(FieldColor playerColor) {
		int sum = 0;
		for(Field field : table.getFields()) {
			if(field.getColor() == playerColor) {
				sum += getLongestPathLength(field.getX(), field.getY(), playerColor, new ArrayList<>()) - 1;
			}
		}
		return sum;
	}
	
	/**
	 * Clones the state.
	 * @return Copy of the state.
	 * */
	public State clone() {
		return new State(table.clone(), color);
	}

	/**
	 * Gets the reachable fields from a field.
	 * @param field The field.
	 * @return The reachable fields from the field.
	 * */
	public List<Field> getReachableFieldsFrom(Field field) {
		return getReachableFieldsFrom(field, new ArrayList<>(), color);
	}


	/**
	 * Gets the reachable fields from a field.
	 * @param field The field.
	 * @param reachableFields The fields already reached.
	 * @param color The color for check.
	 * @return The reachable fields from the field.
	 * */
	public List<Field> getReachableFieldsFrom(Field field, List<Field> reachableFields, FieldColor color) {
		for(Field neighbour : Cache.getNeighbours(field)) {
			if(neighbour.getColor() == color && !reachableFields.contains(neighbour)) {
				reachableFields.add(neighbour);
				getReachableFieldsFrom(neighbour, reachableFields, color);
			}
		}
		return reachableFields;
	}
	
}
