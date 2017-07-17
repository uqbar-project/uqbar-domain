package org.uqbar.commons.model.application;

import org.uqbar.commons.model.Entity;
import org.uqbar.commons.model.Repo;

/**
 * The main application model object that represents the whole application.
 * It's a facade to access the persistent application state but you can extend it with your
 * own methods and therefore responsabilities.
 * 
 * @author jfernandes
 */
public interface Application {

	/**
	 * Returns the appropriated repo for the given entity type.
	 */
	public <T extends Entity> Repo<T> getRepo(Class<? extends T> type);
	
}
