package fr.shyrogan.publisher4k.scanner

import fr.shyrogan.publisher4k.subscription.Subscription
import java.lang.reflect.AnnotatedElement

/**
 * A [SubscriptionScanner] is invoked by the [Publisher] when an object is registered,
 * it allows the exploration of objects to get their [Subscription] inside.
 */
interface SubscriptionScanner {

    /**
     * Applies this factory to the [element] and returns a potential [Subscription]
     */
    fun <O: Any> apply(element: AnnotatedElement, instance: O): Subscription<Any>?

}