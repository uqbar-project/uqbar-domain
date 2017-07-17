package org.uqbar.commons.model.utils;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ScalaBeanInfo implements BeanInfo {

	private static final Log logger = LogFactory.getLog(ScalaBeanInfo.class);

	private static final String SCALA_SETTER_SUFFIX = "_$eq";

	private final BeanInfo delegate;

	private final PropertyDescriptor[] propertyDescriptors;

	public ScalaBeanInfo(Class<?> beanClass) throws IntrospectionException {
		this(Introspector.getBeanInfo(beanClass));
	}

	public ScalaBeanInfo(BeanInfo delegate) throws IntrospectionException {
		this.delegate = delegate;
		this.propertyDescriptors = initPropertyDescriptors(delegate);
	}

	private static PropertyDescriptor[] initPropertyDescriptors(BeanInfo beanInfo) {
		Map<String, PropertyDescriptor> propertyDescriptors =
				new TreeMap<String, PropertyDescriptor>(new PropertyNameComparator());
		for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
			propertyDescriptors.put(pd.getName(), pd);
		}

		for (MethodDescriptor md : beanInfo.getMethodDescriptors()) {
			Method method = md.getMethod();

			if (ReflectionUtils.isObjectMethod(method)) {
				continue;
			}
			if (isScalaSetter(method)) {
				addScalaSetter(propertyDescriptors, method);
			}
			else if (isScalaGetter(method)) {
				addScalaGetter(propertyDescriptors, method);
			}
		}
		return propertyDescriptors.values()
				.toArray(new PropertyDescriptor[propertyDescriptors.size()]);
	}

	private static boolean isScalaGetter(Method method) {
		return method.getParameterTypes().length == 0 &&
				!method.getReturnType().equals(Void.TYPE) &&
				!(method.getName().startsWith("get") ||
						method.getName().startsWith("is"));
	}

	public static boolean isScalaSetter(Method method) {
		return method.getParameterTypes().length == 1 &&
				method.getReturnType().equals(Void.TYPE) &&
				method.getName().endsWith(SCALA_SETTER_SUFFIX);
	}

	private static void addScalaSetter(Map<String, PropertyDescriptor> propertyDescriptors,
	                                   Method writeMethod) {
		String propertyName = writeMethod.getName().substring(0,
				writeMethod.getName().length() - SCALA_SETTER_SUFFIX.length());

		PropertyDescriptor pd = propertyDescriptors.get(propertyName);
		if (pd != null && pd.getWriteMethod() == null) {
			try {
				pd.setWriteMethod(writeMethod);
			}
			catch (IntrospectionException ex) {
				logger.debug("Could not add write method [" + writeMethod + "] for " +
						"property [" + propertyName + "]: " + ex.getMessage());
			}
		}
		else if (pd == null) {
			try {
				pd = new PropertyDescriptor(propertyName, null, writeMethod);
				propertyDescriptors.put(propertyName, pd);
			}
			catch (IntrospectionException ex) {
				logger.debug("Could not create new PropertyDescriptor for " +
						"writeMethod [" + writeMethod + "] property [" + propertyName +
						"]: " + ex.getMessage());
			}
		}
	}

	private static void addScalaGetter(Map<String, PropertyDescriptor> propertyDescriptors,
	                                   Method readMethod) {
		String propertyName = readMethod.getName();

		PropertyDescriptor pd = propertyDescriptors.get(propertyName);
		if (pd != null && pd.getReadMethod() == null) {
			try {
				pd.setReadMethod(readMethod);
			}
			catch (IntrospectionException ex) {
				logger.debug("Could not add read method [" + readMethod + "] for " +
						"property [" + propertyName + "]: " + ex.getMessage());
			}
		}
		else if (pd == null) {
			try {
				pd = new PropertyDescriptor(propertyName, readMethod, null);
				propertyDescriptors.put(propertyName, pd);
			}
			catch (IntrospectionException ex) {
				logger.debug("Could not create new PropertyDescriptor for " +
						"readMethod [" + readMethod + "] property [" + propertyName +
						"]: " + ex.getMessage());
			}
		}
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		return propertyDescriptors;
	}

	public BeanInfo[] getAdditionalBeanInfo() {
		return delegate.getAdditionalBeanInfo();
	}

	public BeanDescriptor getBeanDescriptor() {
		return delegate.getBeanDescriptor();
	}

	public int getDefaultEventIndex() {
		return delegate.getDefaultEventIndex();
	}

	public int getDefaultPropertyIndex() {
		return delegate.getDefaultPropertyIndex();
	}

	public EventSetDescriptor[] getEventSetDescriptors() {
		return delegate.getEventSetDescriptors();
	}

	public Image getIcon(int iconKind) {
		return delegate.getIcon(iconKind);
	}

	public MethodDescriptor[] getMethodDescriptors() {
		return delegate.getMethodDescriptors();
	}
	
	public PropertyDescriptor getPropertyDescriptor(String property){
		String propertyName = property;
		if(property.startsWith("_")){
			propertyName = propertyName.substring(1);
		}
		for (PropertyDescriptor descriptor : this.propertyDescriptors) {
			if(descriptor.getName().equals(propertyName)){
				return descriptor; 
			}
		}
		return null;
	}
	
	public static PropertyDescriptor getPropertyDescriptor(Object bean, String name){
		return getPropertyDescriptor(bean.getClass(), name);
	}
	
	public static PropertyDescriptor getPropertyDescriptor(Class bean, String name){
		try {
			return new ScalaBeanInfo(bean).getPropertyDescriptor(name);
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static PropertyDescriptor[] getPropertyDescriptors(Class bean){
		try {
			return new ScalaBeanInfo(bean.getClass()).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sorts property names alphanumerically to emulate the behavior of {@link
	 * java.beans.BeanInfo#getPropertyDescriptors()}.
	 */
	private static class PropertyNameComparator implements Comparator<String> {

		public int compare(String left, String right) {
			byte[] leftBytes = left.getBytes();
			byte[] rightBytes = right.getBytes();

			for (int i = 0; i < left.length(); i++) {
				if (right.length() == i) {
					return 1;
				}
				int result = leftBytes[i] - rightBytes[i];
				if (result != 0) {
					return result;
				}
			}
			return left.length() - right.length();
		}
	}

}
