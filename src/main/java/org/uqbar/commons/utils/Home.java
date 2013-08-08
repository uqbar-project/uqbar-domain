package org.uqbar.commons.utils;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.uqbar.commons.model.ObservableObject;

@Transactional
public class Home<T> {
	public static final String OBJECTS = "objects";
	private Class<T> type;

	public Home(Class<T> type) {
		this.type = type;
	}
	
	protected Storage getStorage() {
		return ApplicationContext.getInstance().getSingleton(Storage.class);
	}
	
	public void add(T object) {
		this.getStorage().add(this.type, object);
//		this.firePropertyChange(OBJECTS, null, this.getObjects());
	}
	
	public void remove(T object) {
		this.getStorage().remove(this.type, object);
//		this.firePropertyChange(OBJECTS, null, this.getObjects());
	}
	
	public List<T> getObjects() {
		return this.getStorage().getObjects(this.type);
	}
	
	public Collection<T> getObjects(Predicate predicate) {
		return this.getStorage().getObjects(this.type, predicate);
	}
	
}
