package org.uqbar.commons.model;

import java.util.List;

/**
 * Un <b>repo</b> representa la interfaz hacia el lugar físico donde se persisten los objetos.
 * Se asocia a un tipo de objeto dado (a una clase), y permite realizar operaciones
 * sobre ese tipo de objetos.
 * 
 * Estas operaciones relacionadas con la persistencia de los objetos.
 * Por ejemplo: buscar bajo cierto criterio, eliminar un objeto, agregar uno nuevo, etc.
 * 
 * Permite desacoplar al dominio de la lógica propia de la tecnología de la persistencia y de la manera en que 
 * se persiste: archivo, base de datos relacional, xml, etc.
 * 
 * Antiguamente se le daba el término "Home" que proviene de la idea de 
 * que es el "hogar" de cierto tipo de objetos, y que
 * aquí es donde "viven". Actualmente se asocia la idea de un repositorio,
 * o lugar de almacenamiento de dichos objetos. 
 * 
 * Si quiero buscar una instancia voy a poder hacerlo a través de su Repo correspondiente.
 * 
 * <hr>
 * 
 * <b>Repo</b> is an interface that communicates to objects persistence model (named T).
 * You can perform several CRUD operations to this T objects: search (by id or passing an example or prototypical object).
 * create, update or delete messages are some of them..
 * 
 * @see CollectionBasedRepo
 * 
 * @author npasserini
 */
public interface Repo<T extends Entity> {
	
	// ********************************************************
	// ** Administrative information - Info para el compilador
	// ********************************************************
	
	public Class<T> getEntityType();
	
	// ********************************************************
	// ** Search - Búsqueda
	// ********************************************************
	
	public T searchById(int id);

	public List<T> searchByExample(T example);

	public T createExample();
	
	public List<T> allInstances();

	// **************************************************************************
	// ** CRUD Operations - Operaciones de Alta, Baja, Modificación y Consulta
	// **************************************************************************
	
	public void create(T object);

	public void update(T object);

	public void delete(T object);

}
