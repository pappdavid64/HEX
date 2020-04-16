package unideb.diploma.view;

import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

public interface HexView {
	void printState(State state);

	void printWinner(Player player);
	
}
