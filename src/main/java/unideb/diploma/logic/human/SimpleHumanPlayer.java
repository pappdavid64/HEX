package unideb.diploma.logic.human;

import java.util.concurrent.CountDownLatch;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;
import unideb.diploma.strategy.Strategy;

/**
 * A simple human player.
 * */
public class SimpleHumanPlayer extends PlayerWithNameAndColor{

	/**
	 * Latch for waiting for the player's click.
	 * */
	private static CountDownLatch latch;
	
	/**
	 * The position of the player's click.
	 * */
	private static Position position;
	
	/**
	 * Constructor.
	 * @param name The name of the player.
	 * @param color The color of the player.
	 * */
	public SimpleHumanPlayer(String name, FieldColor color) {
		super(name, color);
	}

	/**
	 * Gets the next move by the state.
	 * @param state The state of the game.
	 * @return The operator which will be used.
	 * */
	@Override
	public Operator getNextMove(State state) {
		latch = new CountDownLatch(1);
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Cache.getOperatorAt(position);
	}

	/**
	 * Sets the position on player's click.
	 * @param position The position where the player clicked.
	 * */
	public static void setPositionOnClick(Position position) {
		SimpleHumanPlayer.position = position;
		latch.countDown();
	}

	@Override
	public void setStrategies(Strategy[] strategies) {

	}

	@Override
	public void init(){

	}
	
}
