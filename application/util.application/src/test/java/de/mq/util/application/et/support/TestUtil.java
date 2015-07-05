package de.mq.util.application.et.support;




import org.junit.Assert;

import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;


public class TestUtil {

    public static final String FOO_EXCEPTION_MESSAGE = "fooException";
    public static final String FOO_CHILD_EXCEPTION_MESSAGE = "fooChildException";
    public static final String BAR_EXCEPTION_MESSAGE = "barException";

    public static final Exception FOO_EXCEPTION = new FooException(FOO_EXCEPTION_MESSAGE, null);
    public static final Exception FOO_CHILD_EXCEPTION = new FooChildException(FOO_CHILD_EXCEPTION_MESSAGE, null);
    public static final RuntimeException FOO_RUNTIME_EXCEPTION = new FooRuntimeException("fooRuntimeException", null);
    public static final Exception BAR_EXCEPTION = new BarException(BAR_EXCEPTION_MESSAGE, null);



    public static RuntimeException catchException(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            return e;
        }
        throw new AssertionError("Runnable did not throw an exception");
    }

    public static void expectException(Exception e, Class<? extends Exception> type, String message, Throwable cause) {
   	
   	 Assert.assertEquals(type, e.getClass());
   	 Assert.assertEquals(message, e.getMessage());
   	 Assert.assertEquals(cause, e.getCause());
      
    }

    public static RuntimeException translateException(final ExceptionTranslator et, final Exception exception) {
        return TestUtil.catchException(() -> {
            et.withTranslation(() -> {
                throw exception;
            });
        });
    }
}
