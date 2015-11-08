package de.mq.util.event;

import org.junit.Assert;
import org.junit.Test;

public class EventTest {
	
	private static final String EVENT_ID = "Kylie is nice";
	final Event<String, Void> event =  () -> EVENT_ID;
	
	@Test
	public final void parameter() {
		Assert.assertTrue(event.parameter().isEmpty());
	}
	
	@Test
	public final void result() {
		Assert.assertFalse(event.result().isPresent());
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public final void assign() {
		event.assign(null);
	}

}
