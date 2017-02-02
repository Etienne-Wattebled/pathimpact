package org.univlille1.pathimpact.sequitur;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.univlille1.pathimpact.element.ElementItf;
import org.univlille1.pathimpact.grammaire.Grammaire;
import org.univlille1.pathimpact.grammaire.Grammaire.Regle;

public class Sequitur {
	private List<ElementItf> stacktrace;
	private Grammaire grammaire;

	public Sequitur(List<ElementItf> elements) {
		stacktrace = new LinkedList<ElementItf>(elements);
	}

	public Grammaire executerSequitur() {
		if (grammaire != null) {
			return grammaire;
		}
		grammaire = new Grammaire();
		ElementItf before = stacktrace.get(0);
		ElementItf last = before;
		ElementItf last2 = null;
		grammaire.ajouterElementDansS(before);
		stacktrace.remove(0);
		for (ElementItf element : stacktrace) {
			grammaire.ajouterElementDansS(element);
			int nb = 2;
			while (nb >= 2) {
				ListIterator<ElementItf> it = grammaire.getS().getListIteratorEnd();
				if (it.hasPrevious()) {
					last = it.previous();
				} else {
					break;
				}
				if (it.hasPrevious()) {
					last2 = it.previous();
				} else {
					break;
				}
				LinkedList<ElementItf> lR = new LinkedList<ElementItf>();
				lR.add(last2);
				lR.add(last);
				Regle r = grammaire.getRegleQuiProduit(lR);
				if (r == null) {
					r = grammaire.ajouterRegle(lR);
					nb = grammaire.appliquerRegleSurS(r);
				} else {
					// La règle existe, il ne faut surtout pas mettre à jour nb et tester la compression davantage si on peut.
					grammaire.appliquerRegleSurS(r);
				}
				grammaire.simplierRegles();
			}
		}
		return grammaire;
	}
}
