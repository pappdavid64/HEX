package unideb.diploma.strategy.strength;

public enum Strength {
	VERY_STRONG(5), STRONG(4), MEDIUM(3), WEAK(2), VERY_WEAK(1);
	
	private int value;
	
	public boolean isStronger(Strength other) {
		return value > other.value;
	}
	
	public boolean isEquals(Strength other) {
		return value == other.value;
	}
	
	
	public int getValue() {
		return value;
	}
	
	private Strength(int value) {
		this.value = value;
	}
}
