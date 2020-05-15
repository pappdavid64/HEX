package unideb.diploma.statistic;

import java.util.HashMap;
import java.util.Map;

import unideb.diploma.logic.Player;

/**
 * Statistics of the game.
 * */
public class GameStatistic {
	/**
	 * Map with the player and their wins.
	 * */
	private Map<Player, Integer> playerWinCounter;
	
	/**
	 * Constructor.
	 * */
	public GameStatistic() {
		playerWinCounter = new HashMap<>();
	}
	
	/**
	 * Saves the player.
	 * @param player The player whom wanted to be saved.
	 * */
	public void savePlayer(Player player) {
		playerWinCounter.put(player, 0);
	}
	
	/**
	 * Add win to the player.
	 * @param player The player who won.
	 * */
	public void addWinToPlayer(Player player) {
		Integer newWinCounter = playerWinCounter.get(player) + 1;
		playerWinCounter.put(player, newWinCounter);
	}
	
	/**
	 * Prints statistics to the console.
	 * */
	public void printStatistics() {
		for(Player player : playerWinCounter.keySet()) {
			System.out.println("Player: " + player.getName() + " won: " + playerWinCounter.get(player) + " times.");
		}
	}
}
