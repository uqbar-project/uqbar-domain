package org.uqbar.commons.model;

import java.lang.reflect.Method;

/**
 * @deprecated Esto no se usa mas. Ahora con aspectos no hace falta heredar de ningun lado
 */
@Deprecated
public interface IModel<T> {
	
	T getSource();

	Object getProperty(String propertyName);
	
	Method getGetter(String property);
}
