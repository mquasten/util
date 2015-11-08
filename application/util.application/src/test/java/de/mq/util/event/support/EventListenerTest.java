package de.mq.util.event.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.util.event.Event;
import de.mq.util.event.Observable;
import de.mq.util.event.support.BeanContainerOperations.BeanFilter;

public class EventListenerTest {

	private static final String EVENT_SUBJECT = "eventforSubject";

	private static final String SAVE_METHOD = "save";

	private static final Long ID = 19680528L;

	private static final String CHANGE_SUBJECT_METHOD = "subject";
	private static final String TARGETS_FIELDS_NAME = "targets";
	private static final String METHODS_FIELD_NAME = "methods";


	BeanContainerOperations beanContainerOperations = Mockito.mock(BeanContainerOperations.class);

	private final EventAnnotationOperations eventAnnotationOperations = Mockito.mock(EventAnnotationOperations.class);

	private final SimpleEventListenerImpl eventListener = new SimpleEventListenerImpl(eventAnnotationOperations, beanContainerOperations);


	@SuppressWarnings("unchecked")
	final Observable<Long> subject = Mockito.mock(Observable.class);
	@SuppressWarnings("unchecked")
	final Event<String, Observable<Long>> event = Mockito.mock(Event.class);
	

	
	private ApplicationContext ctx = Mockito.mock(ApplicationContext.class);
	
	@Test
	public final void init() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
		final Map<String, Object> controllers = new HashMap<>();
		final Object controller = new TestController(subject);
		controllers.put(controller.getClass().getSimpleName(), controller);
		final Method result = controller.getClass().getDeclaredMethod(CHANGE_SUBJECT_METHOD, Long.class);
	
		Mockito.when(eventAnnotationOperations.isAnnotaionPresent(result)).thenReturn(true);
		Mockito.when(eventAnnotationOperations.valueFromAnnotation(result)).thenReturn(EVENT_SUBJECT);
		Mockito.when(ctx.getBeansWithAnnotation(Controller.class)).thenReturn(controllers);
		
		Mockito.doAnswer(i -> { 
			return ((BeanFilter<?>) i.getArguments()[0]).filter(ctx);
		}).when(beanContainerOperations).beansForFilter((BeanFilter<?>) Mockito.any(BeanContainerOperations.BeanFilter.class));
		
	//	Mockito.when(beanContainerOperations.beansForFilter( beanFilterMock())).thenReturn( controllers);
	
		eventListener.init();
		
		

		final Map<Object, Method> methods = methods();
		final Map<Object, Object> targets = targets();

		Assert.assertEquals(1, methods.size());
		Assert.assertEquals(1, targets.size());
		Assert.assertEquals(EVENT_SUBJECT, methods.keySet().iterator().next());
		Assert.assertEquals(EVENT_SUBJECT, targets.keySet().iterator().next());
		Assert.assertEquals(result, methods.get(EVENT_SUBJECT));
		Assert.assertEquals(controller, targets.get(EVENT_SUBJECT));
		
	

	}

	

	@SuppressWarnings("unchecked")
	private Map<Object, Object> targets() {
		return (Map<Object, Object>) ReflectionTestUtils.getField(eventListener, TARGETS_FIELDS_NAME);
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Method> methods() {
		return (Map<Object, Method>) ReflectionTestUtils.getField(eventListener, METHODS_FIELD_NAME);
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void processEvent() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
		final Object controller = new TestController(subject);
		final Method method = controller.getClass().getDeclaredMethod(CHANGE_SUBJECT_METHOD, Long.class);
		methods().put(EVENT_SUBJECT, method);
		targets().put(EVENT_SUBJECT, controller);
		Mockito.when(event.id()).thenReturn(EVENT_SUBJECT);
		final Map<Class<?>, Object> params = new HashMap<>();
		params.put(Long.class, ID);
		Mockito.when(event.parameter()).thenReturn(params);

		
		ArgumentCaptor<Map<Class<?>,Object>> defaultsCaptor = (ArgumentCaptor<Map<Class<?>, Object>>) ArgumentCaptor.forClass((Class<?>) Map.class) ;
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class[]> typesCaptor =  ArgumentCaptor.forClass(Class[].class) ;
		
		Mockito.when(beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(defaultsCaptor.capture(), typesCaptor.capture())).thenReturn(new Object[] {ID});
		
	
		eventListener.processEvent(event);

		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Observable> captor = ArgumentCaptor.forClass(Observable.class);
		Mockito.verify(event).assign(captor.capture());
		Assert.assertEquals(subject, captor.getValue());
		
		Assert.assertEquals(1, ((Map<?,?>)defaultsCaptor.getValue()).size());
		Assert.assertEquals(ID, ((Map<?,?>)defaultsCaptor.getValue()).get(Long.class));
		
		Assert.assertTrue(Arrays.asList((Class[])typesCaptor.getValue()).stream().findFirst().isPresent());
		Assert.assertEquals(Long.class, Arrays.asList((Class[])typesCaptor.getValue()).stream().findFirst().get());

	}

	@Test
	public final void processEventVoidMethod() throws BeanInstantiationException, NoSuchMethodException, SecurityException {
	
		final TestController controller = new TestController(subject);
		final Method method = controller.getClass().getDeclaredMethod(SAVE_METHOD, Observable.class);
		methods().put(EVENT_SUBJECT, method);
		targets().put(EVENT_SUBJECT, controller);

		Mockito.when(event.id()).thenReturn(EVENT_SUBJECT);
		
		@SuppressWarnings("unchecked")
		ArgumentCaptor<Map<Class<?>,Object>> defaultsCaptor = (ArgumentCaptor<Map<Class<?>, Object>>) ArgumentCaptor.forClass((Class<?>) Map.class) ;
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class[]> typesCaptor =  ArgumentCaptor.forClass(Class[].class) ;
	
		Mockito.when(beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(defaultsCaptor.capture(), typesCaptor.capture())).thenReturn(new Object[] {subject});
		
		eventListener.processEvent(event);

		Mockito.verify(event, Mockito.times(0)).assign(Mockito.any());
		Mockito.verify(subject).notifyObservers(19680528L);
		
		Assert.assertEquals(0, ((Map<?,?>)defaultsCaptor.getValue()).size());
		
		Assert.assertTrue(Arrays.asList((Class[])typesCaptor.getValue()).stream().findFirst().isPresent());
		Assert.assertEquals(Observable.class, Arrays.asList((Class[])typesCaptor.getValue()).stream().findFirst().get());
	
	}

}

class TestController {

	private static final long ID = 19680528L;
	private final Observable<Long> subject;

	TestController(final Observable<Long> subject) {
		this.subject = subject;
	}

	@Qualifier
	void save(final Observable<Long> subject) {
		subject.notifyObservers(ID);
	}

	@Qualifier()
	Observable<Long> subject(final Long id) {
		return subject;

	}

}
