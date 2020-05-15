package unideb.diploma.domain;

/**
 * Position.
 * */
public class Position {
	/**
	 * The row.
	 * */
	private final int x;
	
	/**
	 * The column.
	 * */
	private final int y;
	
	/**
	 * Constructor.
	 * @param x The row.
	 * @param y The column.
	 * */
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x.
	 * @return x.
	 * */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y.
	 * @return y.
	 * */
	public int getY() {
		return y;
	}

	/**
	 * Cloning the position.
	 * @return Copy of the position.
	 * */
	public Position clone() {
		return new Position(x, y);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Position)) {
			return false;
		}
		Position other = (Position) obj;
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
	
}
