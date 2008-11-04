package org.crank.annotations.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This annotation is more to demonstrate our AJAX
 * support. We don't expect people to use this per se.
 * 
 * Please use Email annotation instead.
 * 
 * @author Rick Hightower
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
public @interface CommonEmail {
	String detailMessage() default "";
	String summaryMessage() default "";
}
