package org.univlille1.pathimpact.sequitur;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.element.Methode;
import org.univlille1.pathimpact.grammaire.Grammaire;
import org.univlille1.pathimpact.grammaire.Grammaire.Regle;

public class SequiturTest {
	@Test
	public void testSequitur1() {
		List<ElementItf> elements = new LinkedList<ElementItf>();
		Methode b = new Methode("b");
		Methode e = new Methode("e");
		elements.add(b);elements.add(b);elements.add(e);elements.add(b);elements.add(e);elements.add(e);
		elements.add(b);elements.add(e);elements.add(b);elements.add(e);elements.add(b);elements.add(b);
		elements.add(e);elements.add(b);elements.add(e);elements.add(e);
		
		Sequitur sequitur = new Sequitur(elements);
		Grammaire grammaire = sequitur.executerSequitur();
		
		// Vérifie qu'il existe 3 règles
		assertEquals(grammaire.getNbRegles(),3);
		
		// Une règle regle1 qui produit BE
		LinkedList<ElementItf> expected = new LinkedList<ElementItf>();
		expected.add(b);
		expected.add(e);
		Regle regle1 = grammaire.getRegleQuiProduit(expected);
		assertNotNull(regle1);
		
		// Une règle regle2 qui produit deux fois regle1
		expected.clear();
		expected.add(regle1);expected.add(regle1);
		Regle regle2 = grammaire.getRegleQuiProduit(expected);
		assertNotNull(regle2);
		
		// Une règle regle3 qui produit b regle2 e
		expected.clear();
		expected.add(b);expected.add(regle2);expected.add(e);
		Regle regle3 = grammaire.getRegleQuiProduit(expected);
		assertNotNull(regle3);
		
		List<ElementItf> s = grammaire.getS().getElements();
		assertEquals(3,s.size());
		
		// On vérifie S
		ListIterator<ElementItf> it = grammaire.getS().listIterator();
		assertEquals(regle3,it.next());
		assertEquals(regle2,it.next());
		assertEquals(regle3,it.next());
	}
}
