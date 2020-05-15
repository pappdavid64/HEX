package unideb.diploma.strategy.strength;

/**
 * Represents the strength of a strategy.
 * */
public class StrategyStrength {
	
	/**
	 * Creates a very weak strategy strength with value.
	 * @param value The value.
	 * @return The strategy strength with the value.
	 * */
	public static StrategyStrength veryWeak(int value) {
		return of(Strength.VERY_WEAK, value);
	}
	
	/**
	 * Creates a weak strategy strength with value.
	 * @param value The value.
	 * @return The strategy strength with the value.
	 * */
	public static StrategyStrength weak(int value) {
		return of(Strength.WEAK, value);
	}
	
	/**
	 * Creates a medium strategy strength with value.
	 * @param value The value.
	 * @return The strategy strength with the value.
	 * */
	public static StrategyStrength medium(int value) {
		return of(Strength.MEDIUM, value);
	}
	
	/**
	 * Creates a strong strategy strength with value.
	 * @param value The value.
	 * @return The strategy strength with the value.
	 * */
	public static StrategyStrength strong(int value) {
		return of(Strength.STRONG, value);
	}
	
	/**
	 * Creates a very strong strategy strength with value.
	 * @param value The value.
	 * @return The strategy strength with the value.
	 * */
	public static StrategyStrength veryStrong(int value) {
		return of(Strength.VERY_STRONG, value);
	}
	
	/**
	 * Creates a strategy strength with value.
	 * @param strength The strength.
	 * @param value The value.
	 * @return The strategy strength with the value.
	 * */
	private static StrategyStrength of(Strength strength, int value) {
		return new StrategyStrength(strength, value);
	}
	
	/**
	 * The strength of the strategy.
	 * */
	private Strength strength;
	
	/**
	 * The value of the strategy.
	 * */
	private int value;

	/**
	 * Constructor.
	 * @param strength The strength of the strategy.
	 * @param value The value of the strategy.
	 * */
	private StrategyStrength(Strength strength, int value) {
		this.strength = strength;
		this.value = value;
	}
	
	/**
	 * Gets the strength.
	 * @return The strength.
	 * */
	public Strength getStrength() {
		return strength;
	}
	
	/**
	 * Gets the value.
	 * @return The value.
	 * */
	public int getValue() {
		return value;
	}
	
	/**
	 * Checks if its stronger than an other strategy.
	 * @param other The other strategy.
	 * @return true if its stronger than the other strategy.
	 * */
	public boolean isStrongerThan(StrategyStrength other) {
		if(other == null) {
			return true;
		}
		return (strength.isStronger(other.strength)) ?  true : (strength.isEquals(other.strength) ? value > other.value : false);
	}
	
	@Override
	public String toString() {
		return "StrategyStrength [strength=" + strength + ", value=" + value + "]";
	}
}
