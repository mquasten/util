package de.mq.util.event;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

@FunctionalInterface
public interface EventFascadeProxyFactory {
	
	@Retention(RetentionPolicy.RUNTIME)
	@Inherited
	@Qualifier
	public @interface    EventFascadeProxyFactoryQualifier {
		FactoryType value() default FactoryType.CGLib;
	}
	
	public enum FactoryType {
		CGLib,
		DynamicProxy;
	}


	

	<T> T createProxy(Class<? extends T> targetClass);

}