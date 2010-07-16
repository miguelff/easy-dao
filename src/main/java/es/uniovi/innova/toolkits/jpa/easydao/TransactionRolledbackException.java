package es.uniovi.innova.toolkits.jpa.easydao;

/**
 * Exception thrown when an active transaction cannot be commited,
 * and it's rolled back.
 * 
 * @author miguelff
 *
 */
public class TransactionRolledbackException extends RuntimeException {

	private static final long serialVersionUID = -440347087527708668L;

	public TransactionRolledbackException() {}

	public TransactionRolledbackException(String message) {
		super(message);
	}

	public TransactionRolledbackException(Throwable cause) {
		super(cause);
	}

	public TransactionRolledbackException(String message, Throwable cause) {
		super(message, cause);
	}

}
