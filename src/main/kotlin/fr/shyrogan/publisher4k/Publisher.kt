package fr.shyrogan.publisher4k

import fr.shyrogan.publisher4k.scanner.SubscriptionScanner
import fr.shyrogan.publisher4k.scanner.default.SimpleFieldScanner
import fr.shyrogan.publisher4k.publisher.Dispatcher
import fr.shyrogan.publisher4k.subscription.Listener
import fr.shyrogan.publisher4k.subscription.Subscription
import java.lang.reflect.AnnotatedElement
import java.util.*

/**
 * The [Publisher] class is a class used by publisher4k to contain each [Dispatcher] and
 * call any event with the [publish] method.
 */
@Suppress("unchecked_cast")
open class Publisher @JvmOverloads
    constructor(private val scanners: Array<SubscriptionScanner> = DEFAULT_SCANNERS)
{

    private val publisherMap = mutableMapOf<Class<out Any>, Dispatcher<Any>>()
    private val subscribersCache = WeakHashMap<Any, MutableMap<Class<out Any>, List<Subscription<Any>>>>()

    private val taskPublishers = mutableMapOf<Class<out Any>, Dispatcher<Any>>()

    /**
     * Explores specified [object] and looks for subscriptions using each factory
     * then register each of them.
     */
    fun register(`object`: Any) {
        val objectClass = `object`.javaClass
        val cache = subscribersCache[`object`]
        if(cache == null) {
            val elements: Set<AnnotatedElement> = objectClass.declaredFields
                    .union(objectClass.declaredMethods.asList())

            elements
                    .mapNotNull {
                        scanners.map { scanner ->
                            scanner.apply(it, `object`)
                        }.firstOrNull()
                    }
                    .groupBy { it.topic }
                    .forEach { (topic, subscriptions) ->
                        // cache
                        val cache = subscribersCache
                                .getOrPut(`object`) { mutableMapOf() }
                        cache[topic] = subscriptions

                        // register
                        val dispatcher = publisherMap.getOrPut(topic) { Dispatcher() }
                        dispatcher.addAll(subscriptions)
                    }
            return
        }
        cache.forEach { (topic, subscriptions) ->
            publisherMap[topic]?.addAll(subscriptions)
        }
    }

    /**
     * Explores specified [object] and looks for subscriptions using each factory
     * then unregister each of them.
     */
    fun unregister(`object`: Any) {
        val cache = subscribersCache[`object`] ?: return
        cache.forEach { (topic, subscriptions) ->
            publisherMap[topic]?.removeAll(subscriptions)
        }
    }

    /**
     * Registers specified [subscription] as a task (one time call)
     */
    fun registerTask(subscription: Subscription<Any>) {
        taskPublishers.getOrPut(subscription.topic) { Dispatcher() }.add(subscription)
    }

    /**
     * Returns the [Dispatcher] instance for [topic]
     */
    operator fun <T: Any> get(topic: Class<T>): Dispatcher<T>? {
        return publisherMap[topic]
                as Dispatcher<T>?
    }

    /**
     * Publishes [topic] to each of its [Listener]
     */
    fun <T: Any> publish(topic: T): T {
        taskPublishers[topic.javaClass]?.apply {
            this as Dispatcher<T>
            listeners.forEach {
                it.receive(topic)
            }
            clear()
        }

        get(topic.javaClass)
                ?.listeners
                ?.forEach {
                    it.receive(topic)
                    if(topic is Cancellable && topic.isCancelled)
                        return topic
                }

        return topic
    }

    companion object {
        val DEFAULT_SCANNERS = arrayOf<SubscriptionScanner>(
            SimpleFieldScanner
        )
    }

}