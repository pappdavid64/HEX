package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;

public class FieldValueStrategy implements Strategy{

	private boolean active = true;
	
	@Override
	public Operator getNextMove(State state) {
		Field fieldByBestValue = getBestValueOfFields(state);
		Operator nextOperator = Cache.getOperatorAt(fieldByBestValue.getX(), fieldByBestValue.getY());
		return nextOperator;
	}

	@Override
	public int getGoodnessByState(State state) {
		return getBestValueOfFields(state).getGoodness();
	}
	
	private Field getBestValueOfFields(State state) {
		List<Field> bestValuedFields = new ArrayList<>();
		int goodness = Integer.MIN_VALUE;
		for(Field field : state.getTable().getFields()) {
			int goodnessOfField = (field.getColor() == FieldColor.WHITE) ? field.getGoodness() : Integer.MIN_VALUE;
			if(goodness < goodnessOfField) {
				bestValuedFields.clear();
				goodness = goodnessOfField;
			}
			if(goodness == goodnessOfField) {
				bestValuedFields.add(field);
			}
		}
		Random rand = new Random();
		return bestValuedFields.get(rand.nextInt(bestValuedFields.size()));
	}
	
	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void deActivate() {
		active = false;
	}

	@Override
	public void activate() {
		active = true;		
	}
}