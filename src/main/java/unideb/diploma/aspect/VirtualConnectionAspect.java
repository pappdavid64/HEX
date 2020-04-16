package unideb.diploma.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import unideb.diploma.cache.Cache;
import unideb.diploma.config.AppConfig;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;

@Aspect
@Component
public class VirtualConnectionAspect {

	@AfterReturning(pointcut="onPlayerMove()", returning="returnValue")
	public void onPlayerMoveRemoveFieldFromVirtualConenction(Object returnValue) {
		if(returnValue != null) {
			Position position = ((Operator) returnValue).getPosition();
			Field searchedField = new Field(position, FieldColor.WHITE);
			try (ConfigurableApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
		        Player playerOne = appContext.getBean("playerOne", Player.class);
		        Player playerTwo = appContext.getBean("playerTwo", Player.class);
		        
		        for(VirtualConnection connection : Cache.getVirtualConnectionsOf(playerOne)) {
		        	if(connection.getConnections().contains(searchedField)) {
		        		connection.getConnections().remove(searchedField);
		        		if(connection.getConnectionsCount() == 0) {
		        			Cache.removeVirtualConnection(playerOne, connection);
		        			break;
		        		}
		        	}			
	        	}
		        for(VirtualConnection connection : Cache.getVirtualConnectionsOf(playerTwo)) {
		        	if(connection.getConnections().contains(searchedField)) {
		        		connection.getConnections().remove(searchedField);
		        		if(connection.getConnectionsCount() == 0) {
		        			Cache.removeVirtualConnection(playerTwo, connection);
		        			break;
		        		}
		        	}			
				}
			}
		}
	}
	
	@Pointcut("execution(* unideb.diploma.logic.Player.getNextMove(..))")
	public void onPlayerMove() {}
}
