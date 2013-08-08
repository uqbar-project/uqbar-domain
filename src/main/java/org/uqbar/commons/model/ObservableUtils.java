package org.uqbar.commons.model;

import java.lang.reflect.Method;

import org.uqbar.commons.utils.Observable;
import org.uqbar.commons.utils.ReflectionUtils;

/**
 * Utility class for {@link Observable} object functions, for example to force
 * triggering a property change method.
 * 
 * @author jfernandes
 */
public class ObservableUtils {

	public static void firePropertyChanged(Object model, String propertyName, Object newValue) {
		Method firePropertyChangedMethod = ReflectionUtils.findMethod(model.getClass(), "firePropertyChange", true, new Class[] { String.class, Object.class, Object.class });
		if(firePropertyChangedMethod != null){
			ReflectionUtils.invoke(model, firePropertyChangedMethod, propertyName, null, newValue);
		}
	}

}
