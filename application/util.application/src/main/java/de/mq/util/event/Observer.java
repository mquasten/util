package de.mq.util.event;

@FunctionalInterface
public interface Observer<EventType> {

	void process(final EventType event);

}