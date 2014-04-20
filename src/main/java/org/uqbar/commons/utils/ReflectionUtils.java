package org.uqbar.commons.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import org.uqbar.commons.model.ScalaBeanInfo;


public class ReflectionUtils {

	public static Object invokeMethod(Object model, String actionName) {
		try {
			return model.getClass().getMethod(actionName, new Class[]{}).invoke(model, new Object[]{});
		}
		catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		catch (SecurityException e) {
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
    public static void invokeMethod(final Object model, final String actionName, final Class[] c, final Object... args) {
        try {
        	findMethod(model.getClass(), actionName, true, c).invoke(model, args);
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to call method with name [" + actionName + "] and parameter types [" + Arrays.toString(c) + "] into target object [" + model + "]", e);
        }
    }
    
    /**
     * Invokes a method and handle reflection exceptions. It uses variable
     * parameter syntax.
     */
    public static Object invoke(final Object object, final Method method, final Object... params) {
        try {
            return method.invoke(object, params);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException("Exception during method invocation", e);
        } catch (final Exception e) {
            throw new RuntimeException("Cannot invoke method", e);
        }
    }
	
    public static void invokeMethod(Object object, String method, Object... args) {
        Class[] clazz = new Class[args.length] ;
        int i = 0;
        for (Object object2 : args) {
        	if(object2 == null){
        		clazz[i] = Object.class;
        	}else{
        		clazz[i] = object2.getClass();
        	}
            i++;
        }
        invokeMethod(object, method, clazz, args);
    }
    
    public static void invokeSetter(final Object object, final String property, final Object value) {
        if (object == null)
            return;

        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(object, property);

        try {
            invoke(object, propertyDescriptor.getWriteMethod(), value);

        } catch (final Exception ex) {
            throw new RuntimeException("Se ha producido un error invocando " + "set"
                    + StringUtils.capitalize(property), ex);
        }

    }
    
    public static Object invokeGetter(final Object object, final String property) {
    	try{
    		PropertyDescriptor propertyDescriptor = getPropertyDescriptor(object, property);
    		return invoke(object, propertyDescriptor.getReadMethod());
    		
    	}catch(final Exception ex){
    		throw new RuntimeException("Error invocando el getter  " + "get" + StringUtils.capitalize(property), ex);
    	}
    }
	
	public static List<Field> getAllFields(Object object){
		ArrayList<Field> fields = new ArrayList<Field>();
		Class<? extends Object> cls = object.getClass();
		
		while (cls != null) {
			CollectionUtils.addAll(fields, cls.getDeclaredFields());
			cls = cls.getSuperclass();
		}
		
		return fields;
	}
	
	public static List<Field> getAllFieldAnnotation(Object object, Class<? extends Annotation> annotation){
		List<Field> fields = getAllFields(object);
		List<Field> fieldWithAnnotation = new ArrayList<Field>();
		
		for (Field field : fields) {
            if(field.isAnnotationPresent(annotation)){
            	fieldWithAnnotation.add(field);            
            }
        }
        return fieldWithAnnotation;        
    }
    
	public static List<Field> getAllFieldToPredicate(Object object, Predicate<Field> predicate){
		if(object == null){
			return new ArrayList<Field>();
		}
		List<Field> fields = getAllFields(object);
		
        return (List<Field>) CollectionUtils.select(fields, predicate);        
    }
	
    public static Object readField(final Object target, final Field field) {
        try {
        	if(Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers())){
        		field.setAccessible(true);
        	}
            return field.get(target);
        } catch (final Exception e) {
            throw new RuntimeException("Cannot get field value", e);
        }
    }
    
    public static Object readField(final Object object, final String property) {
    	return readField(object, getField(object.getClass(), property));
    }
    
    
    public static Field getField(final Class clazz, final String property) {
    	 Class<?> currentClass = clazz;
    	 Field field = null;
         while (currentClass != null && field == null){
        	 field = findField(currentClass, property);
        	 currentClass = currentClass.getSuperclass();
         }
         if(field == null){
        	 throw new RuntimeException("Cannot recover field " + property + " from class " + clazz);
         }
         return field;
    }
    
    public static Field findField(final Class clazz, final String property) {
        try {
            return clazz.getDeclaredField(property);
        }catch (NoSuchFieldException nsfE) {
        	try {
        		return clazz.getField(property);
        	} catch (final Exception e) {
        		return null;        		
        	}
    	} catch (final Exception e) {
    		return null;
    	}
    }
    
	public static Method findMethod(Class<?> clazz, String name, boolean matchTypes, Class[] paramTypes) {
        Class<?> searchType = clazz;
        while (!Object.class.equals(searchType) && searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType
                    .getMethods() : searchType.getDeclaredMethods());
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (name.equals(method.getName())
                        && ( !matchTypes || (paramTypes.length == method.getParameterTypes().length))) {

                    boolean found = true;
                    Class<?>[] methodParameterTypes = method
                            .getParameterTypes();

                    for (int j = 0; j < methodParameterTypes.length; j++) {
                        found = methodParameterTypes[j].isAssignableFrom(paramTypes[j]);
                        if (!found)
                            break;
                    }

                    if (found){
                    	method.setAccessible(true);
                    	return method;
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        // Si es un find... porque se tira una exception si no se encuentra el método?
//        throw new RuntimeException("No method with name [" + name + "] and argument types [" + Arrays.toString(paramTypes) + "] in class [" + clazz + "]");
        return null;
    }
	
	public static PropertyDescriptor getPropertyDescriptor(Object object, String property) {
		try {
			PropertyDescriptor propertyDescriptor = ScalaBeanInfo.getPropertyDescriptor(object, property);
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
	}
	
	public static boolean conteinsAnyAnnotation(Object object, Class<? extends Annotation>... anotations){
		Class<?> clazz = object.getClass();
		for (Class<? extends Annotation> annotation: anotations) {
			if(clazz.isAnnotationPresent(annotation)){
				return true;
			}
		}
		return false;
	}

	
	public static boolean isBasicType(Class<?> type) {
		return type.isAssignableFrom(String.class)||
				type.isAssignableFrom(Number.class) ||
				type.isAssignableFrom(Enum.class) ||
				type.isAssignableFrom(Boolean.class) ||
				type.isAssignableFrom(Date.class) ||
				//primitives
				type.isPrimitive();
	}
	
	public static boolean isObjectMethod(Method method) {
		try {
			Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
			return true;
		} catch (SecurityException ex) {
			return false;
		} catch (NoSuchMethodException ex) {
			return false;
		}
	}
	
	public static <T> T newInstanceForName(String className){
		try {
			return newInstance(Class.forName(className));
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException("No existe la clase: " + className, e);
		}
	}

	public static <T> T newInstance(Class<?> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("No se pudo instanciar la clase: " + clazz, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
