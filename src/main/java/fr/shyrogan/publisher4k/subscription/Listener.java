package fr.shyrogan.publisher4k.subscription;

/**
 * Represents an object able to receive an event of type {@link T}, this object is coded
 * in Java because Kotlin's object `(T) -> Unit` is way slower for some reason..
 *
 * Using this interface will allow you to operate faster in case if there are massive amounts of listeners.
 *
 * @param <T> Event type
 */
public interface Listener<T> {

    /**
     * Method executed when this listener receives event with specified
     * event type.
     *
     * @param event Event type
     */
    void receive(T event);

}
