package unideb.diploma.view;

import unideb.diploma.App;
import unideb.diploma.domain.Position;
import unideb.diploma.game.State;
import unideb.diploma.logic.Player;

/**
 * Responsive for the display for the console.
 * */
public class HexViewImpl implements HexView {

	
	/**
	 * Prints the state to the console.
	 * */
	@Override
	public void printState(State state) {
		for(int i = 0; i < App.BOARD_SIZE; i++) {
			printNSpaces(i);
			for(int j = 0; j < App.BOARD_SIZE; j++) {
				printFieldColorAt(state, i, j);
			}
			printNewLine();
		}
	}

	/**
	 * Prints a new line to the console.
	 * */
	private void printNewLine() {
		System.out.println();
	}

	/**
	 * Prints the field color at a position to the console.
	 * @param state The state of the game.
	 * @param i The row index.
	 * @param j The column index.
	 * */
	private void printFieldColorAt(State state, int i, int j) {
		System.out.print(state.getTable().getFieldAt(new Position(i,j)).getColor().getValue() + " ");
	}

	/**
	 * Prints n spaces to the console.
	 * @param n The number of the spaces.
	 * */
	private void printNSpaces(int n) {
		for(int i = 0; i < n; i++)
			System.out.print(" ");
	}
	
	/**
	 * Prints the winner to the console.
	 * */
	@Override
	public void printWinner(Player player) {
		System.out.println("Player: " + player.getName() + " won the game.");
	}

}
