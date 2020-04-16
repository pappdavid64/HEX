package unideb.diploma.cache;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.App;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;

public class OperatorCache {
	private List<Operator> allOperator;
	private List<Operator> useableOperators;
	
	public OperatorCache() {
		initAllOperator();
		useableOperators = new ArrayList<>(allOperator);
	}
	
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
	
	private boolean isValidPosition(Position position) {
		boolean xIsValid = (position.getX() >= 0) && (position.getX() < App.BOARD_SIZE);
		boolean yIsValid = (position.getY() >= 0) && (position.getY() < App.BOARD_SIZE);
		return xIsValid && yIsValid;
	}
	
	List<Operator> getUseableOperators() {
		return new ArrayList<>(useableOperators);
	}
	
	void removeOperatorFromUseableOperators(Operator operator) {
		useableOperators.remove(operator);
	}
	
	void fillUseableOperators() {
		useableOperators = new ArrayList<Operator>(allOperator);
	}
	
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
