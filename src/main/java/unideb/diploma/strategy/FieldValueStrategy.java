package unideb.diploma.strategy;

import java.util.*;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.observer.Observable;
import unideb.diploma.observer.Observer;
import unideb.diploma.strategy.strength.StrategyStrength;

public class FieldValueStrategy implements Strategy, Observer {

	private Map<Field, Integer> fieldsAndValues;
	private Player player;

	public FieldValueStrategy(Player player) {
		this.player = player;
		fieldsAndValues = getBaseValues();
	}

	private Map<Field, Integer> getBaseValues() {
		Map<Field, Integer> baseFieldsAndValues = new HashMap<>();

		for(Field field : Cache.getState().getTable().getFields()){
			baseFieldsAndValues.put(field, field.getGoodness());
			field.addObserver(this);
		}

		return baseFieldsAndValues;
	}

	@Override
	public Operator getNextMove(State state) {
		Field fieldByBestValue = getBestValueOfFields(state);
		Operator nextOperator = Cache.getOperatorAt(fieldByBestValue.getX(), fieldByBestValue.getY());
		return nextOperator;
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		return StrategyStrength.weak(getBestValueOfFields(state).getGoodness());
	}
	
	public Field getBestValueOfFields(State state) {
		List<Field> bestValuedFields = new ArrayList<>();
		int goodness = Integer.MIN_VALUE;
		for(Field field : fieldsAndValues.keySet()) {
			int goodnessOfField = (field.getColor() == FieldColor.WHITE) ? fieldsAndValues.get(field) : Integer.MIN_VALUE;
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
	public void notify(Observable observable) {
		Field field = (Field)observable;
		//int plus = (field.getColor() == player.getColor()) ? -10 : 10;
		//for(Field neighbour : Cache.getNeighboursOfLevel(field,1).withColor(FieldColor.WHITE)){
		//	int value = fieldsAndValues.get(neighbour);
		//	fieldsAndValues.put(neighbour, value + plus);
		//}
	}
}
