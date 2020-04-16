package unideb.diploma.game;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
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
//		FieldColor lastPlayer = (color == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE;
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
	
	public State clone() {
		return new State(table.clone(), color);
	}
}
