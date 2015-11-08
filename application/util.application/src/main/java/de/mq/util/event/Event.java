package de.mq.util.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface Event<T,R> {
	
	 T id();
	 
	default  Map<Class<?>, Object> parameter() {
		return new HashMap<>();
	}
	
	default Optional<R>  result() {
		return Optional.empty();
	}
	
	default  void  assign(R result) {
		throw new UnsupportedOperationException("Results not supported for this event");
	}

}
