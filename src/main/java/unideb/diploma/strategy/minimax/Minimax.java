package unideb.diploma.strategy.minimax;

import unideb.diploma.cache.Cache;
import unideb.diploma.game.Operator;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;
import unideb.diploma.strategy.Strategy;
import unideb.diploma.strategy.strength.StrategyStrength;

import java.util.List;

public class Minimax implements Strategy {

	private int depth;
	private Player player;

	public Minimax(int depth, Player player){
		this.depth = depth;
		this.player = player;
	}

	@Override
	public Operator getNextMove(State state) {
		return getNextMoveWithMinimax(state.clone(), Cache.getUseableOperators());
	}

	@Override
	public StrategyStrength getGoodnessByState(State state) {
		return StrategyStrength.medium(depth);
	}

	@Override
	public void init() {

	}

	private Operator getNextMoveWithMinimax(State state, List<Operator> useableOperators){
		Node startingNode = new Node(depth, 0, state, useableOperators, null, null);
		return maxNode(startingNode).getMadeBy();
	}

	private Node maxNode(Node node) {
		Node best = null;
		if(node.isLeafNode()) {
			return node;
		}
		for(Node child : node.getChildNodes()) {
			Node next = minNode(child);
			if(best == null) {
				best = next;
			} else if(getHeuristic(best, true) < getHeuristic(next, true)) {
				best = next;
			}
		}

		return best;
	}
	
	private Node minNode(Node node) {
		Node best = null;
		if(node.isLeafNode()) {
			return node;
		}
		for(Node child : node.getChildNodes()) {
			Node next = maxNode(child);
			if(best == null) {
				best = next;
			} else if(getHeuristic(best, false) > getHeuristic(next, false)) {
				best = next;
			}
		}
			
		return best;
	}
	
	private int getHeuristic(Node node, boolean isMax){
		if(node.getState().isEndState()) {
			return isMax ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		}
		return isMax ? node.getState().getLongestPathLength(player.getColor()) - node.getState().getLongestPathLength(player.getOpponentColor()) :
			node.getState().getLongestPathLength(player.getOpponentColor()) - node.getState().getLongestPathLength(player.getColor());
	}

}
