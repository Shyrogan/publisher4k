package fr.shyrogan.publisher4k.subscription;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Because we don't want to forget our Java classes, you can annotate a field/method with the
 * `@Subscribe` annotation so it can be registered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Subscribe {

    /**
     * @return The priority of the subscription marked by this annotation
     */
    int value() default 0;

}
