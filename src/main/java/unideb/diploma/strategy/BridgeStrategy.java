package unideb.diploma.strategy;

import java.util.ArrayList;
import java.util.List;

import unideb.diploma.cache.Cache;
import unideb.diploma.cache.Direction;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.strength.StrategyStrength;

public class BridgeStrategy implements Strategy{

	private Field base;
	private Player player;
	private BaseSelector baseSelector;
	private boolean active;
	private boolean baseChanged;
	
	public BridgeStrategy(Player player) {
		this.player = player;
		active = true;
		baseSelector = new BaseSelector(player);
	}
	
	@Override
	public Operator getNextMove(State state) {
		Field selectedField = null;

		if(baseChanged) {
			baseChanged = false;
			return Cache.getOperatorAt(base.getPosition());
		}
		
		for(Direction direction : player.getDirections()) {
			Field actual = base;
			while(	selectedField == null ) {
				List<Field> neighbours = new ArrayList<>();
				for(Field neighbour : Cache.getNeighboursByDirection(direction, actual)) {
					FieldColor color = state.getFieldAt(neighbour.getPosition()).getColor();
					if(color == FieldColor.WHITE || color  == player.getColor()) {
						neighbours.addAll(Cache.getNeighboursByDirection(direction, neighbour));
					}
				}
				actual = getPlayersFieldFromList(neighbours, state);
				if(actual == null) {
					selectedField = selectFieldFromList(neighbours, state);
					break;
				}
			}
		}
		return Cache.getOperatorAt(selectedField.getPosition());
	}

	private Field getPlayersFieldFromList(List<Field> fields, State state) {
		for(Field field : fields) {
			if(state.getFieldAt(field.getPosition()).getColor() == player.getColor()) {
				return field;
			}
		}
		return null;
	}
	
	private Field selectFieldFromList(List<Field> fields, State state) {
		Field selected = null;
		int max = Integer.MIN_VALUE;
		for(Field field : fields) {
			int actualFieldCounter = (state.getFieldAt(field.getPosition()).getColor() == FieldColor.WHITE) ? getFieldAppearences(fields, field) : Integer.MIN_VALUE;
			if( actualFieldCounter > max) {
				max = actualFieldCounter;
				selected = field;
			}
		}
		return selected;
	}
	
	private int getFieldAppearences(List<Field> fields, Field field) {
		int counter = 0;
		for(Field actField : fields) {
			if(actField.equals(field)) {
				counter++;
			}
		}
		return counter;
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		if(base == null) {
			baseChanged = true;
			base = baseSelector.selectBaseFromWhiteFields(state).getBase();
		}
		if(baseSelector.canReachTheEndFromBase(state)) { 
			return StrategyStrength.medium(3);
			} else  {
				base = baseSelector.selectBaseFromWhiteFields(state).getBase();
				baseChanged = true;
				return StrategyStrength.veryWeak(0);
			}
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void deActivate() {
//		active = false;
	}

	@Override
	public void activate() {
		active = true;		
	}

	@Override
	public void reCalculate(State state) {
		base = baseSelector.selectBaseFromWhiteFields(state).getBase();
	}
	
}
