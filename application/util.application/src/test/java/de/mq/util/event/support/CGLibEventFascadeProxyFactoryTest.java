package de.mq.util.event.support;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;





import net.sf.cglib.proxy.MethodProxy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ReflectionUtils;

import de.mq.util.event.Event;
import de.mq.util.event.EventFascadeProxyFactory;
import de.mq.util.event.Observable;

public class CGLibEventFascadeProxyFactoryTest {

	static final String CHANGE_SUBJECT = "changeSubject";
	private static final long ID = 4711L;
	final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
	final EventAnnotationOperations eventAnnotationOperations = Mockito.mock(EventAnnotationOperations.class);
	@SuppressWarnings("rawtypes")
	final Observable subject = Mockito.mock(Observable.class);

	final EventFascadeProxyFactory eventFascadeProxyFactory = new CGLibEventFascadeProxyFactoryImpl(applicationEventPublisher, eventAnnotationOperations);

	@Before
	public void setup() {
		final Method method = ReflectionUtils.findMethod(TestFascade.class, CHANGE_SUBJECT, Long.class);

		final Qualifier annotation = method.getAnnotation(Qualifier.class);

		Mockito.when(eventAnnotationOperations.valueFromAnnotation(method)).thenReturn(annotation.value());

		Mockito.when(eventAnnotationOperations.isAnnotaionPresent(method)).thenReturn(true);
	}

	@Test
	public final void createProxy() {

		Mockito.doAnswer(i -> {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			final Event<String, Observable> event = (Event<String, Observable>) i.getArguments()[0];
			Assert.assertEquals(CHANGE_SUBJECT, event.id());
			final Map<Class<?>, Object> params = event.parameter();
			Assert.assertEquals(1, params.size());
			Assert.assertEquals(Long.class, params.keySet().iterator().next());
			Assert.assertEquals(ID, params.values().iterator().next());
			event.assign(subject);
			return null;
		}).when(applicationEventPublisher).publishEvent(Mockito.any(Event.class));

		TestFascade proxy = eventFascadeProxyFactory.createProxy(TestFascade.class);

		Assert.assertEquals(subject, proxy.changeSubject(ID));

		Assert.assertTrue(proxy == eventFascadeProxyFactory.createProxy(TestFascade.class));

		Assert.assertTrue(proxy.toString().startsWith(MethodProxy.class.getName()));

	}

	@Test
	public final void createProxyVoidResult() {

		final TestFascade proxy = eventFascadeProxyFactory.createProxy(TestFascade.class);
		Assert.assertNull(proxy.changeSubject(ID));

		@SuppressWarnings({ "rawtypes", "unchecked" })
		final ArgumentCaptor<Event<String, Void>> eventCaptor = ArgumentCaptor.forClass((Class) Event.class);
		Mockito.verify(applicationEventPublisher).publishEvent(eventCaptor.capture());

		Assert.assertEquals(CHANGE_SUBJECT, eventCaptor.getValue().id());
		Assert.assertEquals(1, eventCaptor.getValue().parameter().size());
		Assert.assertEquals(Long.class, eventCaptor.getValue().parameter().keySet().stream().findFirst().get());
		Assert.assertEquals(Optional.of(ID), eventCaptor.getValue().parameter().values().stream().findFirst());
	}

}

abstract class TestFascade {
	@SuppressWarnings("rawtypes")
	@Qualifier(CGLibEventFascadeProxyFactoryTest.CHANGE_SUBJECT)
	public abstract Observable changeSubject(Long id);
}