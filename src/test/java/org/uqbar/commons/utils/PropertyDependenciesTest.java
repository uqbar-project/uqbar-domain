package org.uqbar.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.uqbar.commons.model.ObservableUtils;

public class PropertyDependenciesTest {

	@Test(expected = RuntimeException.class)
	public void fallaSiLaPropiedadNoExiste() {
		ObservableUtils.firePropertyChanged(new ObservableSample(0), "foo");
	}

	@Test
	// TODO esto es correcto?
	public void noHaceNadaSiElObjetoNoEsObservable() {
		ObservableUtils.firePropertyChanged(new UnobservableSample(), "baz");
	}

	@Test
	public void notificaSiLaProiedadExisteConElValorActual() {
		PropertyChangeListener listener = mock(PropertyChangeListener.class);
		ObservableSample sample = new ObservableSample(10);
		sample.addPropertyChangeListener("bar", listener);

		ObservableUtils.firePropertyChanged(sample, "bar");

		verify(listener, times(1)).propertyChange(newValueIs(10));
	}

	@Test
	public void cuandoSeCreaUnaDependenciaSeAgreganObservadoresParaLasDependencias() {
		ObservableSample model = new ObservableSample(10);

		ObservableUtils.dependencyOf(model, "foobar", "bar");

		assertEquals(1, model.getPropertyChangeListeners("bar").length);
	}

	@Test
	public void cuandoSeCreaUnaDependenciaYSeLaNotificaActualizaLaPropiedadCalculada() {
		PropertyChangeListener listener = mock(PropertyChangeListener.class);
		ObservableSample sample = new ObservableSample(10);
		sample.addPropertyChangeListener("foobar", listener);
		ObservableUtils.dependencyOf(sample, "foobar", "bar");
		
		ObservableUtils.firePropertyChanged(sample, "bar");

		verify(listener, times(1)).propertyChange(newValueIs(11));
	}

	private PropertyChangeEvent newValueIs(final int expectedNewValue) {
		return argThat(new CustomMatcher<PropertyChangeEvent>("newValue") {
			public boolean matches(Object arg0) {
				return ((PropertyChangeEvent) arg0)//
						.getNewValue().equals(expectedNewValue);
			}
		});
	}
}
