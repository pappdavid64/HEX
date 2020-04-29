package unideb.diploma;

import unideb.diploma.annotation.ExecutionTime;
import unideb.diploma.cache.Cache;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.service.HexService;
import unideb.diploma.statistic.GameStatistic;
import unideb.diploma.view.HexView;

public class App{	
	
    public static final int BOARD_SIZE = 11;
    
    private Player playerOne;
    private Player playerTwo;
    private int numberOfMatches;
    private HexService service;
    private HexView view;
    private GameStatistic statistic;
    private State state;
    
    public App() {}
    
    public App(HexService service, HexView view, Player playerOne, Player playerTwo, int numberOfMatches) {
    	this.service = service;
    	this.view = view;
    	this.playerOne = playerOne;
    	this.playerTwo = playerTwo;
    	this.numberOfMatches = numberOfMatches;
    	statistic = new GameStatistic();
    	statistic.savePlayer(playerOne);
    	statistic.savePlayer(playerTwo);
    	state = Cache.getState();
    }
    
    @ExecutionTime
    public void playGame() {
    	for(int i = 0; i < numberOfMatches; i++) {
    		playOneMatch();    		
    	}
    	statistic.printStatistics();
    }
    
    private void playOneMatch() {    	
    	state = Cache.getState();
    	Cache.resetUseableOperators();
    	while(true) {
    		state.applyOperator(service.getNextMoveFrom(playerOne, state));
    		if(state.isEndState()) {
    			view.printWinner(playerOne);
    			statistic.addWinToPlayer(playerOne);
    			System.out.println("--------------------------------------------");
    			sleep(3000);
    			Cache.resetState();
    			SpringApp.setStage();
    			break;
    		}
    		state.applyOperator(service.getNextMoveFrom(playerTwo, state));
    		if(state.isEndState()) {
    			view.printWinner(playerTwo);
    			statistic.addWinToPlayer(playerTwo);
    			System.out.println("--------------------------------------------");
    			sleep(3000);
    			Cache.resetState();
    			SpringApp.setStage();
    			break;
    		}
    	}
    }

    public static void sleep(int millis) {
    	try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Player getPlayerOne() {
    	return playerOne;
    }
    
    public Player getPlayerTwo() {
    	return playerTwo;
    }
}
