package de.mq.util.application.et.support;

public class FooRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FooRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FooRuntimeException(String message) {
        super(message);
    }
}
