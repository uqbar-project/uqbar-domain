package org.uqbar.commons.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;

/**
 * 
 * Implementación de {@link Repo} que mantiene los objetos en memoria, dentro 
 * de una collection.
 * 
 * Esta implementacion ¡NO PERSISTE!
 * Es decir que, cada vez que se baja la VM (java), todos los datos se pierden.
 * 
 * @see Repo
 * 
 * @author npasserini
 * 
 * <hr>
 * 
 * Base implementation of {@link Repo} holding all objects in a collection in-memory,
 * so it is NOT PERSISTENT. Every time you close your application, all data is lost.
 * 
 */
public abstract class CollectionBasedRepo<T extends Entity> extends AbstractAutogeneratedIdRepo<T> {
	private List<T> objects = new ArrayList<T>();

	// ********************************************************
	// ** Getters y setters de objects para respetar el contrato Java Bean
	// ********************************************************

	public List<T> getObjects() {
		return objects;
	}

	public void setObjects(List<T> objects) {
		this.objects = objects;
	}
	
	// ********************************************************
	// ** Altas, bajas y modificaciones.
	// ********************************************************

	@Override
	protected void effectiveCreate(T object) {
		this.objects.add(object);
	}

	@Override
	public void update(T object) {
		this.objects.remove(object);
		this.objects.add(object);
	}

	@Override
	protected void effectiveDelete(T object) {
		this.objects.remove(object);
	}
	
	// ********************************************************
	// ** Búsquedas
	// ********************************************************
	
	@Override
	public List<T> searchByExample(final T example) {
		return (List<T>) CollectionUtils.select(this.objects, this.getCriterio(example));
	}

	protected abstract Predicate<T> getCriterio(T example);

	@Override
	public T searchById(int id) {
		for (T candidate : this.allInstances()) {
			if (candidate.getId().equals(id)) {
				return candidate;
			}
		}

		// TODO Mejorar el mensaje de error
		throw new RuntimeException("No se encontró el objeto con el id: " + id);
	}

	@Override
	public List<T> allInstances() {
		return this.objects;
	}

	// ********************************************************
	// ** Criterios de búsqueda
	// ********************************************************

	protected Predicate<T> getCriterioTodas() {
		return new Predicate<T>() {
			@Override
			public boolean evaluate(T arg) {
				return true;
			}
		};
	}

	protected Predicate<T> getCriterioPorId(final Integer id) {
		return new Predicate<T>() {
			@Override
			public boolean evaluate(T arg) {
				return arg.getId().equals(id);
			}
		};
	}

}