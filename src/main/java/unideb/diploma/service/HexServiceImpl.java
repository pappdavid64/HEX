package unideb.diploma.service;

import unideb.diploma.annotation.ExecutionTime;
import unideb.diploma.annotation.Log;
import unideb.diploma.cache.Cache;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

/**
 * Responsive for the legal moves of the players.
 * */
public class HexServiceImpl implements HexService {


	/**
	 * Gets and checks the next move from the player.
	 * If its not valid move, then the service throws it back.
	 * @param player The player who's turn is.
	 * @return the move of the player.
	 * */
	
	@Override
	@ExecutionTime
	@Log
	public Operator getNextMoveFrom(Player player, State state) {
		//System.out.println("Player: " + player.getName() + "'s turn.");
		Operator nextMove;
		while( (nextMove = player.getNextMove(state)) == null) {
			
		}
		Cache.removeOperatorFromUseableOperators(nextMove);
		Cache.addVirtualConnectionsFromField(player, state.getFieldAt(nextMove.getPosition()), state);
		return nextMove;
	}

}
