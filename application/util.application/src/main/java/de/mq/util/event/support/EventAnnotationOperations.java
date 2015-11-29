package de.mq.util.event.support;

import java.lang.reflect.Method;

public interface EventAnnotationOperations {

	public abstract Object valueFromAnnotation(Method method);

	boolean  isAnnotaionPresent (final Method method);


}