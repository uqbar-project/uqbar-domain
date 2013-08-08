package org.uqbar.commons.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

public class Model<T> implements IModel<T> {
	private T target;

	public Model(T target) {
		this.target = target;
	}

	// TODO Este método debería ser protected
	/**
	 * Hace un set del field correspondiente a la propiedad y dispara el evento de propertyChange
	 */
	
	public void setFieldValue(String fieldName, Object value) {
		try {
			// Object originalValue = this.getProperty(fieldName);
			Field field = this.getSetter(fieldName).getDeclaringClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(this, value);
//			this.firePropertyChange(fieldName, originalValue, value);
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
		return getPropertyValue(target, property);
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

	@Override
	public Method getGetter(String property) {
		return getPropertyDescriptor(target, property).getReadMethod();
	}

	protected Method getSetter(String property) {
		return getPropertyDescriptor(target, property).getWriteMethod();
	}

	public static PropertyDescriptor getPropertyDescriptor(Object object, String property) {
		try {
			PropertyDescriptor propertyDescriptor;
			if(!property.contains(".")){
				propertyDescriptor = ScalaBeanInfo.getPropertyDescriptor(object, property);
			}else{
				String[] parts = property.split("\\."); 
				propertyDescriptor = ScalaBeanInfo.getPropertyDescriptor(object, parts[0]);
				Class<?> nestedTarget = propertyDescriptor.getPropertyType();
				for (int i = 1; i < parts.length; i++) {
					propertyDescriptor = Model.propertyDescriptor(nestedTarget, parts[i]);
					nestedTarget = propertyDescriptor.getPropertyType();
				}
			}
			if (propertyDescriptor == null) {
				throw new RuntimeException("No se encuentra un property descriptor para la propiedad " + property
					+ " en un objeto de la clase " + object.getClass() + " o ese clase no tiene dicha propiedad.");
			}
			return propertyDescriptor;
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Se intentó acceder a la propiedad " + property
				+ " de un objeto, pero el objeto recibido era nulo.", e);
		}
	}
	
	private static PropertyDescriptor propertyDescriptor(Class<?> beanClass, String propertyName){
		final PropertyDescriptor[] propertyDescriptors = ScalaBeanInfo.getPropertyDescriptors(beanClass);
		
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if(propertyDescriptor.getName().equals(propertyName)){
				return propertyDescriptor;
			}
		}
		
		throw new RuntimeException("No se encuentra un property descriptor para la propiedad " + propertyName
				+ " en un objeto de la clase " + beanClass);
		
	}

	public T getSource() {
		return target;
	}

	public void setTarget(T target) {
		this.target = target;
	}

}
