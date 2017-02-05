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
		// CF https://courses.cs.washington.edu/courses/csep590a/07au/lectures/lecture05small.pdf
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
		// CF http://www.cc.gatech.edu/~orso/papers/orso.term.harrold.ICSE04.pdf
		// Attention : La règle qui produit CEr est une règle supplémentaire au document
		// D'après ce site : http://www.sequitur.info/ on obtient bien la règle qui produit CEr car ce trigramme est en doublon
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
		
		assertEquals(9, grammaire.getNbRegles());

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

		// regle4 = C E r 
		expected.clear();
		expected.add(c);
		expected.add(e);
		expected.add(Evenement.RETURN);
		Regle regle4 =  grammaire.getRegleQuiProduit(expected);
		
		// regle5 = A regle4
		expected.clear();
		expected.add(a);
		expected.add(regle4);
		Regle regle5 = grammaire.getRegleQuiProduit(expected);

		// regle6 = r r
		expected.clear();
		expected.add(Evenement.RETURN);
		expected.add(Evenement.RETURN);
		Regle regle6 = grammaire.getRegleQuiProduit(expected);

		// regle7 = x M
		expected.clear();
		expected.add(Evenement.END_OF_PROGRAM);
		expected.add(m);
		Regle regle7 = grammaire.getRegleQuiProduit(expected);

		// regle8 = D regle6
		expected.clear();
		expected.add(d);
		expected.add(regle6);
		Regle regle8 = grammaire.getRegleQuiProduit(expected);

		// regle9 = regle4 F r regle8
		expected.clear();
		expected.add(regle4);
		expected.add(f);
		expected.add(Evenement.RETURN);
		expected.add(regle8);
		Regle regle9 = grammaire.getRegleQuiProduit(expected);
		
		assertNotNull(regle1);
		assertNotNull(regle2);
		assertNotNull(regle3);
		assertNotNull(regle4);
		assertNotNull(regle5);
		assertNotNull(regle6);
		assertNotNull(regle7);
		assertNotNull(regle8);
		assertNotNull(regle9);
		
		// On vérifie S
		assertEquals(20,grammaire.getS().getNbElements());
		ListIterator<ElementItf> it = grammaire.getS().listIterator();
		assertEquals(regle2,it.next());
		assertEquals(regle1,it.next());
		assertEquals(regle3,it.next());
		assertEquals(regle5,it.next());
		assertEquals(regle6,it.next());
		assertEquals(Evenement.END_OF_PROGRAM,it.next());
		assertEquals(regle2,it.next());
		assertEquals(c,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(regle3,it.next());
		assertEquals(regle7,it.next());
		assertEquals(regle5,it.next());
		assertEquals(regle8,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(regle7,it.next());
		assertEquals(b,it.next());
		assertEquals(regle9,it.next());
		assertEquals(regle9,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(Evenement.END_OF_PROGRAM,it.next());
	}
	
	@Test
	public void testSequitur3() {
		//M B r A C D r E r r r r x M B G r r r x M B C F r r r r x
		// CF https://cse.unl.edu/~grother/papers/issre03.pdf
		Methode m = new Methode("M");
		Methode b = new Methode("B");
		Methode a = new Methode("A");
		Methode c = new Methode("C");
		Methode d = new Methode("D");
		Methode e = new Methode("E");
		Methode f = new Methode("F");
		Methode g = new Methode("G");
		
		LinkedList<ElementItf> elements = new LinkedList<ElementItf>();
		elements.add(m);
		elements.add(b);
		elements.add(Evenement.RETURN);
		elements.add(a);
		elements.add(c);
		elements.add(d);
		elements.add(Evenement.RETURN);
		elements.add(e);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.END_OF_PROGRAM);
		elements.add(m);
		elements.add(b);
		elements.add(g);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.END_OF_PROGRAM);
		elements.add(m);
		elements.add(b);
		elements.add(c);
		elements.add(f);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.RETURN);
		elements.add(Evenement.END_OF_PROGRAM);
		
		Sequitur sequitur = new Sequitur(elements);
		Grammaire grammaire = sequitur.executerSequitur();
		assertEquals(4,grammaire.getNbRegles());
		
		LinkedList<ElementItf> expected = new LinkedList<ElementItf>();
		// regle1 = r r
		expected.add(Evenement.RETURN);
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
		
		// regle4 = x regle2
		expected.clear();
		expected.add(regle1);
		expected.add(Evenement.RETURN);
		Regle regle4 = grammaire.getRegleQuiProduit(expected);
		
		assertNotNull(regle1);
		assertNotNull(regle2);
		assertNotNull(regle3);
		assertNotNull(regle4);
	}
}
