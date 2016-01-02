package org.uqbar.commons.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An object set executes a given persistence-initialization logic
 * on an {@link Application} object.
 * 
 * You can extend this class for your own application in order to create persistent objects 
 * that needs to be already created for your application. 
 * 
 * @author jfernandes
 */
public class ObjectSet<T extends Entity> {

	public void execute(Application application) {
		for (T entity : this.createSimpleObjects()) {
			Repo<T> repoT = (Repo<T>) application.getRepo(entity.getClass());
			repoT.create(entity);
		}
	}

	protected List<T> createSimpleObjects() {
		return new ArrayList<T>();
	}
	
}