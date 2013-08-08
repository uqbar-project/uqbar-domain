package org.uqbar.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class MemoryStorage implements Storage {

	private Map<Class<?>, List<?>> businessObjects = new HashMap<Class<?>, List<?>>();

	@Override
	public <T> void add(Class<T> type, T object) {
		this.getObjects(type).add(object);
	}

	@Override
	public <T> void remove(Class<T> type, T object) {
		this.getObjects(type).remove(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjects(Class<T> type) {
		List<T> list = (List<T>) this.businessObjects.get(type);

		if (list == null) {
			list = new ArrayList<T>();
			this.businessObjects.put(type, list);
		}

		return list;
	}

	public <T> void setObjects(Class<T> type, List<T> objects) {
		this.businessObjects.put(type, objects);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getObjects(Class<T> type, Predicate predicate) {
		return CollectionUtils.select(getObjects(type), predicate);
	}

}
