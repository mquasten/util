package de.mq.util.event.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
class BeanContainerTemplate implements BeanContainerOperations {

	private final ApplicationContext ctx;

	@Autowired
	BeanContainerTemplate(final ApplicationContext ctx) {
		this.ctx = ctx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.util.support.BeanContainerOperations#
	 * resolveMandatoryBeansFromDefaultOrContainer(java.util.Map,
	 * java.lang.Class[])
	 */
	@Override
	public final Object[] resolveMandatoryBeansFromDefaultOrContainer(final Map<Class<?>, Object> defaults, final Class<?>[] targets) {
		Assert.notNull(targets);
		Assert.notNull(defaults);
		final List<Object> results = new ArrayList<>();

		Arrays.asList(targets).forEach(target -> results.add(defaults.containsKey(target) ? defaults.get(target) : requiredSingelBean(target)));

		return results.toArray(new Object[results.size()]);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.util.support.BeanContainerOperations#requiredSingelBean
	 * (java.lang.Class)
	 */
	@Override
	public final <T> T requiredSingelBean(Class<? extends T> target) {
		return ctx.getBean(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.util.support.BeanContainerOperations#beansForFilter(
	 * de.mq.merchandise.util.support.BeanContainerOperations.BeanFilter)
	 */
	@Override
	public final <T> Collection<T> beansForFilter(final BeanFilter<T> filter) {
		return filter.filter(ctx);
	}

}
