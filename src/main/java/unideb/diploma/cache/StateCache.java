package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.domain.Table;
import unideb.diploma.game.State;

/**
 * Cache for the states.
 * */
public class StateCache {
	/**
	 * Actual state.
	 * */
	private static State state;
	
	/**
	 * Saved states.
	 * */
	private List<State> states;
	
	
	
	StateCache(){
		states = new ArrayList<>();
	}
	
	/**
	 * Adding a state to the state cache.
	 * @param state The state which will be added.
	 * */
	void addStateToCache(State state) {
		states.add(state);
	}
	
	/**
	 * Removing a state.
	 * @param state The state which will be removed.
	 * */
	void removeStateFromCache(State state) {
		states.remove(state);
	}
	
	/**
	 * Reseting the states of the state cache.
	 * */
	void clearStates() {
		states = new ArrayList<>();
	}
	
	/**
	 * Gets the states.
	 * @return The states.
	 * */
	List<State> getStates() {
		return states;
	}
	
	/**
	 * Gets the actual state.
	 * */
	State getState() {
		if(state == null) {
			state = createState();
		}
		return state;
	}
	
	/**
	 * Resetting the actual state.
	 * */
	void resetState() {
		state = createState();
	}
	
	/**
	 * Creates the base state.
	 * @return The base state.
	 * */
	private State createState() {
		List<Field> fields = new ArrayList<>();
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			for(int j = 0; j < App.BOARD_SIZE; j++) {
				Position position = new Position(i,j);
				FieldColor color = FieldColor.WHITE;
				Field field = new Field(position, color);
				fields.add(field);
			}
		}
		State statee = new State(new Table(fields), FieldColor.BLUE);
		System.out.println(statee);
		return new State(new Table(fields), FieldColor.BLUE);
	}
}
