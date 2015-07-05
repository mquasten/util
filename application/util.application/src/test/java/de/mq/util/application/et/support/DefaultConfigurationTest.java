package de.mq.util.application.et.support;





import de.mq.util.application.et.ExceptionTranslatorOperations.ET;
import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DefaultConfigurationTest {

    private ExceptionTranslator et = ET.newConfiguration().done();
    private Exception checkedException = new IOException("checked exception", null);
    private RuntimeException runtimeException = new NullPointerException("runtime exception");


    @Test
    public void withTranslationTranslatesCheckedExceptionsToRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withTranslation(() -> {
                throw checkedException;
            });
        });
        
        Assert.assertEquals("checked exception", result.getMessage());
        Assert.assertEquals(checkedException, result.getCause());
        //expect(result.getMessage()).toEqual("checked exception");
        //expect(result.getCause()).toEqual(checkedException);
    }

    @Test
    public void withTranslationDoesNothingIfNoExceptionIsThrown() {
        et.withTranslation(() -> {
            // empty
        });
    }

    @Test
    public void withTranslationDoesNotTranslateRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withTranslation(() -> {
                throw runtimeException;
            });
        });
        Assert.assertEquals(runtimeException, result);
       // expect(result).toEqual(runtimeException);
    }

    @Test
    public void withReturningTranslationTranslatesCheckedExceptionsToRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withReturningTranslation(() -> {
                throw checkedException;
            });
        });
        Assert.assertEquals("checked exception", result.getMessage());
        Assert.assertEquals(checkedException, result.getCause());
        //expect(result.getMessage()).toEqual("checked exception");
        //expect(result.getCause()).toEqual(checkedException);
    }

    @Test
    public void withReturningTranslationReturnsTheValueIfNoExceptionIsThrown() {
        final String result = et.withReturningTranslation(() -> "foo");
        Assert.assertEquals("foo", result);
        //expect(result).toEqual("foo");
    }

    @Test
    public void withReturningTranslationDoesNotTranslateRuntimeExceptions() {
        RuntimeException result = TestUtil.catchException(() -> {
            et.withReturningTranslation(() -> {
                throw runtimeException;
            });
        });
        Assert.assertEquals(runtimeException, result);
        //expect(result).toEqual(runtimeException);
    }

    @Test
    public void withTranslationFailsIfNullIsPassed() {
        final RuntimeException result = TestUtil.catchException(() -> et.withTranslation(null));
        Assert.assertEquals(IllegalArgumentException.class, result.getClass());
        Assert.assertEquals("null is not a valid argument for ET.withTranslation()", result.getMessage());
        //expect(result.getClass()).toEqual(IllegalArgumentException.class);
        //expect(result.getMessage()).toEqual("null is not a valid argument for ET.withTranslation()");
    }

    @Test
    public void withReturningTranslationFailsIfNullIsPassed() {
        final RuntimeException result = TestUtil.catchException(() -> et.withReturningTranslation(null));
        Assert.assertEquals(IllegalArgumentException.class, result.getClass());
        Assert.assertEquals("null is not a valid argument for ET.withReturningTranslation()", result.getMessage());
        //expect(result.getClass()).toEqual(IllegalArgumentException.class);
        //expect(result.getMessage()).toEqual("null is not a valid argument for ET.withReturningTranslation()");
    }

}
