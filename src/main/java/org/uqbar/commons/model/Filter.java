package org.uqbar.commons.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Annotation que se utiliza para que se pueda filtrar por ese atributo
 */
@Target({ElementType.METHOD, ElementType.FIELD}) @Retention(RetentionPolicy.RUNTIME)
public @interface Filter {}
