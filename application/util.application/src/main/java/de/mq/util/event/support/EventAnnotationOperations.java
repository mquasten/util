package de.mq.util.event.support;

import java.lang.reflect.Method;

interface EventAnnotationOperations {

	public abstract Object valueFromAnnotation(Method method);

	boolean  isAnnotaionPresent (final Method method);


}