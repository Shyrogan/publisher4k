package fr.shyrogan.publisher4k.subscription

import java.lang.reflect.AnnotatedElement

/**
 * A [Subscription] represents to publisher4k an object that can be called, it's used to contains the information
 * required to order and call correctly the [Listener].
 */
data class Subscription<T: Any>(val priority: Int, val topic: Class<out T>,val listener: Listener<T>,
                                val parent: AnnotatedElement? = null) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Subscription<*>

        if(parent != null && parent == other.parent)
            return topic == other.topic

        return topic == other.topic
                && listener == other.listener
    }

    override fun hashCode(): Int {
        var result = priority
        result = 31 * result + topic.hashCode()
        result = 31 * result + listener.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }
}

/**
 * Creates a [Subscription] instance with [listener] and a priority equals to the default priority
 * which is `0`.
 */
inline fun <reified T: Any> subscription(listener: Listener<T>): Subscription<T> {
    return Subscription(0, T::class.java, listener, null)
}

/**
 * Creates a [Subscription] instance with [listener] and a priority of [priority] allowing
 * your [listener] to receive the event before another one.
 */
inline fun <reified T: Any> subscription(priority: Int, listener: Listener<T>): Subscription<T> {
    return Subscription(priority, T::class.java, listener, null)
}