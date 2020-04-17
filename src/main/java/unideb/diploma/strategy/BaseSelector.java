package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.cache.Direction;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

public class BaseSelector {
	private Field base;
	private Player player;
	
	public BaseSelector(Player player) {
		this.player = player;
	}
	
	public BaseSelector selectBaseByFieldValue(State state) {
		
		for(Field field : state.getTable().getFields()) {
			if(field.getColor() == player.getColor()) {
				base = (base == null) ? field : base;
				if(base.getGoodness() < field.getGoodness()) {
					base = field;
				}
			}
		}
		return this;
	}
	
	public BaseSelector selectBaseByBridge(State state) {
		Map<Field, Integer> numberOfReachableFields = new HashMap<>();
		for(Field field : state.getTable().getFields()) {
			if(field.getColor() == player.getColor()) {
				if(canReachTheEnd(state, field)) {
					numberOfReachableFields.put(field, getReachableFieldsFromField(field, new ArrayList<>()).size());
				}
			}
		}
		int max = 0;
		for(Field field : numberOfReachableFields.keySet()) {
			System.out.println("field: " + field);
			System.out.println("reachable field number: " + numberOfReachableFields.get(field));
			if(max < numberOfReachableFields.get(field)) {
				base = field;
				max = numberOfReachableFields.get(field);
			}
		}
		return this;
	}

	
	public BaseSelector selectBaseFromWhiteFields(State state) {
		Map<Field, Integer> goodnessOfField = new HashMap<>();
		List<Field> whiteFields = new ArrayList<>();
		for(Field field : state.getTable().getFields()) {
			if(field.getColor() == FieldColor.WHITE) {
				goodnessOfField.put(field, field.getGoodness());
				whiteFields.add(field);
			}
		}
		for(Field field : whiteFields) {
			if(canReachTheEnd(state, field)) {
				int goodness = goodnessOfField.get(field) * 2;
				goodnessOfField.put(field, goodness);
			}
		}
		int max = 0;
		for(Field field : goodnessOfField.keySet()) {
			if(max < goodnessOfField.get(field)) {
				max = goodnessOfField.get(field);
				base = field;
			}
		}
		return this;
	}
	
	public boolean canReachTheEndFromBase(State state) {
		if(base == null) {
			return false;
		}
		return canReachTheEnd(state, base);
	}
	
	public Field getBase() {
		return base;
	}
	
	private boolean canReachTheEnd(State state, Field field) {
		for(Direction direction : player.getDirections()) {
			boolean canReachTheEnd = canReachTheEndInDirection(state, field, direction);
			if(!canReachTheEnd) {
				return false;
			}
		}
		
		return true;
	}

	private boolean canReachTheEndInDirection(State state, Field actual, Direction direction) {
		for(Field field : Cache.getNeighboursByDirection(direction, actual)) {
			if(field.getX() == 0 || field.getX() == App.BOARD_SIZE - 1) {
				return true;
			}
			if(field.getY() == 0 || field.getY() == App.BOARD_SIZE - 1) {
				return true;
			}
			FieldColor color = state.getFieldAt(field.getPosition()).getColor();
			if(color == FieldColor.WHITE || color == player.getColor()) {
				return canReachTheEndInDirection(state, field, direction);
			}
		}
		return false;
	}
	
	private List<Field> getReachableFieldsFromField(Field field, List<Field> reachableFields) {
		for(Direction direction : player.getDirections()) {
			for(Field actual : Cache.getNeighboursByDirection(direction, field)) {
				if( (actual.getColor() == player.getColor() || actual.getColor() == FieldColor.WHITE) && !reachableFields.contains(actual)) {
					reachableFields.add(actual);
					return getReachableFieldsFromField(actual, reachableFields);
				}
			}
		}
		return reachableFields;
	}
	
}
