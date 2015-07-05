package de.mq.util.application.et.support;

public class FooException extends Exception {

	private static final long serialVersionUID = 1L;

	public FooException(String message) {
        super(message);
    }

    public FooException(String message, Throwable cause) {
        super(message, cause);
    }
}
