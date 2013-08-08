package org.uqbar.commons.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.uqbar.commons.utils.TransactionalAndObservable;

/**
 * Base class for implementing search objects.
 * 
 * @param <T> The type of the objects to be searched.
 */
@TransactionalAndObservable
public abstract class Search<T>  implements Serializable {
	private static final Logger log = Logger.getLogger(Search.class);
	public static final String RESULTS = "results";
	public static final String SELECTED = "selected";
	public static final String SEARCH = "search";

	private T selected;
	private List<T> results;
	private final Class<T> entityType;

	public Search(Class<T> entityType) {
		this.entityType = entityType;
		results = new ArrayList<T>();
	}

	// ***********************************************************
	// ** Actions
	// ***********************************************************

	/**
	 * The type of objects to be searched by this object.
	 */
	public Class<T> getEntityType() {
		return this.entityType;
	}

	/**
	 * Perform the search. The results will be left in the {@link #RESULTS} property.
	 */
	public void search() {
		this.results = null;
		this.results = this.doSearch();

		// log
		StringBuilder builder = new StringBuilder("Search Result: ");
		for (T object : this.results) {
			builder.append(object + ", ");
		}
		log.debug(builder);
		// endlog

		
		// Adjust selection, nullify if selected object is out-filtered of the new search.
		if (!this.results.contains(this.getSelected())) {
			this.setSelected(null);
		}
	}

	/**
	 * Internal method for subclasses to execute the search.
	 * 
	 * @return The list of results.
	 */
	protected abstract List<T> doSearch();

	/**
	 * Clear the parameters of the search. To be implemented by subclasses.
	 */
	public abstract void clear();

	/**
	 * Elimina el elemento seleccionado del repositorio subyacente.
	 */
	public abstract void removeSelected();

	// ***********************************************************
	// ** Accessors
	// ***********************************************************

	/**
	 * Returns the result of this search associated to the values of the search parameters present at the last
	 * time the {@link #search()} method was invoked.
	 */
	public List<T> getResults() {
		return this.results;
	}

	/**
	 * The selected object between the search results. It gets cleared each time a search is performed.
	 * 
	 * @return The selected object.
	 */
	public T getSelected() {
		return this.selected;
	}

	/**
	 * Changes the selection.
	 * 
	 * @param selected The new selected object.
	 */
	public void setSelected(T selected) {
		this.selected = selected;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}
}
