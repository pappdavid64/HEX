package unideb.diploma.strategy;


import java.util.ArrayList;
import java.util.List;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.domain.FieldColor;
import unideb.diploma.domain.Position;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.ConnectionUtil;
import unideb.diploma.strategy.connection.VirtualConnection;
import unideb.diploma.strategy.strength.StrategyStrength;

public class FieldConnectorStrategy implements Strategy {

	private Player player;
	private int longestWayLength;
	private Operator nextMove;
	
	public FieldConnectorStrategy(Player player) {
		this.player = player;
	}
	
	@Override
	public Operator getNextMove(State state) {
//		for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
//			if(connection.getConnectionsCount() == 1 && ConnectionUtil.isTheOnlyOneConnection(connection, player, state) && !canReachAnotherFieldIntheLineFromVirtualConnection(state, connection)) {
//				nextMove = Cache.getOperatorAt(connection.getConnections().get(0).getPosition());
//				Cache.removeVirtualConnection(player, connection);
//				break;
//			}
//		}
		if(nextMove == null) {
			for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
				if(!canReachAnotherFieldIntheLineFromVirtualConnection(state, connection)) {
					Field field = connection.getConnections().get(0);
					nextMove = Cache.getOperatorAt(field.getPosition());
					Cache.removeVirtualConnection(player, connection);
					break;
				}
			}
		}
		return nextMove;
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		boolean canEnd = (canEndFromVirtualConnections(state.clone()));
		boolean makesLonger = virtualConnectionWithOneFieldMakesThePathLonger(state.clone());
		return ( makesLonger || canEnd) ? StrategyStrength.strong(longestWayLength) : StrategyStrength.weak(1);
	}

	
	
	private boolean virtualConnectionWithOneFieldMakesThePathLonger(State state) {
		longestWayLength = state.getPathLength(player.getColor());
		
		for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
			int actualLenght;
			if(connection.getConnectionsCount() == 1 && (ConnectionUtil.isTheOnlyOneConnection(connection, player, state) && !canReachAnotherFieldIntheLineFromVirtualConnection(state, connection))) {
				Position position = connection.getConnections().get(0).getPosition();
				State actualState = state.clone();
				Cache.getOperatorAt(position).use(actualState.getTable(), player.getColor());
				actualLenght = actualState.getPathLength(player.getColor());
				if(actualLenght > longestWayLength) {
					longestWayLength = actualLenght;
					nextMove = Cache.getOperatorAt(position);
					return true;
				}
			}
		}
		nextMove = null;
		return false;
	}

	private boolean canEndFromVirtualConnections(State state) {
		List<VirtualConnection> connections = Cache.getVirtualConnectionsOf(player);
		for(VirtualConnection connection : connections) {
			Field field = connection.getConnections().get(0);
			Operator operator = Cache.getOperatorAt(field.getPosition());
			operator.use(state.getTable(),player.getColor());
			if(state.isEndState(player.getColor())) {
				return true;
			}
		}
		return false;
	}

	private boolean canReachAnotherFieldIntheLineFromVirtualConnection(State state, VirtualConnection connection) {
		List<Field> reachableFields = new ArrayList<>();
		Field field = connection.getConnections().get(0);
		for(Field actual : Cache.getNeighbours(field).withColor(player.getColor())) {
			System.out.println("actual field: " + actual);
			reachableFields = state.getReachableFieldsFrom(actual, new ArrayList<>());
			System.out.println("reachable fields: " + reachableFields);
			for(Field reachableField : reachableFields) {
				VirtualConnection con =  Cache.getConnectionFromField(player, reachableField);
				if(con != null) {
					for(Field connectionOfVirtualConnection : con.getConnections()) {
						if(player.getColor() == FieldColor.RED) {
							if(connectionOfVirtualConnection.getX() == field.getX()) {
								return true;
							}
						}
						if(player.getColor() == FieldColor.BLUE) {
							if(connectionOfVirtualConnection.getY() == field.getY()) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
}
