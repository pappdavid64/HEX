package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;

/**
 * Cache for the operators.
 * */
public class OperatorCache {
	/**
	 * All operators.
	 * */
	private List<Operator> allOperator;
	
	/**
	 * Useable operators.
	 * */
	private List<Operator> useableOperators;
	
	/**
	 * Constructor.
	 * */
	public OperatorCache() {
		initAllOperator();
		useableOperators = new ArrayList<>(allOperator);
	}
	
	/**
	 * Initialize all operators.
	 * */
	private void initAllOperator() {
		allOperator = new ArrayList<>();
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			for(int j = 0; j < App.BOARD_SIZE; j++) {
				Position position = new Position(i,j);
				Operator operator = new Operator(position);
				allOperator.add(operator);
			}
		}
	}
	
	/**
	 * Checks if a position is valid.
	 * @param position The position.
	 * @return true if the position is valid.
	 * */
	private boolean isValidPosition(Position position) {
		boolean xIsValid = (position.getX() >= 0) && (position.getX() < App.BOARD_SIZE);
		boolean yIsValid = (position.getY() >= 0) && (position.getY() < App.BOARD_SIZE);
		return xIsValid && yIsValid;
	}
	
	/**
	 * Gets the useable operators.
	 * @return The useable operators.
	 * */
	List<Operator> getUseableOperators() {
		return new ArrayList<>(useableOperators);
	}
	
	/**
	 * Removes the operator from useable operators.
	 * @param operator The operator which will be removed.
	 * */
	void removeOperatorFromUseableOperators(Operator operator) {
		useableOperators.remove(operator);
	}
	
	/**
	 * Reseting the useable operators.
	 * */
	void fillUseableOperators() {
		useableOperators = new ArrayList<Operator>(allOperator);
	}
	
	/**
	 * Gets the operator at position.
	 * @param position The position.
	 * @return The operator at the position.
	 * */
	Operator getOperatorAt(Position position) {
		if(!isValidPosition(position)) {
			throw new IllegalArgumentException("There is no such operator!");
		}
		for(Operator op : useableOperators) {
			if(op.getPosition().equals(position)) {
				return op;
			}
		}
		return null;
	}
	
}
