package unideb.diploma.strategy;


import java.util.List;

import unideb.diploma.cache.Cache;
import unideb.diploma.domain.Field;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.connection.VirtualConnection;

public class FieldConnectorStrategy implements Strategy {

	private Player player;
	private boolean active;
	
	public FieldConnectorStrategy(Player player) {
		this.player = player;
		active = true;
	}
	
	@Override
	public Operator getNextMove(State state) {
		Operator nextMove = null;
		for(VirtualConnection connection : Cache.getVirtualConnectionsOf(player)) {
			if(connection.getConnectionsCount() == 1) {
				nextMove = Cache.getOperatorAt(connection.getConnections().get(0).getPosition());
				Cache.removeVirtualConnection(player, connection);
				break;
			}
		}
		if(nextMove == null) {
			VirtualConnection connection = Cache.getVirtualConnectionsOf(player).get(0);
			Field field = connection.getConnections().get(0);
			nextMove = Cache.getOperatorAt(field.getPosition());
			Cache.removeVirtualConnection(player, connection);
		}
		return nextMove;
	}

	@Override
	public int getGoodnessByState(State state) {
		boolean canEnd = (canEndFromVirtualConnections(state.clone()));
		return (canEnd) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
	}

	
	
	private boolean canEndFromVirtualConnections(State state) {
		List<VirtualConnection> connections = Cache.getVirtualConnectionsOf(player);
		for(VirtualConnection connection : connections) {
			Field field = connection.getConnections().get(0);
			Operator operator = Cache.getOperatorAt(field.getPosition());
			operator.use(state.getTable(), player.getColor());
			if(state.isEndState(player.getColor())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void deActivate() {
		active = false;
	}

	@Override
	public void activate() {
		active = true;		
	}
}
