package fr.shyrogan.publisher4k

import fr.shyrogan.publisher4k.Publisher
import fr.shyrogan.publisher4k.subscription.Listener
import fr.shyrogan.publisher4k.subscription.Subscribe
import fr.shyrogan.publisher4k.subscription.subscription
import org.junit.Test
import kotlin.system.measureTimeMillis

class PerformanceTest {

    private val publisher = Publisher()

    /**@Subscribe(String::class)
    val listener = Listener<String> {

    }**/
    /**val kotlinListener = subscription(Listener<String> { })**/

    @Test
    fun measurePerformanceRegistration() {
        println("registering")
        val listener = SimpleListener()
        val time = measureTimeMillis {
            for(i in 0..1000) {
                publisher.register(SimpleListener())
            }
        }
        println("$time")
    }

    @Test
    fun measurePerformancePublish() {
        println("publishing")
        publisher.register(SimpleListener())
        val text = object : Cancellable() {}
        val time = measureTimeMillis {
            for(i in 0..1_000_000) {
                publisher.publish(text)
            }
        }
        println("$time")
    }

    @Test
    fun testRegistration() {
        val listener = SimpleListener()
        println("Should be registered")
        publisher.register(listener)
        publisher.publish("Hea")
        println("Should be unregistered")
        publisher.unregister(listener)
        publisher.publish("Hea")
    }


    class SimpleListener {
        @Subscribe
        val listener = Listener { e: String ->

        }
    }

}