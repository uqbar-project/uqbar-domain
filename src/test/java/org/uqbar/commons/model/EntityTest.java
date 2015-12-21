package org.uqbar.commons.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntityTest {

	Entity clienteVacio;
	Entity cliente1;
	Entity otroCliente1;
	Entity cliente2;
	Object otraCosa;
	
	@SuppressWarnings("serial")
	@Before
	public void setUp() throws Exception {
		clienteVacio = new Entity() { };
		cliente1 = new Entity() { };
		cliente1.setId(1);
		otroCliente1 = new Entity() { };
		otroCliente1.setId(1);
		cliente2 = new Entity() { };
		cliente2.setId(2);
		otraCosa = "Otra cosa";
	}

	@Test
	public void testEntidadNuevaEsNew() {
		Assert.assertTrue(clienteVacio.isNew());
	}

	@Test
	public void testEntidadConIdNoEsNew() {
		Assert.assertFalse(cliente1.isNew());
	}
	
	@Test
	public void testDosEntidadesConMismoIdSonIguales() {
		Assert.assertTrue(cliente1.equals(otroCliente1));
	}
	
	@Test
	public void testDosEntidadesConDistintoIdSonDistintos() {
		Assert.assertFalse(cliente1.equals(cliente2));
	}
	
	@Test
	public void testDosEntidadesUnoConIdNuloNoSonIguales() {
		Assert.assertFalse(cliente1.equals(clienteVacio));
	}
	
	@Test
	public void testDosEntidadesUnoConIdNuloNoSonIgualesAlReves() {
		Assert.assertFalse(clienteVacio.equals(cliente1));
	}
	
	@Test
	public void testUnaEntidadNoEsIgualAOtraCosa() {
		Assert.assertFalse(cliente1.equals(otraCosa));
	}

	@Test
	public void testDosEntidadesConMismoIdCompartenHashCode() {
		Assert.assertEquals(cliente1.hashCode(), otroCliente1.hashCode());
	}
	
	@Test
	public void testDosEntidadesConDistintoIdTienenDiferenteHashCode() {
		Assert.assertNotEquals(cliente1.hashCode(), cliente2.hashCode());
	}
	
	@Test
	public void testDosEntidadesUnoConIdNuloNoTienenIgualHashCode() {
		Assert.assertNotEquals(cliente1.hashCode(), clienteVacio.hashCode());
	}
	
	@Test
	public void testUnaEntidadNoTieneMismoHashCodeQueOtraCosa() {
		Assert.assertNotEquals(cliente1.hashCode(), otraCosa.hashCode());
	}

}
