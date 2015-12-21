package org.uqbar.commons.model;

import java.util.List;

import org.uqbar.commons.utils.TransactionalAndObservable;

/**
 * Default implementation of a {@link Search}, delegating into a {@link Repo} object and performing a search
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
	private Repo<T> repo;

	/**
	 * Straightforward constructor.
	 * 
	 * @param repo The repo into which we will delegate our searches.
	 */
	public SearchByExample(Repo<T> repo) {
		super(repo.getEntityType());
		this.repo = repo;

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
		this.example = this.repo.createExample();
	}

	@Override
	protected List<T> doSearch() {
		return this.repo.searchByExample(this.example);
	}

	@Override
	public void removeSelected() {
		this.repo.delete(this.getSelected());
		this.search();
	}

	public void setExample(T example) {
		this.example = example;
	}
}