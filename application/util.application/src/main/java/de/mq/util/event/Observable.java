package de.mq.util.event;

public interface Observable<EventType> {

	void register(final Observer<EventType> observer, final EventType event);

	void remove(final Observer<EventType> observer, final EventType event);

	void remove(final Observer<EventType> observer);

	void notifyObservers(final EventType event);

}