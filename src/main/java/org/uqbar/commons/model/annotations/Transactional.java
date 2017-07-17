package org.uqbar.commons.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to declare that the a given class is going to be part of the
 * transaction unit of work, and there for is kind of a transactional resource.
 * In our case, in OOP, it would be a "transactional object".
 * 
 * @author jfernandes
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

}
