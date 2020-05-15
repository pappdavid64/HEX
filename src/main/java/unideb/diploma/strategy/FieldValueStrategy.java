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

/**
 * Strategy which selects the field with the best value.
 * */
public class FieldValueStrategy implements Strategy, Observer {

	/**
	 * A map that contains the goodness of each field.
	 * */
	private Map<Field, Integer> fieldsAndValues;
	
	/**
	 * The player whom the strategy want to win.
	 * */
	private Player player;

	/**
	 * Constructor.
	 * @param player The player whom the strategy want to win.
	 * */
	public FieldValueStrategy(Player player) {
		this.player = player;
		fieldsAndValues = getBaseValues();
	}

	/**
	 * Initialize the base values.
	 * @return The map of fields with their base values.
	 * */
	private Map<Field, Integer> getBaseValues() {
		Map<Field, Integer> baseFieldsAndValues = new HashMap<>();

		for(Field field : Cache.getState().getTable().getFields()){
			baseFieldsAndValues.put(field, field.getGoodness());
			field.addObserver(this);
		}

		return baseFieldsAndValues;
	}

	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	@Override
	public Operator getNextMove(State state) {
		Field fieldByBestValue = getBestValueOfFields();
		Operator nextOperator = Cache.getOperatorAt(fieldByBestValue.getX(), fieldByBestValue.getY());
		return nextOperator;
	}

	/**
	 * Gets the goodness of the strategy by the state.
	 * @param state The state of the game.
	 * @return The strength of the strategy by the state.
	 * */
	@Override
	public StrategyStrength getGoodnessByState(State state) {
		return StrategyStrength.weak(getBestValueOfFields().getGoodness());
	}
	
	/**
	 * Gets the best valued field.
	 * @return the best valued field.
	 * */
	public Field getBestValueOfFields() {
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
		System.out.println(bestValuedFields);
		return bestValuedFields.get(rand.nextInt(bestValuedFields.size()));
	}

	/**
	 * If a field's color is set, then notifies the strategy.
	 * @param observable The field which color was set.
	 * */
	@Override
	public void notify(Observable observable) {
		Field field = (Field)observable;
		if(field.getColor() == player.getColor()) {
			setFieldValue(field, field, new ArrayList<>(), 1);
		} else {
			setFieldValue(field, field, new ArrayList<>(), -1);
		}
	}
	
	private void setFieldValue(Field base, Field field, List<Field> alreadyWas, int plus) {
		fieldsAndValues.put(field, fieldsAndValues.get(field) + plus);
		alreadyWas.add(field);
		List<Field> neighbours = Cache.getNeighbours(field).withColor(FieldColor.WHITE);
		for(Field neighbour : neighbours) {
			if(!alreadyWas.contains(neighbour)) {
				setFieldValue(base, neighbour, alreadyWas, plus + (1 * plus) );
			}
		}
	}
	
	
}
