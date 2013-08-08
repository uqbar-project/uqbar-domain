package org.uqbar.commons.model;

import java.lang.reflect.Method;

public interface IModel<T> {
	
	T getSource();

	Object getProperty(String propertyName);
	
	Method getGetter(String property);
}
