package de.mq.util.application.et.support;

public class BarRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BarRuntimeException(String message) {
        super(message);
    }

    public BarRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
