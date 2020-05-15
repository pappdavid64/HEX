package unideb.diploma.strategy.strength;

/**
 * Strength
 * */
public enum Strength {
	VERY_STRONG(5), STRONG(4), MEDIUM(3), WEAK(2), VERY_WEAK(1);
	
	/**
	 * The strength in number.
	 * It is for easier comparing.
	 * */
	private int value;
	
	/**
	 * Checks if the other strength is stronger.
	 * @param other The other strength.
	 * @return true if it is stronger than the other.
	 * */
	public boolean isStronger(Strength other) {
		return value > other.value;
	}
	
	/**
	 * Checks if the other strength is equally strong.
	 * @param other The other strength.
	 * @return true if the other is equally strong.
	 * */
	public boolean isEquals(Strength other) {
		return value == other.value;
	}
	
	/**
	 * Gets the value.
	 * @return The value.
	 * */
	public int getValue() {
		return value;
	}
	
	/**
	 * Constructor.
	 * @param value The value of the strength.
	 * */
	private Strength(int value) {
		this.value = value;
	}
}
