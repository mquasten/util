package de.mq.util.event.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;




import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

public class EventAnnotationOperationTest {

	private static final String METHOD_NAME = "calculateHotScrore";
	static final String ANNOTATION_VALUE = METHOD_NAME;
	private EventAnnotationOperations eventAnnotationOperations = new EventAnnotationTempalte(Arrays.asList(Qualifier.class));
	private final Method method = ReflectionUtils.findMethod(ArtistController.class, METHOD_NAME, Long.class);
	@SuppressWarnings("unchecked")
	private final Set<Class<? extends Annotation>> qualifiers = (Set<Class<? extends Annotation>>) ReflectionTestUtils.getField(eventAnnotationOperations, "qualifiers");;

	@Before
	public void setup() {

		qualifiers.clear();
		qualifiers.add(Qualifier.class);
	}

	@Test
	public void valueFromAnnotation() {
		Assert.assertEquals(ANNOTATION_VALUE, eventAnnotationOperations.valueFromAnnotation(method));
	}

	@Test
	public void valueFromAnnotationMissindAnnotation() {
		qualifiers.clear();
		Assert.assertFalse(eventAnnotationOperations.isAnnotaionPresent(method));
	}

	@Test
	public void isAnnotaionPresent() {
		Assert.assertTrue(eventAnnotationOperations.isAnnotaionPresent(method));
	}

	@Test(expected=IllegalArgumentException.class)
	public final void wrongAnnotation() {

		qualifiers.clear();
		
		qualifiers.add(Annotation.class);

		final Method method = ReflectionUtils.findMethod(ArtistController.class, METHOD_NAME, Long.class);
		eventAnnotationOperations.valueFromAnnotation(method);
	}

}

abstract class ArtistController {

	@Qualifier(EventAnnotationOperationTest.ANNOTATION_VALUE)
	abstract int calculateHotScrore(Long artistId);

}