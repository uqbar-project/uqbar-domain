package org.uqbar.commons.utils;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.Predicate;

public interface Storage {
	public <T> void add(Class<T> type, T object);
	public <T> void remove(Class<T> type, T object);
	public <T> List<T> getObjects(Class<T> type);
	public <T> Collection<T> getObjects(Class<T> type, Predicate predicate);

}
