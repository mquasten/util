package de.mq.util.event.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

class EventAnnotationTempalte implements EventAnnotationOperations {
	
	private Collection<Class<? extends Annotation>> qualifiers = new HashSet<>();
	
	private  final String valueMethodName = "value";
	
	EventAnnotationTempalte(Collection<Class<? extends Annotation>> qualifiers) {
		qualifiers.forEach(q -> add(q));	
	}
	
	


	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.support.EventAnnotationOperations#valueFromAnnotation(java.lang.reflect.Method)
	 */
	@Override
	public Object valueFromAnnotation(final Method method) {
		final Optional<Annotation> annotation = annotation(method);
		Assert.isTrue(annotation.isPresent(), "Method should be annotated, something is wrong.");

		final Method valueMethod = ReflectionUtils.findMethod(annotation.get().getClass(), valueMethodName);
		Assert.notNull(valueMethod, valueMethodName + " for Annotation not found");
		final Object eventId = ReflectionUtils.invokeMethod(valueMethod, annotation.get());
		return eventId;
	}


	private  Optional<Annotation> annotation(final Method method) {
		return qualifiers.stream().map(q -> (Annotation) AnnotationUtils.findAnnotation(method, q)).filter(a -> a != null).findFirst();
	
	
	}
	
	public void add(Class<? extends Annotation> qualifier){
		qualifiers.add(qualifier);
	}



	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.support.EventAnnotationOperations#isAnnotaionPresent(java.lang.reflect.Method)
	 */
	@Override
	public boolean isAnnotaionPresent(final Method method) {
		return annotation(method).isPresent();
	}

}
