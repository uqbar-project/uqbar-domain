package org.uqbar.commons.utils;

import java.util.HashMap;
import java.util.Map;

import org.uqbar.commons.model.Entity;
import org.uqbar.commons.model.Repo;

/**
 * El contexto de la aplicación.
 */
public class ApplicationContext {
	private static ApplicationContext instance = new ApplicationContext();
	private Map<Object, Object> singletons = new HashMap<Object, Object>();

	// ********************************************************
	// ** Instance management
	// ********************************************************

	public static ApplicationContext getInstance() {
		return instance;
	}

	/**
	 * Prepara un ApplicationContext a partir de una configuración. El {@link ApplicationContext} creado se
	 * guarda y será obtenido a partir de ahora cada vez que se invoque a {@link #getInstance()}.
	 * 
	 * @param configuration La configuración que se desea utilizar.
	 * @return El {@link ApplicationContext} creado.
	 */
	public static ApplicationContext create(ApplicationContextConfiguration configuration) {
		instance = new ApplicationContext();

		configuration.configure(instance);

		return instance;
	}

	// ********************************************************
	// ** Singletons
	// ********************************************************

	public <T> T getSingleton(Object key) {
		return this.internalGetSingleton(key, //
			"No existe un singleton registrado bajo la key: " + key);
	}

	public <T> void configureSingleton(Object key, T singleton) {
		this.singletons.put(key, singleton);
	}

	// ********************************************************
	// ** Repos
	// ********************************************************

	public <T> Repo<? extends Entity> getRepo(Class<T> type) {
		return this.internalGetSingleton(type, //
			"No existe un repo registrado para la clase: " + type.getSimpleName());
	}

	public <T> void configureRepo(Class<T> type, Repo<? extends Entity> repo) {
		this.singletons.put(type, repo);
	}

	// ********************************************************
	// ** Internal
	// ********************************************************

	@SuppressWarnings("unchecked")
	private <T> T internalGetSingleton(Object key, String message) {
		T singleton = (T) this.singletons.get(key);

		if (singleton == null) {
			throw new RuntimeException(message);
		}

		return singleton;
	}
}
