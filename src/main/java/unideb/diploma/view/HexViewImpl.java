package unideb.diploma.view;

import unideb.diploma.App;
import unideb.diploma.domain.Position;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

public class HexViewImpl implements HexView {

	@Override
	public void printState(State state) {
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			printNSpaces(i);
			for(int j = 0; j < App.BOARD_SIZE; j++) {
				printFieldColorAt(state, i, j);
//				printFieldGoodnessAt(state, i, j);
			}
			printNewLine();
		}
	}

	private void printNewLine() {
		System.out.println();
	}

	private void printFieldColorAt(State state, int i, int j) {
		System.out.print(state.getTable().getFieldAt(new Position(i,j)).getColor().getValue() + " ");
	}

	private void printNSpaces(int n) {
		for(int i = 0; i < n; i++)
			System.out.print(" ");
	}

//	private void printFieldGoodnessAt(State state, int i, int j) {
//		System.out.print(state.getTable().getFieldAt(new Position(i,j)).getGoodness() + " ");
//	}
	
	@Override
	public void printWinner(Player player) {
		System.out.println("Player: " + player.getName() + " won the game.");
	}

}
