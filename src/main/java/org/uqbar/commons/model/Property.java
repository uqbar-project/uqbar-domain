package org.uqbar.commons.model;

import java.beans.PropertyChangeListener;
import java.util.Arrays;

import org.uqbar.commons.model.utils.ObservableUtils;

/**
 * The {@link Property} class reifies an observable property in a model.
 * 
 * This class mostly deprecates the {@link ObservableUtils} helper class for
 * normal application code, since all of its behaviour is exposed here in a more
 * object-oriented fashion
 * 
 * @author flbulgarelli
 * @since 3.4
 */
public class Property {

	private Object model;
	private String name;

	public Property(Object model, String name) {
		this.model = model;
		this.name = name;
	}

	/**
	 * Set ups one or more dependencies for this property in. That is, this
	 * property will fire a <code>propertyChanged</code> event whenever any of
	 * the <code>dependencies</code> get updated.
	 * 
	 * @param dependencies
	 *            the the dependencies to be added to this property
	 */
	public void dependsOn(String... dependencies) {
		ObservableUtils.dependencyOf(model, name, dependencies);
	}

	/**
	 * Fires that this property value has changed, specifying its new value
	 * 
	 * @param newValue
	 */
	public void changed(Object newValue) {
		ObservableUtils.firePropertyChanged(model, name, newValue);
	}

	/**
	 * Fires that this property value has changed
	 */
	public void changed() {
		ObservableUtils.firePropertyChanged(model, name);
	}

	/**
	 * Adds a {@link PropertyChangeListener} to this property
	 */
	public void addListener(PropertyChangeListener listener) {
		ObservableUtils.addPropertyListener(model, listener, Arrays.asList(name));
	}

	/**
	 * DSL'ish constructor for a {@link Property}.
	 * 
	 * @param model
	 *            the object this property belongs to
	 * @param name
	 *            the name of the property
	 * @return a new {@link Property}
	 */
	public static Property property(Object model, String name) {
		return new Property(model, name);
	}

}
