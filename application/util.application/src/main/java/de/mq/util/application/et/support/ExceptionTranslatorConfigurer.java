package de.mq.util.application.et.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mq.util.application.et.ExceptionTranslatorOperations.ExceptionTranslator;

public abstract class ExceptionTranslatorConfigurer {

    @SafeVarargs
    public final ExceptionMappingConfigurer translate(Class<? extends Exception>... sources) {
        List<Class<? extends Exception>> sourceList = new ArrayList<>(sources.length);
        Collections.addAll(sourceList, sources);
        return this.translate(sourceList);
    }

    protected abstract ExceptionMappingConfigurer translate(List<Class<? extends Exception>> sources);

    public abstract ExceptionTranslator done();

}
