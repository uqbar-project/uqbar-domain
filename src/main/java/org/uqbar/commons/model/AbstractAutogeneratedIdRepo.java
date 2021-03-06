package org.uqbar.commons.model;

import java.util.List;

import org.uqbar.commons.model.exceptions.UserException;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

/**
 * Base {@link Repo} implementation for reusing the code to generate
 * id's, and between {@link CollectionBasedRepo}.
 * 
 * In another language this could be trait ;)
 * 
 * @author jfernandes
 */
public abstract class AbstractAutogeneratedIdRepo<T extends Entity> implements Repo<T> {
	private int nextId = 1;
	
	protected int getNextId() {
		return this.nextId++;
	}

	// **** CREATE ****
	
	@Override
	public void create(T object) {
		this.validateCreate(object);
		if (object.getId() == null) {
			object.setId(this.getNextId());
		}
		this.effectiveCreate(object);
	}
	
	/**
	 * Abstract method. Subclasses must implement the logic
	 * to add this object into the repository (persist it for the first time).
	 */
	protected abstract void effectiveCreate(T object);
	
	/**
	 * Template method: subclasses might override to execute some type of
	 * validation related to the object creation.
	 * By default it delegates to Entity.validateCreate
	 */
	protected void validateCreate(T object) {
		validateWithAnnotations(object);
		object.validateCreate();
	}

	protected void validateWithAnnotations(T object) {
		Validator validator = new Validator();
		// collect the constraint violations
		List<ConstraintViolation> violations = validator.validate(object);

		if (violations.size()>0) {
			StringBuffer message = new StringBuffer();
			for (ConstraintViolation violation : violations) {
				message.append(violation.getMessage()).append(System.lineSeparator());
			}
		  throw new UserException(message.toString());
		}
	}

	// **** DELETE ****
	
	@Override
	public void delete(T object) {
		this.validateDelete(object);
		this.effectiveDelete(object);
	}

	/**
	 * Template method: subclasses might override to execute some type of
	 * validation related to the object deleting.
	 * By default it delegates to Entity.validateDelete
	 */
	protected void validateDelete(T object) {
		object.validateDelete();
	}
	
	protected abstract void effectiveDelete(T object);
	
}
