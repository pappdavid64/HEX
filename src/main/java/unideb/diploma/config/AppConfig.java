package unideb.diploma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.logic.Player;
import unideb.diploma.logic.ai.AIPlayer;
import unideb.diploma.logic.human.SimpleHumanPlayer;
import unideb.diploma.service.HexService;
import unideb.diploma.service.HexServiceImpl;
import unideb.diploma.strategy.BlockingStrategy;
import unideb.diploma.strategy.BridgeStrategy;
import unideb.diploma.strategy.FieldConnectorStrategy;
import unideb.diploma.strategy.FieldValueStrategy;
import unideb.diploma.strategy.PathBlockingStrategy;
import unideb.diploma.strategy.RandomStrategy;
import unideb.diploma.strategy.Strategy;
import unideb.diploma.strategy.WinningStrategy;
import unideb.diploma.view.HexView;
import unideb.diploma.view.HexViewImpl;

/**
 * Configuration class for the Spring framework.
 * */
@Configuration
@ComponentScan("unideb.diploma.aspect")
@EnableAspectJAutoProxy
public class AppConfig {

	/**
	 * The number of the rounds.
	 * */
	private final int NUMBER_OF_ROUNDS = 1;
	
	/**
	 * Singleton bean of HexViewImpl.
	 * @return The hex view.
	 * */
	@Bean
	public HexView view() {
		return new HexViewImpl();
	}
	
	/**
	 * Singleton bean of HexServiceImpl.
	 * @return The hex service.
	 * */
	@Bean
	public HexService service() {
		return new HexServiceImpl();
	}
	
	/**
	 * Blue human player.
	 * @return The blue human player.
	 * */
	@Bean(name="playerOne")
	public Player blueHumanPlayer() {
		SimpleHumanPlayer humanPlayer = new SimpleHumanPlayer("Human player", FieldColor.BLUE);
		return humanPlayer;
	}
	

	/**
	 * Red AI player.
	 * @return The red AI player.
	 * */
	@Bean(name="playerTwo")
	public Player redAIPlayer() {
		AIPlayer ai = new AIPlayer("AI player two", FieldColor.RED);
		return ai;
	}
	

	/**
	 * Blue AI player.
	 * @return The blue AI player.
	 * */
	@Bean(name="playerThree")
	public Player blueAIPlayer() {
		AIPlayer ai = new AIPlayer("AI player three", FieldColor.BLUE);
		ai.setStrategies(new Strategy[] {bridgeStrategy(ai), fieldConnectorStrategy(ai), randomStrategy(), blockingStrategy(ai,2), winningStrategy(ai,2)});
		return ai;
	}
	

	/**
	 * Red human player.
	 * @return The Red human player.
	 * */
	@Bean(name="playerFour")
	public Player redHumanPlayer() {
		SimpleHumanPlayer humanPlayer = new SimpleHumanPlayer("Human player", FieldColor.RED);
		return humanPlayer;
	}
	
	/**
	 * Creates a new bridge strategy for the player.
	 * @param player The player who has the strategy.
	 * @return Bridge strategy.
	 * */
	public Strategy bridgeStrategy(Player player) {
		return new BridgeStrategy(player);
	}
	

	/**
	 * Creates a new field connector strategy for the player.
	 * @param player The player who has the strategy.
	 * @return Field connector strategy.
	 * */
	public Strategy fieldConnectorStrategy(Player player) {
		return new FieldConnectorStrategy(player);
	}
	

	/**
	 * Creates a new field value strategy for the player.
	 * @param player The player who has the strategy.
	 * @return Field value strategy.
	 * */
	public Strategy fieldValueStrategy(Player player) {
		return new FieldValueStrategy(player);
	}
	

	/**
	 * Creates a new random strategy.
	 * @return Random strategy.
	 * */
	public Strategy randomStrategy(){
		return new RandomStrategy();
	}
	

	/**
	 * Creates a new blocking strategy for the player.
	 * @param player The player who has the strategy.
	 * @param depth The maximum allowed steps number.
	 * @return Blocking strategy.
	 * */
	public Strategy blockingStrategy(Player player, int depth) {
		return new BlockingStrategy(player, depth);
	}

	/**
	 * Creates a new winning strategy for the player.
	 * @param player The player who has the strategy.
	 * @param depth The maximum allowed steps number.
	 * @return Winning strategy.
	 * */
	public Strategy winningStrategy(Player player, int depth) {
		return new WinningStrategy(player, depth);
	}
	
	/**
	 * Creates a new path blocking strategy for the player.
	 * @param player The player who has the strategy.
	 * @return Path blocking strategy.
	 * */
	public Strategy pathBlockingStrategy(Player player) {
		return new PathBlockingStrategy(player);
	}
	
	/**
	 * Sets an AI player strategies.
	 * @param player The AI player.
	 * @param strategies The strategies.
	 * */
	private void setAiPlayerStrategies(Player player, Strategy[] strategies) {
		player.setStrategies(strategies);
	}
	
	/**
	 * Sets the AI players strategies.
	 * */
	private void setAiPlayersStrategies() {
		setAiPlayerStrategies(redAIPlayer(), new Strategy[] {
				bridgeStrategy(redAIPlayer()),
				fieldConnectorStrategy(redAIPlayer()),
				fieldValueStrategy(redAIPlayer()),
				randomStrategy(),
				blockingStrategy(redAIPlayer(), 2),
				winningStrategy(redAIPlayer(),2),
				pathBlockingStrategy(redAIPlayer())
				});
		setAiPlayerStrategies(blueAIPlayer(), new Strategy[] {
				bridgeStrategy(blueAIPlayer()),
				fieldConnectorStrategy(blueAIPlayer()),
				fieldValueStrategy(blueAIPlayer()),
				randomStrategy(),
				blockingStrategy(blueAIPlayer(), 2),
				winningStrategy(blueAIPlayer(),2),
				pathBlockingStrategy(blueAIPlayer())
				});
	}
	
	/**
	 * Singleton bean of App.
	 * @return The app.
	 * */
	@Bean
	public App app() {
		Cache.registerPlayer(blueHumanPlayer());
		Cache.registerPlayer(redAIPlayer());
		Cache.registerPlayer(blueAIPlayer());
		Cache.registerPlayer(redHumanPlayer());
		App app = new App(service(), view(), blueHumanPlayer(), redAIPlayer(), NUMBER_OF_ROUNDS);
		setAiPlayersStrategies();
		return app;
	}
}
