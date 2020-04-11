package fr.shyrogan.publisher4k.publisher

import fr.shyrogan.publisher4k.subscription.Listener
import fr.shyrogan.publisher4k.subscription.Subscription

/**
 * A [Dispatcher] is used by publisher4k to contain and manage listeners easily and
 * make EventBus' code cleaner.
 * Each event as its own [Dispatcher] instance managing various [Subscription].
 */
class Dispatcher<T: Any>(val subscriptionList: MutableList<Subscription<T>> = mutableListOf()) {

    /**
     * Contains the various
     */
    var listeners = sortedListenerArray(subscriptionList)

    /**
     * Adds specified [subscription] to the [Dispatcher]
     */
    fun add(subscription: Subscription<T>) = apply {
        subscriptionList.add(subscription)
        listeners = sortedListenerArray(subscriptionList)
    }

    /**
     * Removes specified [subscription] to the [Dispatcher]
     */
    fun remove(subscription: Subscription<T>) = apply {
        subscriptionList.remove(subscription)
        listeners = sortedListenerArray(subscriptionList)
    }

    /**
     * Adds specified [subscription] to the [Dispatcher]
     */
    fun addAll(subscription: Collection<Subscription<T>>) = apply {
        subscriptionList.addAll(subscription)
        listeners = sortedListenerArray(subscriptionList)
    }

    /**
     * Removes specified [subscription] to the [Dispatcher]
     */
    fun removeAll(subscription: Collection<Subscription<T>>) = apply {
        subscriptionList.removeAll(subscription)
        listeners = sortedListenerArray(subscriptionList)
    }

    /**
     * Clears this [Dispatcher]
     */
    fun clear() {
        subscriptionList.clear()
        listeners = emptyArray()
    }

    companion object {
        /**
         * Returns an [Array] of [Listener] sorted by their priority by specifying a
         */
        private fun <T: Any> sortedListenerArray(subscriptions: List<Subscription<T>>): Array<Listener<T>> {
            return subscriptions
                    .sortedBy { it.priority }
                    .map { it.listener }
                    .toTypedArray()
        }
    }

}