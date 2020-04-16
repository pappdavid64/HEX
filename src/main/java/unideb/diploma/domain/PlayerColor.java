package unideb.diploma.domain;

public enum PlayerColor {
	RED('R'), BLUE('B');
	
	private char value;
	
	private PlayerColor(char value) {
		this.value = value;
	}

	public char getValue() {
		return value;
	}

}
