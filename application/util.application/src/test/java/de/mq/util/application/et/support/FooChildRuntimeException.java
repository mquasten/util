package de.mq.util.application.et.support;


public class FooChildRuntimeException extends FooRuntimeException {

	private static final long serialVersionUID = 24880578674244383L;

	public FooChildRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FooChildRuntimeException(String message) {
        super(message);
    }
}
