package org.uqbar.commons.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that describes dependencies of a calculated property
 * 
 * @author flbulgarelli
 * @since 3.4
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies {

	String[] value();
}
