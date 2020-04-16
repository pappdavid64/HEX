package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;
import unideb.diploma.game.State;

public class StateCache {
	private static State baseState;
	private List<State> states;
	
	
	
	StateCache(){
		states = new ArrayList<>();
	}
	
	void addStateToCache(State state) {
		states.add(state);
	}
	
	void removeStateFromCache(State state) {
		states.remove(state);
	}
	
	void clearStates() {
		states = new ArrayList<>();
	}
	
	List<State> getStates() {
		return states;
	}
	
	State getBaseState() {
		if(baseState == null) {
			baseState = createBaseState();
		}
		return baseState;
	}
	
	void resetBaseState() {
		baseState = createBaseState();
	}
	
	private State createBaseState() {
		List<Field> fields = new ArrayList<>();
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			for(int j = 0; j < App.BOARD_SIZE; j++) {
				Position position = new Position(i,j);
				FieldColor color = FieldColor.WHITE;
				Field field = new Field(position, color);
				fields.add(field);
			}
		}
		return new State(new Table(fields), FieldColor.BLUE);
	}
}
