package unideb.diploma.service;

import unideb.diploma.annotation.ExecutionTime;
import unideb.diploma.annotation.Log;
import unideb.diploma.cache.Cache;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

public class HexServiceImpl implements HexService {

	@Override
	@ExecutionTime
	@Log
	public Operator getNextMoveFrom(Player player, State state) {
		System.out.println("Player: " + player.getName() + "'s turn.");
		Operator nextMove;
		while( (nextMove = player.getNextMove(state)) == null) {
			
		}
		Cache.removeOperatorFromUseableOperators(nextMove);
		Cache.addVirtualConnectionsFromField(player, state.getFieldAt(nextMove.getPosition()), state);
//		Cache.printVIrtualConnectionsWithPlayers();
		return nextMove;
	}

}
