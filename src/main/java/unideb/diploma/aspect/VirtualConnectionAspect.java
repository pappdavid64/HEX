package unideb.diploma.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import unideb.diploma.App;
import unideb.diploma.cache.Cache;
import unideb.diploma.config.AppConfig;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;

/**
 * Aspect for the virtual connections.
 * */
@Aspect
@Component
public class VirtualConnectionAspect {

	/**
	 * After a player move, removes the virtual connection of the field from the cache if it existed.
	 * */
	@AfterReturning(pointcut="onPlayerMove()", returning="returnValue")
	public void onPlayerMoveRemoveFieldFromVirtualConenction(Object returnValue) {
		if(returnValue != null) {
			Position position = ((Operator) returnValue).getPosition();
			Field searchedField = new Field(position, FieldColor.WHITE);
			try (ConfigurableApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
				App app = appContext.getBean(App.class);
		        Player playerOne = app.getPlayerOne();
		        Player playerTwo = app.getPlayerTwo();
		        
		        for(VirtualConnection connection : Cache.getVirtualConnectionsOf(playerOne)) {
		        	if(connection.getConnections().contains(searchedField)) {
		        		connection.getConnections().remove(searchedField);
		        	}			
	        	}
		        for(VirtualConnection connection : Cache.getVirtualConnectionsOf(playerTwo)) {
		        	if(connection.getConnections().contains(searchedField)) {
		        		connection.getConnections().remove(searchedField);
		        	}			
				}
			}
		}
	}
	
	@Pointcut("execution(* unideb.diploma.logic.Player.getNextMove(..))")
	public void onPlayerMove() {}
}
