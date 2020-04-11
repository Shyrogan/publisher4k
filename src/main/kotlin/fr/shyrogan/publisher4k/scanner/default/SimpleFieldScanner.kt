package fr.shyrogan.publisher4k.scanner.default

import fr.shyrogan.publisher4k.scanner.SubscriptionScanner
import fr.shyrogan.publisher4k.subscription.Listener
import fr.shyrogan.publisher4k.subscription.Subscribe
import fr.shyrogan.publisher4k.subscription.Subscription
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType




@Suppress("unchecked_cast")
object SimpleFieldScanner: SubscriptionScanner {

    override fun <O : Any> apply(element: AnnotatedElement, instance: O): Subscription<Any>? {
        if(element.javaClass != Field::class.java)
            return null
        element as Field
        element.isAccessible = true
        val annotation = element.getAnnotation(Subscribe::class.java)
        if(annotation == null) {
            return null // This annotation has not specified the event type
        }

        if(Listener::class.java.isAssignableFrom(element.type)) {
            val target = (element.genericType as ParameterizedType).actualTypeArguments[0] as Class<*>
            // At this point we know its a listener
            val listener = element[instance] as Listener<Any>
            return (Subscription(annotation.value, target, listener, element))
        } else if(Subscription::class.java.isAssignableFrom(element.type)) {
            // Easy?
            return (element[instance] as Subscription<Any>)
        }

        return null
    }

}