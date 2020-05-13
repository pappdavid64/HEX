package unideb.diploma.statistic;

import java.util.HashMap;
import java.util.Map;

import unideb.diploma.logic.Player;

public class GameStatistic {
	private Map<Player, Integer> playerWinCounter;
	
	public GameStatistic() {
		playerWinCounter = new HashMap<>();
	}
	
	public void savePlayer(Player player) {
		playerWinCounter.put(player, 0);
	}
	
	public void addWinToPlayer(Player player) {
		Integer newWinCounter = playerWinCounter.get(player) + 1;
		playerWinCounter.put(player, newWinCounter);
	}
	
	public void printStatistics() {
		for(Player player : playerWinCounter.keySet()) {
			System.out.println("Player: " + player.getName() + " won: " + playerWinCounter.get(player) + " times.");
		}
	}
}
