package org.uqbar.commons.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.uqbar.commons.utils.Observable;
import org.uqbar.commons.utils.ReflectionUtils;

/**
 * Utility class for {@link Observable} object functions, for example to force
 * triggering a property change method.
 *
 * @author jfernandes
 * @author flbulgarelli
 */
public class ObservableUtils {

	/**
	 * Set ups one or more dependencies for the given <code>property</code> in
	 * the given <code>model</code>. That is, the property <code>property</code>
	 * will fire a <code>propertyChanged</code> event whenever any of the
	 * <code>dependencies</code> get updated.
	 * 
	 * @since 3.4
	 * @param model
	 *            the model with the given properties
	 * @param property
	 *            the property to which dependencies will be added
	 * @param dependencies
	 *            the the dependencies to be added to the given property
	 */
	public static void dependencyOf(final Object model, final String property, String... dependencies) {
		addPropertyListener(model, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChanged(model, property);
			}
		}, dependencies);
	}

	public static void firePropertyChanged(Object model, String propertyName, Object newValue) {
		Method firePropertyChangedMethod = ReflectionUtils.findMethod(model.getClass(), "firePropertyChange", true, new Class[] { String.class, Object.class, Object.class });
		if(firePropertyChangedMethod != null){
			ReflectionUtils.invoke(model, firePropertyChangedMethod, propertyName, null, newValue);
		}
	}

	public static void firePropertyChanged(Object model, String property) {
		firePropertyChanged(model, property, ReflectionUtils.invokeGetter(model, property));
	}

	public static void addPropertyListener(Object model, PropertyChangeListener listener, String...properties) {
		addPropertyListener(model, listener, Arrays.asList(properties));
	}

	public static void addPropertyListener(Object model, PropertyChangeListener listener, List<String> properties) {
		for (String property: properties) {
			ReflectionUtils.invokeMethod(model, "addPropertyChangeListener", new Object[]{property, listener});
		}
	}

}
