package org.uqbar.commons.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Base class for observable objects. Already implements the logic to manage observers for properties.
 * 
 * @author npasserini
 * @author jfernandes
 */
public class ObservableObject {
	private transient PropertyChangeSupport changeSupport;

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.getChangeSupport().addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.getChangeSupport().removePropertyChangeListener(propertyName, listener);
	}

	protected <T> void firePropertyChange(String propertyName, T oldValue, T newValue) {
		this.getChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
	}
	
	// TODO Este método debería ser protected
	/**
	 * Hace un set del field correspondiente a la propiedad y dispara el evento de propertyChange
	 */
	public void setFieldValue(String fieldName, Object value) {
		try {
			Object originalValue = this.getProperty(fieldName);
			Field field = this.getSetter(fieldName).getDeclaringClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(this, value);
			this.firePropertyChange(fieldName, originalValue, value);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error al obtener el valor al field " + fieldName
				+ " de un objeto de tipo " + this.getClass().getSimpleName() + ". "
				+ "Es posible que haya habido un error de conversión de tipos primitivos de Java", e);

			// Otra posibilidad para esta excepción sería que se esté usando un field de una clase que no está
			// en la jerarquía del objeto, pero por la forma en que está construido eso no puede pasar acá.
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException("No se tiene permisos para acceder al field " + fieldName
				+ " de un objeto de tipo " + this.getClass().getSimpleName() + "."
				+ "Esto no debería pasar, reporte este bug al proveedor del framework .", e);

			// Dado que estamos haciendo setAccesible, eso no puede pasar, pero el Java me obliga a manejar
			// esa excepción.
		}
		catch (NoSuchFieldException e) {
			throw new RuntimeException("No se encotró un field con nombre " + fieldName + " en un objeto de tipo "
				+ this.getClass().getSimpleName(), e);
		}
		catch (SecurityException e) {
			throw new RuntimeException("No se tiene permisos para acceder al field " + fieldName
				+ " en un objeto de tipo " + this.getClass().getSimpleName() + ". "
				+ "Esto puede ocurrir porque se está utilizando un SecurityManager, "
				+ "para poder utilizar esta herramienta deberá modificar la política de seguridad.");

		}
	}

	public Object getProperty(String property) {
		return getPropertyValue(this, property);
	}

	public static Object getPropertyValue(Object object, String property) {
		try {
			return PropertyUtils.getProperty(object, property);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Se intentó obtener el valor a la propiedad " + property
				+ " de un objeto, pero el objeto recibido era nulo.", e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException("No se tiene acceso a la propiedad " + property + " de un objeto de tipo "
				+ object.getClass().getSimpleName() + ". Verifique que la propiedad tenga un getter público.", e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException("Al acceder a la propiedad " + property + " de un objeto de tipo "
				+ object.getClass().getSimpleName() + ", el método getter tiró una excepción.", e);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException("Cannot find getter method for the property with name '" + property
				+ "' in an object of type '" + object.getClass().getSimpleName() + "'", e);
		}
	}

	public Method getGetter(String property) {
		return getPropertyDescriptor(this, property).getReadMethod();
	}

	protected Method getSetter(String property) {
		return getPropertyDescriptor(this, property).getWriteMethod();
	}

	public static PropertyDescriptor getPropertyDescriptor(Object object, String property) {
		try {
			PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(object, property);
			if (propertyDescriptor == null) {
				throw new RuntimeException("No se encuentra un property descriptor para la propiedad " + property
					+ " en un objeto de la clase " + object.getClass());
			}
			return propertyDescriptor;
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Se intentó acceder a la propiedad " + property
				+ " de un objeto, pero el objeto recibido era nulo.", e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException("No se tiene acceso a la propiedad " + property + " de un objeto de tipo "
				+ object.getClass().getSimpleName() + ". Verifique que la propiedad tenga un getter / setter público.",
				e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException("Al acceder a la propiedad " + property + " de un objeto de tipo "
				+ object.getClass().getSimpleName() + ", el método setter o getter tiró una excepción.", e);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException("No se encotró el setter o getter correspondiente a la propiedad " + property
				+ " + " + "en un objeto de tipo " + object.getClass().getSimpleName(), e);
		}
	}

	// ********************************************************
	// ** Accessors
	// ********************************************************

	private PropertyChangeSupport getChangeSupport() {
		// Crear el change support en forma lazy permite que cuando un objeto viene de la persistencia se
		// reconstruya esto.
		if (this.changeSupport == null) {
			this.changeSupport = new PropertyChangeSupport(this);
		}
		return this.changeSupport;
	}
}
