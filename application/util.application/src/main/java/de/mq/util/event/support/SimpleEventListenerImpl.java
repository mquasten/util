package de.mq.util.event.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.util.event.Event;

@Controller()
class SimpleEventListenerImpl {

	private final Map<Object, Method> methods = new HashMap<>();
	private final Map<Object, Object> targets = new HashMap<>();

	private final EventAnnotationOperations eventAnnotationOperations;

	private BeanContainerOperations beanContainerOperations;

	@Autowired
	SimpleEventListenerImpl(final EventAnnotationOperations eventAnnotationOperations, final BeanContainerOperations beanContainerOperations) {
		this.eventAnnotationOperations = eventAnnotationOperations;
		this.beanContainerOperations = beanContainerOperations;

	}

	@PostConstruct
	void init() {
		beanContainerOperations.beansForFilter(ctx -> ctx.getBeansWithAnnotation(Controller.class).values()).stream().collect(Collectors.toSet()).forEach(obj -> Arrays.asList(obj.getClass().getDeclaredMethods()).stream().filter(m -> eventAnnotationOperations.isAnnotaionPresent(m)).forEach(m -> handleEvent(obj, m)));
		;
	}

	@SuppressWarnings("unchecked")
	@EventListener
	<T, R> void processEvent(final Event<T, R> event) {
		final Method method = methods.get(event.id());
		final Object target = targets.get(event.id());
		Assert.notNull(method, String.format("Method not found for event: %s.", event.id()));
		Assert.notNull(target, String.format("Target not found for event: %s", event.id()));
		method.setAccessible(true);
		final Object result = ReflectionUtils.invokeMethod(method, target, beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(event.parameter(), method.getParameterTypes()));

		if (result == null) {
			return;
		}
		event.assign((R) result);

	}

	private void handleEvent(Object obj, Method m) {
		final Object event = eventAnnotationOperations.valueFromAnnotation(m);
		methods.put(event, m);
		targets.put(event, obj);
	}

}
