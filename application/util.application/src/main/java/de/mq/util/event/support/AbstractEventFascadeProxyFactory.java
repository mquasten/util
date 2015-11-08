package de.mq.util.event.support;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ObjectUtils;

import de.mq.util.event.Event;
import de.mq.util.event.EventBuilder;
import de.mq.util.event.EventFascadeProxyFactory;

abstract class AbstractEventFascadeProxyFactory implements EventFascadeProxyFactory {

	private final ApplicationEventPublisher applicationEventPublisher;

	private final EventAnnotationOperations eventAnnotationOperations;

	AbstractEventFascadeProxyFactory(final ApplicationEventPublisher applicationEventPublisher, final EventAnnotationOperations eventAnnotationOperations) {
		this.applicationEventPublisher = applicationEventPublisher;
		this.eventAnnotationOperations = eventAnnotationOperations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.util.support.EventFascadeProxyFactory#createProxy(java
	 * .lang.Class)
	 */
	@Override
	public abstract <T> T createProxy(Class<? extends T> targetClass);

	Object invoke(final Method method, final Object[] args) {
		final Object eventId = valueFromAnnotation(method);

		final EventBuilder<?, ?> builder = EventBuilder.of(eventId, Object.class);

		IntStream.range(0, ObjectUtils.toObjectArray(args).length).forEach(i -> builder.withParameter(method.getParameterTypes()[i], args[i]));

		final Event<?, ?> event = builder.build();

		applicationEventPublisher.publishEvent(event);

		return event.result().isPresent() ? event.result().get() : null;
	}

	boolean isAnnotated(final Method method) {
		return eventAnnotationOperations.isAnnotaionPresent(method);
	}

	private Object valueFromAnnotation(final Method method) {
		return eventAnnotationOperations.valueFromAnnotation(method);
	}

}