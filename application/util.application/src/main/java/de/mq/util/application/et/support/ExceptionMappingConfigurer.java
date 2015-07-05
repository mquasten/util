package de.mq.util.application.et.support;

public interface ExceptionMappingConfigurer {

    ExceptionTranslatorConfigurer to(Class<? extends RuntimeException> targetException);

    ExceptionTranslatorConfigurer using(TargetExceptionResolver resolver);

}
