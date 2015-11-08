package de.mq.util.event.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;






import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import de.mq.util.event.Observable;
import de.mq.util.event.support.BeanContainerOperations.BeanFilter;

public class BeanContainerOperationsTest {

	private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);

	private final BeanContainerOperations beanContainerOperations = new BeanContainerTemplate(applicationContext);

	private final BeanFilter<?> beanFilter = Mockito.mock(BeanFilter.class);

	final Observable<?> subject = Mockito.mock(Observable.class);

	@Test
	public void requiredSingelBean() {

		Mockito.when(beanContainerOperations.requiredSingelBean(Observable.class)).thenReturn(subject);

		Assert.assertEquals(subject, beanContainerOperations.requiredSingelBean((Observable.class)));
	}

	@Test
	public void beansForFilter() {
		@SuppressWarnings("rawtypes")
		final Map<String, Observable> beans = new HashMap<>();
		beans.put(subject.getClass().getSimpleName(), subject);
		Mockito.when(applicationContext.getBeansOfType(Observable.class)).thenReturn(beans);
		Mockito.doAnswer(i -> {
			return ((ApplicationContext) i.getArguments()[0]).getBeansOfType(Observable.class).values();
		}).when(beanFilter).filter(Mockito.any(ApplicationContext.class));

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Collection<Observable> results = (Collection<Observable>) beanContainerOperations.beansForFilter(beanFilter);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(subject, results.stream().findFirst().get());
		
	}

	@Test
	public void resolveMandatoryBeansFromDefaultOrContainer() {
		@SuppressWarnings("rawtypes")
		final Observable defaultSubject = Mockito.mock(Observable.class);
		final Map<Class<?>, Object> beans = new HashMap<>();
		beans.put(Observable.class, defaultSubject);
		Mockito.when(applicationContext.getBean(Observable.class)).thenReturn(subject);
		final Object[] results = beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(beans, new Class[] { Observable.class });
		Assert.assertEquals(1, results.length);
		Assert.assertEquals(defaultSubject, results[0]);
	}

	@Test
	public void resolveMandatoryBeansFromDefaultOrContainerNoDefault() {

		Mockito.when(applicationContext.getBean(Observable.class)).thenReturn(subject);
		final Object[] results = beanContainerOperations.resolveMandatoryBeansFromDefaultOrContainer(new HashMap<>(), new Class[] { Observable.class });
		Assert.assertEquals(1, results.length);
		Assert.assertEquals(subject, results[0]);
	}

}
