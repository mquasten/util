package de.mq.util.application.et.support;

public class FooChildException extends FooException {
	private static final long serialVersionUID = 1L;

	public FooChildException(String message) {
        super(message);
    }

    public FooChildException(String message, Throwable cause) {
        super(message, cause);
    }
}
