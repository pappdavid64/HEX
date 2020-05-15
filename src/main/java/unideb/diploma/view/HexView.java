package unideb.diploma.view;

import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

/**
 * Responsive for the display for the console.
 * */
public interface HexView {
	
	/**
	 * Prints the state to the console.
	 * */
	void printState(State state);

	/**
	 * Prints the winner to the console.
	 * */
	void printWinner(Player player);
	
}
