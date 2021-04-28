package unideb.diploma.logic;

import unideb.diploma.cache.Direction;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.strategy.Strategy;

/**
 * Player
 * */
public interface Player {
	
	/**
	 * Gets the color of the player.
	 * @return The color of the player.
	 * */
	FieldColor getColor();
	
	/**
	 * Gets the opponent color of the player.
	 * @return The color of the opponent.
	 * */
	FieldColor getOpponentColor();
	
	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	Operator getNextMove(State state);

	/**
	 * Gets the name.
	 * @return The name.
	 * */
	String getName();
	
	/**
	 * Gets the directions where the player has to build the path.
	 * @return The direction where the player has to build the path.
	 * */
	Direction[] getDirections();
	
	/**
	 * Checks if the player is equals to the other.
	 * @param other The other player.
	 * @return true if the other player is equals with the player.
	 * */
	boolean isEquals(Player other);
	
	/**
	 * Gets the opponent.
	 * @return The opponent.
	 * */
	Player getOpponent();
	
	/**
	 * Sets the opponent
	 * @param opponent The opponent.
	 * */
	void setOpponent(Player opponent);
	
	/**
	 * Sets the strategies of the AI player.
	 * @param strategies The strategies.
	 * */
	void setStrategies(Strategy[] strategies);

	void init();
}
