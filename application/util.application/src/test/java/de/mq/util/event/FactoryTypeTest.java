package de.mq.util.event;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import de.mq.util.event.EventFascadeProxyFactory.FactoryType;



public class FactoryTypeTest {
	
	@Test
	public final void factoryType() {
		Assert.assertEquals(2, FactoryType.values().length);
		Arrays.asList( FactoryType.values()).stream().forEach(value -> Assert.assertEquals(value, FactoryType.valueOf(value.name())));
	}

}
