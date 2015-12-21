package org.uqbar.commons.model;

/**
 * An object set executes a given persistence-initialization logic
 * on an {@link Application} object.
 * 
 * You can extend this class for your own application in order to create persistent objects 
 * that needs to be already created for your application. 
 * 
 * @author jfernandes
 */
public class ObjectSet {

	public void execute(Application application) {
		for (Entity entity : this.createSimpleObjects()) {
			application.getRepo(entity.getClass()).create(entity);
		} 
	}

	protected Entity[] createSimpleObjects() {
		return new Entity[] {};
	}
	
}