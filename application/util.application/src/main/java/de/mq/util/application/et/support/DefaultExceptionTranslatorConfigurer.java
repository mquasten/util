package de.mq.util.application.et.support;

import java.util.List;

import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;


public class DefaultExceptionTranslatorConfigurer extends ExceptionTranslatorConfigurer {

    protected ExceptionMappings mappings;

    public DefaultExceptionTranslatorConfigurer() {
        this(null);
    }

    DefaultExceptionTranslatorConfigurer(ExceptionMappings mappings) {
        this.mappings = mappings;
        if (mappings == null) {
            this.mappings = new ExceptionMappings(null);
        }
    }



    @Override
    protected ExceptionMappingConfigurer translate(List<Class<? extends Exception>> sources) {
        return new DefaultExceptionMappingConfigurer(this.mappings, sources);
    }

    @Override
    public ExceptionTranslator done() {
        return new DefaultExceptionTranslator(mappings);
    }

}
