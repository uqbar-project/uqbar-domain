package org.uqbar.commons.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableSample {
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public ObservableSample(int bar) {
		this.bar = bar;
	}

	private int bar;

	public int getBar() {
		return bar;
	}

	public void setBar(int bar) {
		this.bar = bar;
	}

	public int getFoobar() {
		return getBar() + 1;
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName,
				listener);
	}

	public void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

	public PropertyChangeListener[] getPropertyChangeListeners() {
		return propertyChangeSupport.getPropertyChangeListeners();
	}

	public PropertyChangeListener[] getPropertyChangeListeners(
			String propertyName) {
		return propertyChangeSupport
				.getPropertyChangeListeners(propertyName);
	}

}