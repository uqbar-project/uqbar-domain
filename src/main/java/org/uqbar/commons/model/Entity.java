package org.uqbar.commons.model;

import java.io.Serializable;

/**
 * Base class for all domain objects.
 * 
 * @author npasserini
 * @author nny
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isNew() {
		return this.id == null;
	}
	
	/**
	 * Subclasses might override to implement their own validations
	 * when the user tries to makes this instance "persistent" by storing it
	 * into a {@link Home} object.
	 */
	public void validateCreate() {
		// nothing by default
	}
	
	/**
	 * Subclasses might override to implement their own validations when 
	 * the user tries to delete a persistent instance.
	 */
	public void validateDelete() {
		// nothing by default
	}
	
}
