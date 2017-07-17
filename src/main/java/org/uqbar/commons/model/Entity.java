package org.uqbar.commons.model;

import java.io.Serializable;

/**
 * 
 * Clase base para todos los objetos de dominio 
 * 
 * <hr>
 * 
 * Base class for all domain objects.
 * 
 * @author npasserini
 * @author nny
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 435983647238787181L;
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
	
	/** hashCode based on id **/
	@Override
	public int hashCode() {
		if (this.id == null) {
			return super.hashCode();
		}
		return this.id;
	}

	/** Equality based on id **/
	@Override
	public boolean equals(Object obj) {
		try {
			if (obj == null) {
				return false;
			}
			Entity otherEntity = (Entity) obj;
			if (this.id == null || otherEntity.getId() == null) {
				return super.equals(obj);
			}
			return this.id == otherEntity.getId();
		} catch (ClassCastException e) {
			return false;
		}
	}
	
	/**
	 * Subclasses might override to implement their own validations
	 * when the user tries to makes this instance "persistent" by storing it
	 * into a {@link Repo} object.
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
