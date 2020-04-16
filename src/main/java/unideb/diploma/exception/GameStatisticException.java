package unideb.diploma.exception;

public class GameStatisticException extends RuntimeException {

	private static final long serialVersionUID = -1704747066533081025L;
	
	public GameStatisticException() {
		super();
	}

	public GameStatisticException(String message) {
		super(message);
	}
	
	public GameStatisticException(String message, Throwable exception) {
		super(message, exception);
	}
}
