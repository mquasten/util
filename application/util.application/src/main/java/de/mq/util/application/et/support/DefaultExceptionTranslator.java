package de.mq.util.application.et.support;


import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;
import de.mq.util.application.et.ExceptionTranslatorOperations.ReturningTryBlock;
import de.mq.util.application.et.ExceptionTranslatorOperations.TryBlock;



class DefaultExceptionTranslator implements ExceptionTranslator {

    protected ExceptionMappings exceptionMappings;

    DefaultExceptionTranslator(ExceptionMappings exceptionMappings) {
        this.exceptionMappings = exceptionMappings;
    }

    @Override
    public void withTranslation(TryBlock tryBlock) {
        Arguments.ensureNotNull(tryBlock, "null is not a valid argument for ET.withTranslation()");
        this.withReturningTranslation(() -> {
            tryBlock.run();
            return null;
        });
    }

    @Override
    public <T> T withReturningTranslation(ReturningTryBlock<T> invokable) {
        Arguments.ensureNotNull(invokable, "null is not a valid argument for ET.withReturningTranslation()");
        try {
            return invokable.run();
        } catch (Exception e) {
            throw this.getTargetException(e);
        }
    }

    @Override
    public ExceptionTranslatorConfigurer newConfiguration() {
        return new DefaultExceptionTranslatorConfigurer(new ExceptionMappings(this.exceptionMappings));
    }

    protected RuntimeException getTargetException(Exception source) {
        TargetExceptionResolver resolver = this.exceptionMappings.getExceptionResolver(source.getClass());
        RuntimeException targetException = resolver.getTargetException(source.getMessage(), source);
        if (targetException == null) {
            throw new TranslationException("TargetExceptionResolver returned null as target exception, " +
                    "targetExceptionResolver: " + resolver.getClass().getCanonicalName());
        }
        return targetException;
    }
}
