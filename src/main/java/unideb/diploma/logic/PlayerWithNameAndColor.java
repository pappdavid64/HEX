package unideb.diploma.logic;

import java.util.Arrays;
import java.util.Objects;

import unideb.diploma.cache.Direction;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;

/**
 * Player with name and color.
 * */
public abstract class PlayerWithNameAndColor implements Player {
	/**
	 * The name of the player.
	 * */
	private String name;
	
	/**
	 * The color of the player.
	 * */
	private FieldColor color;
	
	/**
	 * The direction where the player has to build the path.
	 * */
	private Direction[] directions;
	
	/**
	 * The opponent.
	 * */
	private Player opponent;
	
	/**
	 * Constructor.
	 * @param name The name of the player.
	 * @param color The color of the player.
	 * */
	public PlayerWithNameAndColor(String name, FieldColor color) {
		this.name = name;
		this.color = color;
		if(color == FieldColor.BLUE) {
			directions = new Direction[]{Direction.EAST, Direction.WEST};
		} else {
			directions = new Direction[]{Direction.NORTH, Direction.SOUTH};
		}
	}
	
	
	/**
	 * Gets the name.
	 * @return The name.
	 * */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the color of the player.
	 * @return The color of the player.
	 * */
	@Override
	public FieldColor getColor() {
		return color;
	}
	
	/**
	 * Gets the opponent color of the player.
	 * @return The color of the opponent.
	 * */
	@Override
	public FieldColor getOpponentColor() {
		return (color == FieldColor.BLUE) ? FieldColor.RED : FieldColor.BLUE; 
	}
	
	/**
	 * Gets the directions where the player has to build the path.
	 * @return The direction where the player has to build the path.
	 * */
	@Override
	public Direction[] getDirections() {
		return directions;
	}
	
	/**
	 * Gets the opponent.
	 * @return The opponent.
	 * */
	@Override
	public Player getOpponent() {
		return opponent;
	}
	
	/**
	 * Sets the opponent
	 * @param opponent The opponent.
	 * */
	@Override
	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}
	
	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
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
	
	/**
	 * Checks if the player is equals to the other.
	 * @param other The other player.
	 * @return true if the other player is equals with the player.
	 * */
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
