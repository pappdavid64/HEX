package unideb.diploma.service;

import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

/**
 * Responsive for the legal moves of the players.
 * */
public interface HexService {
	
	/**
	 * Gets and checks the next move from the player.
	 * If its not valid move, then the service throws it back.
	 * @param player The player who's turn is.
	 * @return the move of the player.
	 * */
	Operator getNextMoveFrom(Player player, State state);
	
}
