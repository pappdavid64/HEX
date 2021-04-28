package unideb.diploma;

import unideb.diploma.annotation.ExecutionTime;
import unideb.diploma.cache.Cache;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.service.HexService;
import unideb.diploma.statistic.GameStatistic;
import unideb.diploma.view.HexView;

/**
 * The application class.
 * */

public class App{	
	/**
	 * The size of the board.
	 * */
    public static final int BOARD_SIZE = 11;
    
    /**
     * Player one.
     * */
    private Player playerOne;
    
    /**
     * Player two.
     * */
    private Player playerTwo;
    
    /**
     * The number of matches played.
     * */
    private int numberOfMatches;
    
    /**
     * The service which controls the game.
     * */
    private HexService service;
    
    /**
     * The view which is responsible for the representation of the game.
     * */
    private HexView view;
    
    /**
     * The statistics of the game.
     * */
    private GameStatistic statistic;
    
    /**
     * The state of the game.
     * */
    private State state;
    
    /**
     * Constructor for the application.
     * @param service The service of the game.
     * @param view The view of the game.
     * @param playerOne The player who will start the game.
     * @param playerTwo The player who will be the second.
     * @param numberOfMatches The number of matches will be played.
     * */
    public App(HexService service, HexView view, Player playerOne, Player playerTwo, int numberOfMatches) {
    	this.service = service;
    	this.view = view;
    	this.playerOne = playerOne;
    	this.playerTwo = playerTwo;
    	this.numberOfMatches = numberOfMatches;
    	statistic = new GameStatistic();
    	statistic.savePlayer(playerOne);
    	statistic.savePlayer(playerTwo);
    	this.playerOne.setOpponent(playerTwo);
    	this.playerTwo.setOpponent(playerOne);
    }
    
    /**
     * The two player will play x matches, where x is the numberOfMatches.
     * */
    @ExecutionTime
    public void playGame() {
    	for(int i = 0; i < numberOfMatches; i++) {
			System.out.println("Round #" + (i + 1) );
    		playOneMatch();    		
    	}
    	statistic.printStatistics();
    }
    
    /**
     * One match will be played, while someone does not win.
     * */
    private void playOneMatch() {    	
    	state = Cache.getState();
    	while(true) {
    		state.applyOperator(service.getNextMoveFrom(playerOne, state));
    		if(state.isEndState()) {
    			view.printWinner(playerOne);
    			statistic.addWinToPlayer(playerOne);
    			System.out.println("--------------------------------------------");
    			resetGame();
    			break;
    		}
    		state.applyOperator(service.getNextMoveFrom(playerTwo, state));
    		if(state.isEndState()) {
    			view.printWinner(playerTwo);
    			statistic.addWinToPlayer(playerTwo);
    			System.out.println("--------------------------------------------");
    			resetGame();
    			break;
    		}
    	}
    }

    /**
     * Makes the application to wait.
     * @param millis How many milliseconds should the application wait.
     * */
    public void sleep(int millis) {
    	try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    /**
     * Reseting the game after someone won.
     * */
    private void resetGame() {
    	//sleep(3000);
    	Cache.reset();
    	this.playerOne.init();
    	this.playerTwo.init();
    	SpringApp.initStage();
    }
    
    
    /**
     * Gets the player one.
     * @return The player one of the game.
     * */
    public Player getPlayerOne() {
    	return playerOne;
    }
    
    
    /**
     * Gets the player two.
     * @return The player two of the game.
     * */
    public Player getPlayerTwo() {
    	return playerTwo;
    }
}
