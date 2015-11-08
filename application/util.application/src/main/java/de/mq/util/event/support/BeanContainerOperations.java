package de.mq.util.event.support;

import java.util.Collection;
import java.util.Map;

import org.springframework.context.ApplicationContext;

public interface BeanContainerOperations {

	interface BeanFilter<T> {
		Collection<T> filter(final ApplicationContext ctx);
	}

	Object[] resolveMandatoryBeansFromDefaultOrContainer(final Map<Class<?>, Object> defaults, final Class<?>[] targets);

	<T> T requiredSingelBean(Class<? extends T> target);

	<T> Collection<T> beansForFilter(final BeanFilter<T> filter);

}
