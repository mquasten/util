package de.mq.util.application.et.support;

public class BarException extends Exception {

	private static final long serialVersionUID = 1L;

	public BarException(String message) {
        super(message);
    }

    public BarException(String message, Throwable cause) {
        super(message, cause);
    }
}
