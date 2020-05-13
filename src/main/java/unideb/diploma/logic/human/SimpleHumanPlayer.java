package unideb.diploma.logic.human;

import java.util.concurrent.CountDownLatch;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;
import unideb.diploma.strategy.Strategy;

public class SimpleHumanPlayer extends PlayerWithNameAndColor{

	private static CountDownLatch latch;
	private static Position position;
	
	
	public SimpleHumanPlayer(String name, FieldColor color) {
		super(name, color);
	}

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

	public static void setPositionOnClick(Position position) {
		SimpleHumanPlayer.position = position;
		latch.countDown();
	}

	@Override
	public void setStrategies(Strategy[] strategies) {
		// TODO Auto-generated method stub
		
	}
	
}
