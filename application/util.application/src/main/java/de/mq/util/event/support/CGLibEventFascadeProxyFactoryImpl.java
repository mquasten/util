package de.mq.util.event.support;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import de.mq.util.event.EventFascadeProxyFactory;



@Component
@EventFascadeProxyFactory.EventFascadeProxyFactoryQualifier(EventFascadeProxyFactory.FactoryType.CGLib)
class CGLibEventFascadeProxyFactoryImpl extends AbstractEventFascadeProxyFactory {

	private Map<Class<?>, Factory> proxies = new HashMap<Class<?>, Factory>();	

	@Autowired
	CGLibEventFascadeProxyFactoryImpl(final ApplicationEventPublisher applicationEventPublisher, final EventAnnotationOperations eventAnnotationOperations) {
		super(applicationEventPublisher, eventAnnotationOperations);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.util.support.EventFascadeProxyFactory#createProxy(java.lang.Class)
	 */
	@Override
	public final <T> T createProxy(Class<? extends T> targetClass) {
		return proxy(targetClass);
	}

	@SuppressWarnings("unchecked")
	private synchronized <T> T proxy(Class<? extends T> targetClass) {
		if (!proxies.containsKey(targetClass)) {
			proxies.put(targetClass, (Factory) enhancer(targetClass).create());
		}
		return (T) proxies.get(targetClass);
	}

	private <T> Enhancer enhancer(Class<? extends T> targetClass) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetClass);
		enhancer.setCallbackFilter(method -> isAnnotated(method) ? 1 : 0);
		enhancer.setCallbacks(new Callback[] { (MethodInterceptor) (obj, method, args, proxy) -> ReflectionUtils.invokeMethod(method, proxy, args), (MethodInterceptor) (obj, method, args, proxy) -> invoke(method, args) });
		return enhancer;
	}

}
