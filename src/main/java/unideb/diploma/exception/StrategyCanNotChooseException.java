package unideb.diploma.exception;

/**
 * Exception for strategies.
 * If a strategy get the control, but could not make a move, it throws StrategyCanNotChooseException.
 * */
public class StrategyCanNotChooseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param message The message of the exception.
	 * */
	public StrategyCanNotChooseException(String message) {
		super(message);
	}
	
}
