package duson.java.solutionConf.dubbox.filter;

public class CreditException extends RuntimeException {
	public CreditException() {
	}

	public CreditException(String message) {
		super(message);
	}

	public CreditException(Throwable cause) {
		super(cause);
	}

	public CreditException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
	}
}
