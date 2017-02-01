package org.univlille1.pathimpact.sequitur;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;
import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.element.Evenement;
import org.univlille1.pathimpact.element.Methode;
import org.univlille1.pathimpact.grammaire.Grammaire;
import org.univlille1.pathimpact.grammaire.Grammaire.Regle;

public class SequiturTest {
	@Test
	public void testSequitur1() {
		List<ElementItf> elements = new LinkedList<ElementItf>();
		Methode b = new Methode("b");
		Methode e = new Methode("e");
		elements.add(b);
		elements.add(b);
		elements.add(e);
		elements.add(b);
		elements.add(e);
		elements.add(e);
		elements.add(b);
		elements.add(e);
		elements.add(b);
		elements.add(e);
		elements.add(b);
		elements.add(b);
		elements.add(e);
		elements.add(b);
		elements.add(e);
		elements.add(e);

		Sequitur sequitur = new Sequitur(elements);
		Grammaire grammaire = sequitur.executerSequitur();

		// Vérifie qu'il existe 3 règles
		assertEquals(grammaire.getNbRegles(), 3);

		// Une règle regle1 qui produit BE
		LinkedList<ElementItf> expected = new LinkedList<ElementItf>();
		expected.add(b);
		expected.add(e);
		Regle regle1 = grammaire.getRegleQuiProduit(expected);
		assertNotNull(regle1);

		// Une règle regle2 qui produit deux fois regle1
		expected.clear();
		expected.add(regle1);
		expected.add(regle1);
		Regle regle2 = grammaire.getRegleQuiProduit(expected);
		assertNotNull(regle2);

		// Une règle regle3 qui produit b regle2 e
		expected.clear();
		expected.add(b);
		expected.add(regle2);
		expected.add(e);
		Regle regle3 = grammaire.getRegleQuiProduit(expected);
		assertNotNull(regle3);

		List<ElementItf> s = grammaire.getS().getElements();
		assertEquals(3, s.size());

		// On vérifie S
		ListIterator<ElementItf> it = grammaire.getS().listIterator();
		assertEquals(3,grammaire.getS().getNbElements());
		assertEquals(regle3, it.next());
		assertEquals(regle2, it.next());
		assertEquals(regle3, it.next());
	}

	@Test
	public void testSequitur2() {
		Methode m = new Methode("M");
		Methode b = new Methode("B");
		Methode g = new Methode("G");
		Methode c = new Methode("C");
		Methode f = new Methode("F");
		Methode a = new Methode("A");
		Methode e = new Methode("E");
		Methode d = new Methode("D");

		LinkedList<ElementItf> l = new LinkedList<ElementItf>();
		// MBGrGrrACErrrx
		l.add(m);
		l.add(b);
		l.add(g);
		l.add(Evenement.RETURN);
		l.add(g);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(a);
		l.add(c);
		l.add(e);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.END_OF_PROGRAM);

		// MBCrGrrx
		l.add(m);
		l.add(b);
		l.add(c);
		l.add(Evenement.RETURN);
		l.add(g);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.END_OF_PROGRAM);

		// MACErDrrrx
		l.add(m);
		l.add(a);
		l.add(c);
		l.add(e);
		l.add(Evenement.RETURN);
		l.add(d);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.END_OF_PROGRAM);

		// MBCErFrDrrCErFrDrrrx
		l.add(m);
		l.add(b);
		l.add(c);
		l.add(e);
		l.add(Evenement.RETURN);
		l.add(f);
		l.add(Evenement.RETURN);
		l.add(d);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(c);
		l.add(e);
		l.add(Evenement.RETURN);
		l.add(f);
		l.add(Evenement.RETURN);
		l.add(d);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.RETURN);
		l.add(Evenement.END_OF_PROGRAM);

		Sequitur sequitur = new Sequitur(l);
		Grammaire grammaire = sequitur.executerSequitur();

		assertEquals(8, grammaire.getNbRegles());

		LinkedList<ElementItf> expected = new LinkedList<ElementItf>();

		// regle1 = G r
		expected.add(g);
		expected.add(Evenement.RETURN);
		Regle regle1 = grammaire.getRegleQuiProduit(expected);

		// regle2 = M B
		expected.clear();
		expected.add(m);
		expected.add(b);
		Regle regle2 = grammaire.getRegleQuiProduit(expected);

		// regle3 = regle1 r
		expected.clear();
		expected.add(regle1);
		expected.add(Evenement.RETURN);
		Regle regle3 = grammaire.getRegleQuiProduit(expected);

		// regle4 = A C E r
		expected.clear();
		expected.add(a);
		expected.add(c);
		expected.add(e);
		expected.add(Evenement.RETURN);
		Regle regle4 = grammaire.getRegleQuiProduit(expected);

		// regle5 = r r
		expected.clear();
		expected.add(Evenement.RETURN);
		expected.add(Evenement.RETURN);
		Regle regle5 = grammaire.getRegleQuiProduit(expected);

		// regle6 = x M
		expected.clear();
		expected.add(Evenement.END_OF_PROGRAM);
		expected.add(m);
		Regle regle6 = grammaire.getRegleQuiProduit(expected);

		// regle7 = D regle5
		expected.clear();
		expected.add(d);
		expected.add(regle5);
		Regle regle7 = grammaire.getRegleQuiProduit(expected);

		// regle8 = C E r F r regle7
		expected.clear();
		expected.add(c);
		expected.add(e);
		expected.add(Evenement.RETURN);
		expected.add(f);
		expected.add(Evenement.RETURN);
		expected.add(regle7);
		Regle regle8 = grammaire.getRegleQuiProduit(expected);
		
		assertNotNull(regle1);
		assertNotNull(regle2);
		assertNotNull(regle3);
		assertNotNull(regle4);
		assertNotNull(regle5);
		assertNotNull(regle6);
		assertNotNull(regle7);
		assertNotNull(regle8);
		
		// On vérifie S
		assertEquals(20,grammaire.getS().getNbElements());
		ListIterator<ElementItf> it = grammaire.getS().listIterator();
		assertEquals(regle2,it.next());
		assertEquals(regle1,it.next());
		assertEquals(regle3,it.next());
		assertEquals(regle4,it.next());
		assertEquals(regle5,it.next());
		assertEquals(Evenement.END_OF_PROGRAM,it.next());
		assertEquals(regle2,it.next());
		assertEquals(c,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(regle3,it.next());
		assertEquals(regle6,it.next());
		assertEquals(regle4,it.next());
		assertEquals(regle7,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(regle6,it.next());
		assertEquals(b,it.next());
		assertEquals(regle8,it.next());
		assertEquals(regle8,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(Evenement.END_OF_PROGRAM,it.next());
	}
}
