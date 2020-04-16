package unideb.diploma.statistic;

import java.util.HashMap;
import java.util.Map;

import unideb.diploma.exception.GameStatisticException;
import unideb.diploma.logic.Player;

public class GameStatistic {
	private Map<Player, Integer> playerWinCounter;
	
	public GameStatistic() {
		playerWinCounter = new HashMap<>();
	}
	
	public void savePlayer(Player player) {
		if(playerWinCounter.containsKey(player)) {
			throw new GameStatisticException("Player: " + player.getName() + " already saved!");
		}
		playerWinCounter.put(player, 0);
	}
	
	public void addWinToPlayer(Player player) {
		if(!playerWinCounter.containsKey(player)) {
			throw new GameStatisticException("Player: " + player.getName() + " is not found!");
		}
		Integer newWinCounter = playerWinCounter.get(player) + 1;
		playerWinCounter.put(player, newWinCounter);
	}
	
	public void printStatistics() {
		for(Player player : playerWinCounter.keySet()) {
			System.out.println("Player: " + player.getName() + " won: " + playerWinCounter.get(player) + " times.");
		}
	}
}
