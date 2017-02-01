package org.univlille1.pathimpact.grammaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;
import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.element.Evenement;
import org.univlille1.pathimpact.element.Methode;
import org.univlille1.pathimpact.grammaire.Grammaire.Regle;

public class GrammaireTest {
	private Grammaire grammaire;
	
	private Regle regleASupprimer;
	private ElementItf regleASupprimer_e1;
	private ElementItf regleASupprimer_e2;
	
	private Regle regle;
	private ElementItf regle_e1;
	private ElementItf regle_e2;
	private ElementItf regle_e3;
	private ElementItf m;
	private ElementItf m2;
	private ElementItf m3;
	
	private Regle regleADesappliquer;
	
	@Before
	public void setUp() {
		grammaire = new Grammaire();
		LinkedList<ElementItf> l = new LinkedList<ElementItf>();
		// règle à supprimer
		regleASupprimer_e1 = new Methode("run");
		regleASupprimer_e2 = Evenement.RETURN;
		l.add(regleASupprimer_e1);
		l.add(regleASupprimer_e2);
		regleASupprimer = grammaire.ajouterRegle(l);
		
		// règle
		regle_e1 = new Methode("doIt");
		regle_e2 = new Methode("method1");
		regle_e3 = new Methode("method2");
		l.clear();
		l.add(regle_e1);
		l.add(regle_e2);
		l.add(regle_e3);
		regle = grammaire.ajouterRegle(l);
		
		l.clear();
		l.add(regle_e1);
		l.add(regle_e2);
		l.add(regle_e3);
		regleADesappliquer = grammaire.ajouterRegle(l);
		
		m = new Methode("doSomething");
		m2 = new Methode("doSomethingElse");
		m3 = new Methode("doNothing");
		grammaire.ajouterElementDansS(m);
		grammaire.ajouterElementDansS(regle_e1);
		grammaire.ajouterElementDansS(regle_e2);
		grammaire.ajouterElementDansS(regle_e3);
		grammaire.ajouterElementDansS(Evenement.RETURN);
		grammaire.ajouterElementDansS(m3);
		grammaire.ajouterElementDansS(regle_e1);
		grammaire.ajouterElementDansS(regle_e2);
		grammaire.ajouterElementDansS(regleADesappliquer);
		grammaire.ajouterElementDansS(regle_e1);
		grammaire.ajouterElementDansS(regle_e2);
		grammaire.ajouterElementDansS(regle_e3);
		grammaire.ajouterElementDansS(m2);
		grammaire.ajouterElementDansS(Evenement.RETURN);
		grammaire.ajouterElementDansS(regle_e1);
		grammaire.ajouterElementDansS(regle_e2);
		grammaire.ajouterElementDansS(regle_e3);
	}
	
	@Test
	public void testAjouterRegle() {
		Methode m = new Methode("m");
		Methode m2 = new Methode("m2");
		Regle regle = null;
		int nbReglesBefore = grammaire.getNbRegles();
		LinkedList<ElementItf> l = new LinkedList<ElementItf>();
		l.add(m);
		l.add(m2);
		regle = grammaire.ajouterRegle(l);
		assertEquals(nbReglesBefore+1,grammaire.getNbRegles());
		assertNotNull(regle);
		assertEquals(2,regle.getNbElements());
		assertEquals(m,regle.getElement(0));
		assertEquals(m2,regle.getElement(1));
	}
	
	@Test
	public void testSupprimerRegle() {
		int nbReglesBefore = grammaire.getNbRegles();
		grammaire.supprimerRegle(regleASupprimer);
		assertEquals(nbReglesBefore-1,grammaire.getNbRegles());
	}
	
	@Test
	public void testAppliquerRegle() {
		int n = grammaire.appliquerRegleSurS(regle);
		assertEquals(3,n);
		List<ElementItf> l = grammaire.getS().getElements();
		assertEquals(11,l.size());
		Iterator<ElementItf> it = l.listIterator();
		assertEquals(m,it.next());
		assertEquals(regle,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(m3,it.next());
		assertEquals(regle_e1,it.next());
		assertEquals(regle_e2,it.next());
		assertEquals(regleADesappliquer,it.next());
		assertEquals(regle,it.next());
		assertEquals(m2,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(regle,it.next());
	}
	
	@Test
	public void testDesappliquerRegle() {
		int n = grammaire.desappliquerRegleSurS(regleADesappliquer);
		assertEquals(1,n);
		ListIterator<ElementItf> it = grammaire.getS().listIterator();
		assertEquals(19,grammaire.getS().getNbElements());
		assertEquals(m,it.next());
		assertEquals(regle_e1,it.next());
		assertEquals(regle_e2,it.next());
		assertEquals(regle_e3,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(m3,it.next());
		assertEquals(regle_e1,it.next());
		assertEquals(regle_e2,it.next());
		assertEquals(regle_e1,it.next());
		assertEquals(regle_e2,it.next());
		assertEquals(regle_e3,it.next());
		assertEquals(regle_e1,it.next());
		assertEquals(regle_e2,it.next());
		assertEquals(regle_e3,it.next());
		assertEquals(m2,it.next());
		assertEquals(Evenement.RETURN,it.next());
		assertEquals(regle_e1,it.next());
		assertEquals(regle_e2,it.next());
		assertEquals(regle_e3,it.next());
	}
	
	@Test
	public void testSimplifierRegles() {
		assertEquals(3,grammaire.getNbRegles());
		grammaire.simplierRegles();
		assertEquals(0,grammaire.getNbRegles());
		// Le même résultat que désappliquer règle
		assertEquals(19,grammaire.getS().getNbElements());
	}
}
