package unideb.diploma.service;

import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

public interface HexService {
	Operator getNextMoveFrom(Player player, State state);
	
}
