package unideb.diploma.logic.human;

//import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.PlayerWithNameAndColor;

public class SimpleHumanPlayer extends PlayerWithNameAndColor{

//	private Scanner reader;
	private static CountDownLatch latch;
	private static Position position;
	
	
	public SimpleHumanPlayer(String name, FieldColor color) {
		super(name, color);
//		reader = new Scanner(System.in);
	}

	@Override
	public Operator getNextMove(State state) {
//		int x = reader.nextInt() - 1;
//		int y = reader.nextInt() - 1;
		latch = new CountDownLatch(1);
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Cache.getOperatorAt(position);
	}

	public static void setPositionOnClick(Position position) {
		SimpleHumanPlayer.position = position;
		latch.countDown();
	}
	
}
