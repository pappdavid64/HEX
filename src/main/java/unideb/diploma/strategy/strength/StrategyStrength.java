package unideb.diploma.strategy.strength;

public class StrategyStrength {
	
	public static StrategyStrength veryWeak(int value) {
		return of(Strength.VERY_WEAK, value);
	}
	
	public static StrategyStrength weak(int value) {
		return of(Strength.WEAK, value);
	}
	
	public static StrategyStrength medium(int value) {
		return of(Strength.MEDIUM, value);
	}
	
	public static StrategyStrength strong(int value) {
		return of(Strength.STRONG, value);
	}
	
	public static StrategyStrength veryStrong(int value) {
		return of(Strength.VERY_STRONG, value);
	}
	
	private static StrategyStrength of(Strength strength, int value) {
		return new StrategyStrength(strength, value);
	}
	
	
	private Strength strength;
	private int value;

	private StrategyStrength(Strength strength, int value) {
		this.strength = strength;
		this.value = value;
	}
	
	public Strength getStrength() {
		return strength;
	}
	
	public int getValue() {
		return value;
	}
	
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
