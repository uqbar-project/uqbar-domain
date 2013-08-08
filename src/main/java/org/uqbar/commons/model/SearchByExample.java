package org.uqbar.commons.model;

import java.util.List;

import org.uqbar.commons.utils.TransactionalAndObservable;

/**
 * Default implementation of a {@link Search}, delegating into a {@link Home} object and performing a search
 * by example.
 * 
 * @author npasserini
 * 
 * @param <T>
 */
@TransactionalAndObservable
public class SearchByExample<T extends Entity> extends Search<T> {
	public static final String EXAMPLE = "example";

	private T example;
	private Home<T> home;

	/**
	 * Straightforward constructor.
	 * 
	 * @param home The home into which we will delegate our searches.
	 */
	public SearchByExample(Home<T> home) {
		super(home.getEntityType());
		this.home = home;

		init();
	}

	protected void init() {
		this.createExampleObject();
		this.search();
	}

	/**
	 * The object to serve as an example for the searches to be performed. Any properties set into this object
	 * will be used as a filter when the search method is invoked.
	 */
	public T getExample() {
		return this.example;
	}

	@Override
	public void clear() {
		this.createExampleObject();
	}

	protected void createExampleObject() {
		this.example = this.home.createExample();
	}

	@Override
	protected List<T> doSearch() {
		return this.home.searchByExample(this.example);
	}

	@Override
	public void removeSelected() {
		this.home.delete(this.getSelected());
		this.search();
	}

	public void setExample(T example) {
		this.example = example;
	}
}