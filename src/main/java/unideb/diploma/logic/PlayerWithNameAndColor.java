package unideb.diploma.logic;

import java.util.Arrays;
import java.util.Objects;

import unideb.diploma.cache.Direction;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;

public abstract class PlayerWithNameAndColor implements Player {
	private String name;
	private FieldColor color;
	private Direction[] directions;
	
	public PlayerWithNameAndColor(String name, FieldColor color) {
		this.name = name;
		this.color = color;
		if(color == FieldColor.BLUE) {
			directions = new Direction[]{Direction.EAST, Direction.WEST};
		} else {
			directions = new Direction[]{Direction.NORTH, Direction.SOUTH};
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public FieldColor getColor() {
		return color;
	}
	
	@Override
	public FieldColor getOpponentColor() {
		return (color == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE; 
	}
	
	@Override
	public Direction[] getDirections() {
		return directions;
	}
	
	public abstract Operator getNextMove(State state);

	@Override
	public String toString() {
		return super.toString() + ", PlayerWithNameAndColor [name=" + name + ", color=" + color + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(directions);
		result = prime * result + Objects.hash(color, name);
		return result;
	}
	
	@Override
	public boolean isEquals(Player other) {
		return this.name == other.getName() && this.color == other.getColor();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		return color == other.getColor() && Objects.equals(name, other.getName());
	}
	
	
}
