package de.mq.util.event;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.Assert;

public final class EventBuilder<T, R> {

	private final T type;

	private final Map<Class<?>, Object> parameters = new HashMap<>();

	private EventBuilder(final T type) {
		this.type = type;
	}

	public final static <T, R> EventBuilder<T, R> of(final T type, final Class<? extends R> clazz) {

		return new EventBuilder<T, R>(type);
	}

	public final static <T, R> EventBuilder<T, Void> of(final T type) {

		return new EventBuilder<T, Void>(type);
	}

	public final EventBuilder<T, R> withParameter(final Object parameter) {
		parameters.put(parameter.getClass(), parameter);
		Arrays.asList(parameter.getClass().getInterfaces()).forEach(clazz -> parameters.put(clazz, parameter));
		return this;
	}

	public final EventBuilder<T, R> withParameter(final Class<?> clazz, final Object parameter) {
		parameters.put(clazz, parameter);
		return this;
	}

	public final Event<T, R> build() {
		Assert.notNull(type, "Type is mandatory");
		return new Event<T, R>() {

			private R result;

			@Override
			public T id() {
				return type;
			}

			@Override
			public Map<Class<?>, Object> parameter() {
				return parameters;
			}

			@Override
			public Optional<R> result() {
				return Optional.ofNullable(result);
			}

			@Override
			public void assign(final R result) {
				this.result = result;
			}

			@Override
			public int hashCode() {
				return type.hashCode();
			}

			@Override
			public boolean equals(final Object obj) {
				if (!(obj instanceof Event)) {
					return super.equals(obj);
				}
				return type.equals(((Event<?, ?>) obj).id());
			}

			@Override
			public String toString() {
				return id().toString();
			}

		};

	}

}
