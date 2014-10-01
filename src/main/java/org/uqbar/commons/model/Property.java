package org.uqbar.commons.model;

import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class Property {

	private Object model;
	private String name;

	public Property(Object model, String name) {
		this.model = model;
		this.name = name;
	}

	public void dependsOn(String... dependencies) {
		ObservableUtils.dependencyOf(model, name, dependencies);
	}

	public void changed(Object newValue) {
		ObservableUtils.firePropertyChanged(model, name, newValue);
	}

	public void changed() {
		ObservableUtils.firePropertyChanged(model, name);
	}

	public void addListener(PropertyChangeListener listener) {
		ObservableUtils.addPropertyListener(model, listener, Arrays.asList(name));
	}

	public static Property property(Object model, String name) {
		return new Property(model, name);
	}

}
