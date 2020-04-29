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

@Configuration
@ComponentScan("unideb.diploma.aspect")
@EnableAspectJAutoProxy
public class AppConfig {

	private final int NUMBER_OF_ROUNDS = 9;
	
	@Bean
	public HexView view() {
		return new HexViewImpl();
	}
	
	@Bean
	public HexService service() {
		return new HexServiceImpl();
	}
	
	@Bean(name="playerOne")
	public Player playerOne() {
		SimpleHumanPlayer humanPlayer = new SimpleHumanPlayer("Human player", FieldColor.BLUE);
		return humanPlayer;
	}
	
	
	@Bean(name="playerTwo")
	public Player playerTwo() {
		AIPlayer ai = new AIPlayer("AI player two", FieldColor.RED);
		ai.setStrategies(new Strategy[] {
				bridgeStrategy(ai),
				fieldConnectorStrategy(ai),
				fieldValueStrategy(),
				randomStrategy(),
				blockingStrategy(ai,2),
				winningStrategy(ai,2),
				pathBlockingStrategy(ai)
				});
		return ai;
	}
	
	@Bean(name="playerThree")
	public Player playerThree() {
		AIPlayer ai = new AIPlayer("AI player three", FieldColor.BLUE);
		ai.setStrategies(new Strategy[] {bridgeStrategy(ai), fieldConnectorStrategy(ai), randomStrategy(), blockingStrategy(ai,2), winningStrategy(ai,2)});
		return ai;
	}
	
	@Bean(name="playerFour")
	public Player playerFour() {
		SimpleHumanPlayer humanPlayer = new SimpleHumanPlayer("Human player", FieldColor.RED);
		return humanPlayer;
	}
	
	public Strategy bridgeStrategy(Player player) {
		return new BridgeStrategy(player);
	}
	
	public Strategy fieldConnectorStrategy(Player player) {
		return new FieldConnectorStrategy(player);
	}
	
	public Strategy fieldValueStrategy() {
		return new FieldValueStrategy();
	}
	
	public Strategy randomStrategy(){
		return new RandomStrategy();
	}
	
	public Strategy blockingStrategy(Player player, int depth) {
		return new BlockingStrategy(player, depth);
	}
	
	public Strategy winningStrategy(Player player, int depth) {
		return new WinningStrategy(player, depth);
	}
	
	public Strategy pathBlockingStrategy(Player player) {
		return new PathBlockingStrategy(player);
	}
	
	@Bean
	public App app() {
		Cache.registerPlayer(playerOne());
		Cache.registerPlayer(playerTwo());
		Cache.registerPlayer(playerThree());
		Cache.registerPlayer(playerFour());
		return new App(service(), view(), playerOne(), playerTwo(), NUMBER_OF_ROUNDS);
	}
}
