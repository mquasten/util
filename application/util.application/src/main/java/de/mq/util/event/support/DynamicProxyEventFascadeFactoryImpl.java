package de.mq.util.event.support;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import de.mq.util.event.EventFascadeProxyFactory;

@Component
@EventFascadeProxyFactory.EventFascadeProxyFactoryQualifier(EventFascadeProxyFactory.FactoryType.DynamicProxy)
class DynamicProxyEventFascadeFactoryImpl extends AbstractEventFascadeProxyFactory {

	@Autowired
	DynamicProxyEventFascadeFactoryImpl(final ApplicationEventPublisher applicationEventPublisher, final EventAnnotationOperations eventAnnotationOperations) {
		super(applicationEventPublisher, eventAnnotationOperations);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T createProxy(final Class<? extends T> targetClass) {

		return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] { targetClass }, (object, method, args) -> {

			if (!super.isAnnotated(method)) {
				return method.invoke(Proxy.getInvocationHandler(object), args);
			}

			return invoke(method, args);

		});
	}

}
