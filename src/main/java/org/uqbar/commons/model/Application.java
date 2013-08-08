package org.uqbar.commons.model;

/**
 * The main application model object that represents the whole application.
 * It's a facade to access the persistent application state but you can extend it with your
 * own methods and therefore responsabilities.
 * 
 * @author jfernandes
 */
public interface Application {

	/**
	 * Returns the appropriated home for the given entity type.
	 */
	public <T extends Entity> Home<T> getHome(Class<? extends T> type);
	
}
