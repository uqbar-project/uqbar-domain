package org.uqbar.commons.model;

import java.util.List;

/**
 * Una home representa la interfaz hacia el lugar físico donde se persisten los objetos.
 * Está asociada a un tipo de objeto dado (a una clase), y permite realizar operaciones
 * sobre ese tipo de objetos.
 * 
 * Estas operaciones relacionadas con la persistencia de los objetos.
 * Por ejemplo: buscar bajo cierto criterio, eliminar un objeto, agregar uno nuevo, etc.
 * 
 * Permite desacoplar al dominio de la lógica propia de la tecnología de la persistencia y de la manera en que 
 * se persiste: archivo, base de datos relacional, xml, etc.
 * 
 * El termino Home proviene de la idea de que es el "hogar" de cierto tipo de objetos, y que
 * aquí es donde "viven".
 * Si quiero buscar una instancia voy a poder hacerlo a través de su Home correspondiente.
 * 
 * @see CollectionBasedHome
 * 
 * @author npasserini
 */
public interface Home<T extends Entity> {
	
	// ********************************************************
	// ** Información administrativa
	// ********************************************************
	
	public Class<T> getEntityType();
	
	// ********************************************************
	// ** Search
	// ********************************************************
	
	public T searchById(int id);

	public List<T> searchByExample(T example);

	public T createExample();
	
	public List<T> allInstances();

	// ********************************************************
	// ** CRUD
	// ********************************************************
	
	public void create(T object);

	public void update(T object);

	public void delete(T object);

}
